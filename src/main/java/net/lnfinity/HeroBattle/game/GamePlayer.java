package net.lnfinity.HeroBattle.game;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.tasks.Task;
import net.lnfinity.HeroBattle.utils.ActionBar;
import net.md_5.bungee.api.ChatColor;
import net.samagames.gameapi.json.Status;
import net.zyuiop.MasterBundle.StarsManager;
import net.zyuiop.coinsManager.CoinsManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class GamePlayer {

	private UUID playerID;
	private String playerName;

	private boolean playing = true;

	private double gainMultiplier = 1.0;
	private int starsGained = 0;
	private int coinsGained = 0;

	private int originalElo = 0;
	private int elo = 0;

	private PlayerClass classe = null;

	private int jumps = 2;
	private int maxJumps = 2;
	private int percentage = 0;
	private int lives = 3;
	private int additionalLives = 0;

	private int remainingDoubleDamages = 0;
	private int remainingInvisibility = 0;
	private int remainingReducedIncomingDamages = 0;
	private int remainingRespawnInvincibility = 0;

	private int remainingTimeWithMoreJumps = 0;

	private UUID lastDamager = null;

	private List<PlayerClass> classesAvailable = new ArrayList<PlayerClass>();
	private List<Task> tasks = new ArrayList<Task>();

	private final BukkitTask updateTimersTask;

	/**
	 * Avoid the death to be handled multiple times.
	 */
	private boolean deathHandled = false;

	/**
	 * The jumps left count can only be reset every ten ticks
	 * to avoid (n+1)th jumps.
	 */
	private boolean jumpsCountLocked = false;

	/**
	 * When a jump is in progress, no concurrent jump can be done
	 * by this player.
	 */
	private boolean jumpLocked = false;


	private long percentageInflicted = 0l;
	private int playersKilled = 0;
	
	private int killsRank = 0;
	private int percentageRank = 0;


	public GamePlayer(UUID id) {
		playerID = id;
		playerName = Bukkit.getServer().getPlayer(id).getName();

		// TODO Better way than these ugly variables
		updateTimersTask = Bukkit.getScheduler().runTaskTimer(HeroBattle.getInstance(), new Runnable() {
			@Override
			public void run() {
				if(!(HeroBattle.getInstance().getGame().getStatus() == Status.InGame))
					return;

                if(remainingDoubleDamages == 0
						&& remainingInvisibility == 0
						&& remainingReducedIncomingDamages == 0
						&& remainingRespawnInvincibility == 0
						&& remainingTimeWithMoreJumps == 0) {
					return;
				}


				if(remainingTimeWithMoreJumps != 0) {
					remainingTimeWithMoreJumps--;

					if(remainingTimeWithMoreJumps == 0) {
                        setMaxJumps(2, 0);
                    }
				}

				if(remainingDoubleDamages != 0) {
					remainingDoubleDamages--;
				}

				if(remainingInvisibility != 0) {
					remainingInvisibility--;

					if(remainingInvisibility == 0) {
						Player player = Bukkit.getPlayer(playerID);
						if(player != null && player.isOnline())
							HeroBattle.getInstance().getGame().updatePlayerArmor(player);
					}
				}

				if(remainingRespawnInvincibility != 0) {
					remainingRespawnInvincibility--;
				}

				if(remainingReducedIncomingDamages != 0) {
					remainingReducedIncomingDamages--;
				}

                updateActionBar();
            }
		}, 20l, 20l);
	}

	public int getJumps() {
		return jumps;
	}

	public void setJumps(int jumps) {
		// The jumps cannot be reset when locked; they are locked ten ticks after
		// a call to this setJumps method.
		// This to avoid the PlayerMoveEvent to reset this at the beginning
		// of the jump, when the player is close to the ground.
		if(jumpsCountLocked && jumps >= getJumps()) return;

		this.jumps = jumps;


		// Lock manager
		jumpsCountLocked = true;
		Bukkit.getScheduler().runTaskLater(HeroBattle.getInstance(), new Runnable() {
			@Override
			public void run() {
				jumpsCountLocked = false;
			}
		}, 10l);


		// If there isn't any jump left, the fly mode is removed, so the PlayerToggleFlyEvent
		// is not called anymore and the player falls more naturally
		if(getJumps() <= 0) {
			Bukkit.getPlayer(this.getPlayerUniqueID()).setAllowFlight(false);
		}
	}

	public int getMaxJumps() {
		return maxJumps;
	}

	public void setMaxJumps(int maxJumps, int duration) {
		this.maxJumps = maxJumps;
		this.remainingTimeWithMoreJumps = duration;

		setJumpLocked(false);
		setJumps(maxJumps);

		updateActionBar();

		Player player = Bukkit.getPlayer(playerID);
		if(player != null)
			player.setAllowFlight(true); // Ensures the player is immediately allowed to jump
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		setPercentage(percentage, null);
	}

	public void setPercentage(int percentage, GamePlayer aggressor) {
		if(!isPlaying() || getPlayerClass() == null) return;

		int oldPercentage = this.percentage;

		if(getRemainingReducingIncomingDamages() != 0 && percentage >= oldPercentage) {
			percentage -= (percentage - oldPercentage) / 2;
		}

		this.percentage = percentage;

		if(aggressor != null) aggressor.addPercentageInflicted(percentage - oldPercentage);


		Player player = Bukkit.getPlayer(playerID);

		if(getPercentage() >= getPlayerClass().getMaxResistance()) {
			if(aggressor != null) setLastDamager(aggressor.getPlayerUniqueID());

			HeroBattle.getInstance().getGame().onPlayerDeath(playerID, DeathType.KO);

			if(player != null) {
				player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_LARGE, 10);

				player.setLevel(0);
			}
		}

		else {
			if (player != null) {

				player.setLevel(0);
				player.setTotalExperience(0);

				player.setLevel(getPercentage());
				player.setExp(((float) getPercentage()) / ((float) getPlayerClass().getMaxResistance()));

				HeroBattle.getInstance().getGame().updatePlayerArmor(player);
			}
		}

		HeroBattle.getInstance().getScoreboardManager().update(this);
	}

	public int getTotalLives() {
		return lives + additionalLives;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getAdditionalLives() {
		return additionalLives;
	}

	public void setAdditionalLives(int additionalLives) {
		this.additionalLives = additionalLives;
	}

	public void gainLife() {
		Player player = Bukkit.getPlayer(playerID);
		Validate.notNull(player, "Bukkit Player object null in GamePlayer.gainLife ?! (UUID " + playerID + ")");

		if(getLives() < getPlayerClass().getLives()) {
			lives++;
		}
		else {
			additionalLives++;
			player.setMaxHealth(player.getMaxHealth() + 2);
		}

		player.setHealth(player.getHealth() + 2);
	}

	public void looseLife() {
		final Player player = Bukkit.getPlayer(playerID);
		Validate.notNull(player, "Bukkit Player object null in GamePlayer.looseLife ?! (UUID " + playerID + ")");

		if(additionalLives != 0) {
			additionalLives--;
			player.setMaxHealth(player.getMaxHealth() - 2);
			Bukkit.getScheduler().runTaskLater(HeroBattle.getInstance(), new Runnable() {
				@Override
				public void run() {
					player.setHealth(player.getMaxHealth());
				}
			}, 2l);
		}
		else {
			lives--;

			if(lives != 0) {
				player.setHealth(player.getHealth() - 2);
			}
		}
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean bool) {
		playing = bool;
	}

	public int getRemainingDoubleDamages() {
		return remainingDoubleDamages;
	}

	public void addRemainingDoubleDamages(int remainingDoubleDamages) {
		this.remainingDoubleDamages += remainingDoubleDamages;
		updateActionBar();
	}

	public int getRemainingInvisibility() {
		return remainingInvisibility;
	}

	public void addRemainingInvisibility(int remainingInvisibility) {
		this.remainingInvisibility += remainingInvisibility;

		HeroBattle.getInstance().getGame().updatePlayerArmor(Bukkit.getPlayer(playerID));
		updateActionBar();
	}

	public int getRemainingReducingIncomingDamages() {
		return remainingReducedIncomingDamages;
	}

	public void addRemainingReducedIncomingDamages(int remainingReducedIncomingDamages) {
		this.remainingReducedIncomingDamages += remainingReducedIncomingDamages;
		updateActionBar();
	}

	public UUID getLastDamager() {
		return lastDamager;
	}

	public void setLastDamager(UUID lastDamager) {
		this.lastDamager = lastDamager;
	}

	public PlayerClass getPlayerClass() {
		return classe;
	}

	public void setPlayerClass(PlayerClass classe) {
		this.classe = classe;
		if (classe != null) {
			lives = classe.getLives();

			// Reset of the multiplier, only if the game is not started
			// (else, the class was effectively chosen randomly).
			if(HeroBattle.getInstance().getGame().getStatus() != Status.InGame) {
				gainMultiplier = 1.0;
			}

			Player player = Bukkit.getPlayer(playerID);
			if(player != null) {
				getPlayerClass().setClassTeam(player);
			}
		}

		else {
			lives = 3;
			gainMultiplier = 1.4;

			Player player = Bukkit.getPlayer(playerID);
			if(player != null) {
				PlayerClass.setRandomClassTeam(player);
			}
		}

		if(classe != null) {
			ActionBar.sendPermanentMessage(Bukkit.getPlayer(playerID), ChatColor.GREEN + "Classe sélectionnée : " + ChatColor.DARK_GREEN + classe.getName());
		}
		else {
			ActionBar.sendPermanentMessage(Bukkit.getPlayer(playerID), ChatColor.GREEN + "Classe sélectionnée : " + ChatColor.DARK_GREEN + "aléatoire");
		}
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public UUID getPlayerUniqueID() {
		return playerID;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void doubleJump() {

		if(jumpLocked) return; // nop


		Player player = Bukkit.getServer().getPlayer(playerID);

		if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
			setJumps(maxJumps);
		}

		if (getJumps() > 0) {

			// The jump is locked when the velocity is applied, to avoid the player to
			// do dozens of jumps by spam-right-clicking or double-jumping.
			// The jumps count left is not reset directly but ten ticks after, to avoid it being
			// overwritten by the PlayerMoveEvent checking if the player is on the ground.
			final int futureJumps = getJumps() - 1;
			jumpLocked = true;
			Bukkit.getScheduler().runTaskLater(HeroBattle.getInstance(), new Runnable() {
				@Override
				public void run() {
					if(getJumps() == futureJumps + 1) {
						setJumps(futureJumps);
						jumpLocked = false;
					}
				}
			}, 6l);

			// The velocity is applied
			Vector direction = player.getLocation().getDirection().multiply(0.5);
			Vector vector = new Vector(direction.getX(), 0.9, direction.getZ());
			player.setVelocity(vector);
		}
	}

	public boolean playTask(Task t) {
		boolean min = false;
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getClass() == t.getClass()) {
				tasks.get(i).playTask();
				tasks.remove(i);
				min = true;
			}
		}
		return min;
	}

	public void addTask(Task t) {
		tasks.add(t);
	}

	public boolean hasTask(Task t) {
		for (Task task : tasks) {
			if (task.getClass() == t.getClass()) {
				return true;
			}
		}
		return false;
	}

	public List<PlayerClass> getAvaibleClasses() {
		return classesAvailable;
	}

	public void setAvaibleClasses(List<PlayerClass> avaible) {
		this.classesAvailable = avaible;
	}
	
	public void addAvaibleClass(PlayerClass theClass) {
		this.classesAvailable.add(theClass);
	}
	
	public int getOriginalElo() {
		return originalElo;
	}

	public void setOriginalElo(int originalElo) {
		this.originalElo = originalElo;
	}

	public int getElo() {
		return elo;
	}

	public void setElo(int elo) {
		this.elo = elo;
	}

	public long getPercentageInflicted() {
		return percentageInflicted;
	}

	public void addPercentageInflicted(long percentageInflicted) {
		this.percentageInflicted += percentageInflicted;
	}

	public int getPlayersKilled() {
		return playersKilled;
	}

	public void addPlayersKilled() {
		this.playersKilled++;
	}

	public boolean isDeathHandled() {
		return deathHandled;
	}

	public void setDeathHandled(boolean deathHandled) {
		this.deathHandled = deathHandled;
	}

	public int getRemainingRespawnInvincibility() {
		return remainingRespawnInvincibility;
	}

	public void setRespawning() {
		this.remainingRespawnInvincibility = 2;
		updateActionBar();
	}

	public void setJumpLocked(boolean jumpLocked) {
		this.jumpLocked = jumpLocked;
	}


	public void creditCoins(final int amount, final String why) {
		Bukkit.getScheduler().runTaskAsynchronously(HeroBattle.getInstance(), new Runnable() {
			@Override
			public void run() {
				int realAmount = (int) Math.ceil(((double) amount) * gainMultiplier);
				coinsGained += CoinsManager.syncCreditJoueur(playerID, realAmount, true, true, why);
			}
		});
	}

	public void creditStars(int amount, String why) {
		amount = (int) Math.ceil(((double) amount) * gainMultiplier);
		StarsManager.creditJoueur(playerID, amount, why);

		starsGained += amount;
	}

	public int getStarsGained() {
		return starsGained;
	}

	public int getCoinsGained() {
		return coinsGained;
	}

	public int getKillsRank() {
		return killsRank;
	}

	public void setKillsRank(int killsRank) {
		this.killsRank = killsRank;
	}

	public int getPercentageRank() {
		return percentageRank;
	}

	public void setPercentageRank(int percentageRank) {
		this.percentageRank = percentageRank;
	}


	private void updateActionBar() {

		if(!(HeroBattle.getInstance().getGame().getStatus() == Status.InGame))
			return;


		Player player = Bukkit.getPlayer(playerID);
		List<String> currentStatus = new ArrayList<>();

		if(remainingTimeWithMoreJumps != 0) {
			if (getMaxJumps() == 3)
				currentStatus.add(ChatColor.RED + "Triple sauts (" + remainingTimeWithMoreJumps + ")");
			else
				currentStatus.add(ChatColor.RED + "Sauts : " + getMaxJumps() + "× (" + remainingTimeWithMoreJumps + ")");
		}

		if(remainingDoubleDamages != 0) {
			currentStatus.add(ChatColor.DARK_GREEN + "Double dommages (" + remainingDoubleDamages + ")");
		}

		if(remainingInvisibility != 0) {
			currentStatus.add(ChatColor.GRAY + "Invisible (" + remainingInvisibility + ")");
		}

		if(remainingRespawnInvincibility != 0) {
			currentStatus.add(ChatColor.LIGHT_PURPLE + "Invulnérable (" + remainingRespawnInvincibility + ")");
		}

		if(remainingReducedIncomingDamages != 0) {
			currentStatus.add(ChatColor.LIGHT_PURPLE + "Dommages reçus réduits (" + remainingReducedIncomingDamages + ")");
		}


		if(player == null || !player.isOnline()) return;

		if(currentStatus.size() == 0) {
			ActionBar.removeMessage(player, true);
		}
		else {
			ActionBar.sendPermanentMessage(player, StringUtils.join(currentStatus, ChatColor.DARK_GRAY + " - " + ChatColor.RESET));
		}
	}
}
