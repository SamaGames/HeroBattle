package net.lnfinity.HeroBattle.game;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.classes.*;
import net.lnfinity.HeroBattle.tasks.*;
import net.lnfinity.HeroBattle.utils.*;
import net.md_5.bungee.api.ChatColor;
import net.samagames.api.games.*;
import org.apache.commons.lang.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;
import org.bukkit.scheduler.*;
import org.bukkit.util.Vector;

import java.util.*;


/**
 * Represents a player in the game. Every player has a wrapper except moderators. Instances can be
 * retrieved using `HeroBattle.getGamePlayer(UUID)`
 */
public class HeroBattlePlayer extends GamePlayer
{
	private double gainMultiplier = 1.0;

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

	private int remainingThorns = 0;
	private float thornsEfficienty = 0.0f; // percentage of damages inflicted back

	private int remainingTimeWithMoreJumps = 0;

	private UUID lastDamager = null;
	private Map<UUID, Assist> assists = new HashMap<>();

	private List<PlayerClass> classesAvailable = new ArrayList<PlayerClass>();
	private List<Task> tasks = new ArrayList<Task>();

	private BukkitTask updateEffectsTask;

	/**
	 * Avoid the death to be handled multiple times.
	 */
	private boolean deathHandled = false;

	/**
	 * The jumps left count can only be reset every ten ticks to avoid (n+1)th jumps.
	 */
	private boolean jumpsCountLocked = false;

	/**
	 * When a jump is in progress, no concurrent jump can be done by this player.
	 */
	private boolean jumpLocked = false;


	private long percentageInflicted = 0l;
	private int playersKilled = 0;

	private int killsRank = 0;
	private int percentageRank = 0;

	/**
	 * Creates a new playing player. Should be invoked just after joining.
	 *
	 * @param id
	 */
	public HeroBattlePlayer(UUID id)
	{
		super(Bukkit.getServer().getPlayer(id));

		startEffectsUpdaterTask();
	}

	private void startEffectsUpdaterTask()
	{
		// TODO Better way than these ugly variables

		updateEffectsTask = Bukkit.getScheduler().runTaskTimer(HeroBattle.get(), () -> {
			if (!(HeroBattle.get().getGame().getStatus() == Status.IN_GAME))
				return;


			if (remainingTimeWithMoreJumps != 0)
			{
				remainingTimeWithMoreJumps--;

				if (remainingTimeWithMoreJumps == 0)
				{
					setMaxJumps(2, 0);
				}
			}

			if (remainingDoubleDamages != 0)
			{
				remainingDoubleDamages--;
			}

			if (remainingInvisibility != 0)
			{
				remainingInvisibility--;

				if (remainingInvisibility == 0)
				{
					Player player = getPlayerIfOnline();
					if (player != null && player.isOnline())
						HeroBattle.get().getGame().updatePlayerArmor(player);
				}
			}

			if (remainingRespawnInvincibility != 0)
			{
				remainingRespawnInvincibility--;
			}

			if (remainingReducedIncomingDamages != 0)
			{
				remainingReducedIncomingDamages--;
			}

			if (getRemainingThorns() != 0)
			{
				remainingThorns = getRemainingThorns() - 1;
			}


			updateActionBar();
		}, 20l, 20l);
	}

	public int getJumps()
	{
		return jumps;
	}

	public void setJumps(int jumps)
	{
		// The jumps cannot be reset when locked; they are locked ten ticks after
		// a call to this setJumps method.
		// This to avoid the PlayerMoveEvent to reset this at the beginning
		// of the jump, when the player is close to the ground.
		if (jumpsCountLocked && jumps >= getJumps()) return;

		this.jumps = jumps;


		// Lock manager
		jumpsCountLocked = true;
		Bukkit.getScheduler().runTaskLater(HeroBattle.get(), () -> jumpsCountLocked = false, 10l);


		// If there isn't any jump left, the fly mode is removed, so the PlayerToggleFlyEvent
		// is not called anymore and the player falls more naturally
		if (getJumps() <= 0)
		{
			Player player = getPlayerIfOnline();
			if (player != null)
				player.setAllowFlight(false);
		}
	}

	public int getMaxJumps()
	{
		return maxJumps;
	}

	public void setMaxJumps(int maxJumps, int duration)
	{
		this.maxJumps = maxJumps;
		this.remainingTimeWithMoreJumps = duration;

		setJumpLocked(false);
		setJumps(maxJumps);

		updateActionBar();

		Player player = getPlayerIfOnline();
		if (player != null)
			player.setAllowFlight(true); // Ensures the player is immediately allowed to jump
	}

	public int getPercentage()
	{
		return percentage;
	}

	/**
	 * Custom player percentage changing (ae player walking in fire).
	 *
	 * @param percentage
	 */
	public void setPercentage(int percentage)
	{
		setPercentage(percentage, null);
	}

	/**
	 * Change the current percentage of the player if the last damage comes from a player
	 *
	 * @param percentage The new percentage
	 * @param aggressor  The player who caused the percentage change ({@code null} if not
	 *                   applicable).
	 */
	public void setPercentage(int percentage, HeroBattlePlayer aggressor)
	{
		setPercentage(percentage, aggressor, false);
	}

	/**
	 * Change the current percentage of the player if the last damage comes from a player
	 *
	 * @param percentage    The new percentage
	 * @param aggressor     The player who caused the percentage change ({@code null} if not
	 *                      applicable).
	 * @param thornsDamages {@code true} If true this damage comes from thorns damages and should
	 *                      not apply other thorns damages.
	 */
	public void setPercentage(int percentage, HeroBattlePlayer aggressor, Boolean thornsDamages)
	{
		if (isSpectator() || getPlayerClass() == null) return;

		int oldPercentage = this.percentage;

		if (percentage < 0) percentage = 0;

		final int percentageInflicted = (percentage - oldPercentage) * HeroBattle.get().getGame().getDamagesMultiplicator();

		new DamageTag(percentageInflicted, getPlayerIfOnline().getLocation()).play();

		if (!thornsDamages && getRemainingThorns() > 0)
		{
			aggressor.setPercentage((int) (aggressor.getPercentage() + percentageInflicted * getThornsEfficienty()), this, true);
		}

		if (getRemainingReducingIncomingDamages() != 0 && percentage >= oldPercentage)
		{
			percentage -= (percentageInflicted) / 2;
		}

		this.percentage = percentage;

		if (aggressor != null)
		{
			aggressor.addPercentageInflicted(percentageInflicted);


			Assist assist = assists.get(aggressor.getUUID());
			if (assist == null)
			{
				assist = new Assist(percentageInflicted);
				assists.put(aggressor.getUUID(), assist);
			}
			else
			{
				assist.addAssist(percentageInflicted);
			}
		}


		Player player = getPlayerIfOnline();

		if (getPercentage() >= getPlayerClass().getMaxResistance())
		{
			if (aggressor != null) setLastDamager(aggressor.getUUID());

			HeroBattle.get().getGame().onPlayerDeath(getUUID(), DeathType.KO);

			if (player != null)
			{
				player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_LARGE, 10);

				player.setLevel(0);
			}
		}

		else
		{
			if (player != null)
			{

				player.setLevel(0);
				player.setTotalExperience(0);

				player.setLevel(getPercentage());
				player.setExp(((float) getPercentage()) / ((float) getPlayerClass().getMaxResistance()));

				HeroBattle.get().getGame().updatePlayerArmor(player);
			}
		}

		HeroBattle.get().getScoreboardManager().update(this);
	}

	public int getTotalLives()
	{
		return lives + additionalLives;
	}

	public int getLives()
	{
		return lives;
	}

	public void setLives(int lives)
	{
		this.lives = lives;
	}

	public int getAdditionalLives()
	{
		return additionalLives;
	}

	public void setAdditionalLives(int additionalLives)
	{
		this.additionalLives = additionalLives;
	}

	/**
	 * Adds a new life to the player. Player's hearts changing animation handled.
	 */
	public void gainLife()
	{
		Player player = getPlayerIfOnline();
		Validate.notNull(player, "Bukkit Player object null in HeroBattlePlayer.gainLife ?! (UUID " + getUUID() + ")");

		if (getLives() < getPlayerClass().getLives())
		{
			lives++;
		}
		else
		{
			additionalLives++;
			player.setMaxHealth(player.getMaxHealth() + 2);
		}

		player.setHealth(player.getHealth() + 2);
	}

	/**
	 * Removes a life to the player. Player's hearts changing animation handled.
	 */
	public void looseLife()
	{
		final Player player = getPlayerIfOnline();
		Validate.notNull(player, "Bukkit Player object null in HeroBattlePlayer.looseLife ?! (UUID " + getUUID() + ")");

		if (additionalLives != 0)
		{
			additionalLives--;
			player.setMaxHealth(player.getMaxHealth() - 2);
			Bukkit.getScheduler().runTaskLater(HeroBattle.get(), () -> player.setHealth(player.getMaxHealth()), 2l);
		}
		else
		{
			lives--;

			if (lives != 0)
			{
				player.setHealth(player.getHealth() - 2);
			}
			else
			{
				updateEffectsTask.cancel();
				ActionBar.removeMessage(getUUID());
			}
		}
	}

	public int getRemainingDoubleDamages()
	{
		return remainingDoubleDamages;
	}

	public void addRemainingDoubleDamages(int remainingDoubleDamages)
	{
		this.remainingDoubleDamages += remainingDoubleDamages;
		updateActionBar();
	}

	public int getRemainingInvisibility()
	{
		return remainingInvisibility;
	}

	public void addRemainingInvisibility(int remainingInvisibility)
	{
		this.remainingInvisibility += remainingInvisibility;

		HeroBattle.get().getGame().updatePlayerArmor(getPlayerIfOnline());
		updateActionBar();
	}

	public int getRemainingReducingIncomingDamages()
	{
		return remainingReducedIncomingDamages;
	}

	public void addRemainingReducedIncomingDamages(int remainingReducedIncomingDamages)
	{
		this.remainingReducedIncomingDamages += remainingReducedIncomingDamages;
		updateActionBar();
	}

	public int getRemainingThorns()
	{
		return remainingThorns;
	}

	public float getThornsEfficienty()
	{
		return thornsEfficienty;
	}

	public void addRemainingThorns(int remainingThorns, float thornsEfficienty)
	{
		this.remainingThorns = this.getRemainingThorns() + remainingThorns;
		this.thornsEfficienty = Math.max(thornsEfficienty, 0);

		updateActionBar();
	}

	public UUID getLastDamager()
	{
		return lastDamager;
	}

	public void setLastDamager(UUID lastDamager)
	{
		this.lastDamager = lastDamager;
	}

	public Map<UUID, Assist> getAssists()
	{
		return assists;
	}

	public void resetAssists()
	{
		assists = new HashMap<>();
	}

	public PlayerClass getPlayerClass()
	{
		return classe;
	}

	/**
	 * Sets the player class Updates the TAB list & the player's action bar
	 *
	 * @param classe The class.
	 */
	public void setPlayerClass(PlayerClass classe)
	{
		this.classe = classe;
		if (classe != null)
		{
			lives = classe.getLives();

			// Reset of the multiplier, only if the game is not started
			// (else, the class was effectively chosen randomly).
			if (HeroBattle.get().getGame().getStatus() != Status.IN_GAME)
			{
				gainMultiplier = 1.0;
			}

			Player player = getPlayerIfOnline();
			if (player != null)
			{
				getPlayerClass().setClassTeam(player);
			}
		}

		else
		{
			lives = 3;
			gainMultiplier = 1.4;

			Player player = getPlayerIfOnline();
			if (player != null)
			{
				PlayerClass.setRandomClassTeam(player);
			}
		}

		if (classe != null)
		{
			ActionBar.sendPermanentMessage(getPlayerIfOnline(), ChatColor.GREEN + "Classe sélectionnée : " + ChatColor.DARK_GREEN + classe.getName());
		}
		else
		{
			ActionBar.sendPermanentMessage(getPlayerIfOnline(), ChatColor.GREEN + "Classe sélectionnée : " + ChatColor.DARK_GREEN + "aléatoire");
		}
	}

	public List<Task> getTasks()
	{
		return tasks;
	}

	/**
	 * The player makes a big jump. Can only be used two times in the air. Gets reseted by touching
	 * the ground.
	 */
	public void doubleJump()
	{

		if (jumpLocked) return; // nop


		Player player = getPlayerIfOnline();

		if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR)
		{
			setJumps(maxJumps);
		}

		if (getJumps() > 0)
		{
			// The jump is locked when the velocity is applied, to avoid the player to
			// do dozens of jumps by spam-right-clicking or double-jumping.
			// The jumps count left is not reset directly but ten ticks after, to avoid it being
			// overwritten by the PlayerMoveEvent checking if the player is on the ground.

			final int futureJumps = getJumps() - 1;
			jumpLocked = true;

			Bukkit.getScheduler().runTaskLater(HeroBattle.get(), () -> {
				if (getJumps() == futureJumps + 1)
				{
					setJumps(futureJumps);
					jumpLocked = false;
				}
			}, 6l);

			// The velocity is applied
			Vector direction = player.getLocation().getDirection().multiply(0.5);
			Vector vector = new Vector(direction.getX(), 0.9, direction.getZ());
			player.setVelocity(vector);

			// An effect is displayed
			ParticleEffect.CLOUD.display((float) (1 - Math.random() * 2), (float) Math.random(), (float) (1 - Math.random() * 2), 0.1F, 5, player.getLocation(), 1);
		}
	}

	public boolean playTask(Task t)
	{
		boolean min = false;
		for (int i = 0; i < tasks.size(); i++)
		{
			if (tasks.get(i).getClass() == t.getClass())
			{
				tasks.get(i).playTask();
				tasks.remove(i);
				min = true;
			}
		}
		return min;
	}

	public void addTask(Task t)
	{
		tasks.add(t);
	}

	public boolean hasTask(Task t)
	{
		for (Task task : tasks)
		{
			if (task.getClass() == t.getClass())
			{
				return true;
			}
		}
		return false;
	}

	public List<PlayerClass> getAvaibleClasses()
	{
		return classesAvailable;
	}

	public void setAvailableClasses(List<PlayerClass> available)
	{
		this.classesAvailable = available;
	}

	public void addAvaibleClass(PlayerClass theClass)
	{
		this.classesAvailable.add(theClass);
	}

	public int getOriginalElo()
	{
		return originalElo;
	}

	public void setOriginalElo(int originalElo)
	{
		this.originalElo = originalElo;
	}

	public int getElo()
	{
		return elo;
	}

	public void setElo(int elo)
	{
		this.elo = elo;
	}

	public long getPercentageInflicted()
	{
		return percentageInflicted;
	}

	public void addPercentageInflicted(long percentageInflicted)
	{
		this.percentageInflicted += percentageInflicted;
	}

	public int getPlayersKilled()
	{
		return playersKilled;
	}

	public void addPlayersKilled()
	{
		this.playersKilled++;
	}

	public boolean isDeathHandled()
	{
		return deathHandled;
	}

	public void setDeathHandled(boolean deathHandled)
	{
		this.deathHandled = deathHandled;
	}

	public int getRemainingRespawnInvincibility()
	{
		return remainingRespawnInvincibility;
	}

	public void setRespawning()
	{
		this.remainingRespawnInvincibility = 2;
		updateActionBar();
	}

	public void setJumpLocked(boolean jumpLocked)
	{
		this.jumpLocked = jumpLocked;
	}


	@Override
	public void addCoins(final int amount, final String why)
	{
		super.addCoins((int) Math.ceil(((double) amount) * gainMultiplier), why);
	}

	public void addStars(int amount, String why)
	{
		super.addStars((int) Math.ceil(((double) amount) * gainMultiplier), why);
	}

	public int getKillsRank()
	{
		return killsRank;
	}

	public void setKillsRank(int killsRank)
	{
		this.killsRank = killsRank;
	}

	public int getPercentageRank()
	{
		return percentageRank;
	}

	public void setPercentageRank(int percentageRank)
	{
		this.percentageRank = percentageRank;
	}


	private void updateActionBar()
	{

		if (!(HeroBattle.get().getGame().getStatus() == Status.IN_GAME))
			return;


		Player player = getPlayerIfOnline();
		if (player == null || !player.isOnline()) return;


		List<String> currentStatus = new ArrayList<>();


		if (remainingTimeWithMoreJumps != 0)
		{
			if (getMaxJumps() == 3)
				currentStatus.add(ChatColor.RED + "Triple sauts (" + remainingTimeWithMoreJumps + ")");
			else
				currentStatus.add(ChatColor.RED + "Sauts : " + getMaxJumps() + "× (" + remainingTimeWithMoreJumps + ")");
		}

		if (remainingDoubleDamages != 0)
		{
			currentStatus.add(ChatColor.DARK_GREEN + "Double dommages (" + remainingDoubleDamages + ")");
		}

		if (remainingInvisibility != 0)
		{
			currentStatus.add(ChatColor.GRAY + "Invisible (" + remainingInvisibility + ")");
		}

		if (remainingRespawnInvincibility != 0)
		{
			currentStatus.add(ChatColor.LIGHT_PURPLE + "Invulnérable (" + remainingRespawnInvincibility + ")");
		}

		if (remainingReducedIncomingDamages != 0)
		{
			currentStatus.add(ChatColor.LIGHT_PURPLE + "Dommages reçus réduits (" + remainingReducedIncomingDamages + ")");
		}

		if (getRemainingThorns() != 0)
		{
			currentStatus.add(ChatColor.DARK_PURPLE + "Renvoi de dégâts (" + ((int) (getThornsEfficienty() * 100)) + "%) (" + getRemainingThorns() + ")");
		}

		for (PotionEffect effect : player.getActivePotionEffects())
		{
			int duration = (int) Math.rint(((double) effect.getDuration()) / 20d);
			PotionEffectType type = effect.getType();


			if (type.equals(PotionEffectType.BLINDNESS))
			{
				currentStatus.add(ChatColor.DARK_GRAY + "Cécité (" + duration + ")");
			}

			else if (type.equals(PotionEffectType.CONFUSION))
			{
				currentStatus.add(ChatColor.YELLOW + "Nausée (" + duration + ")");
			}

			else if (type.equals(PotionEffectType.NIGHT_VISION))
			{
				currentStatus.add(ChatColor.DARK_BLUE + "Nyctalopie (" + duration + ")");
			}

			else if (type.equals(PotionEffectType.POISON))
			{
				currentStatus.add(ChatColor.YELLOW + "Poison (" + duration + ")");
			}

			else if (type.equals(PotionEffectType.SPEED))
			{
				currentStatus.add(ChatColor.AQUA + "Vitesse (" + duration + ")");
			}

			else if (type.equals(PotionEffectType.SLOW))
			{
				currentStatus.add(ChatColor.DARK_AQUA + "Lenteur (" + duration + ")");
			}
		}

		if (currentStatus.size() == 0)
		{
			ActionBar.removeMessage(player, true);
		}
		else
		{
			ActionBar.sendPermanentMessage(player, StringUtils.join(currentStatus, ChatColor.DARK_GRAY + " - " + ChatColor.RESET));
		}
	}

	/**
	 * Resets the percentage, should only be used for respawn (prevents damage tags to be
	 * displayed)
	 */
	public void resetPercentage()
	{
		this.percentage = 0;
	}

	/**
	 * Global method for player damaging. Adds percentage and creates a knockback effect.
	 *
	 * @param percentageAdded The percentage added.
	 * @param aggressor       The aggressor. {@code null} if non applicable.
	 * @param origin          The origin of the knockback.
	 */
	public void damage(int percentageAdded, HeroBattlePlayer aggressor, Location origin)
	{
		Player player = getPlayerIfOnline();

		player.damage(0);

		final float reducer = 15.0F;

		Vector v = player.getVelocity().add(player.getLocation().toVector().subtract(origin.toVector()).normalize().multiply(percentage / reducer));
		v.setY(0.5);
		player.setVelocity(v);

		if (aggressor == null)
		{
			setPercentage(percentageAdded + percentage);
		}
		else
		{
			setPercentage(percentageAdded + percentage, aggressor);
		}
	}

	/**
	 * Global method for player damaging. Adds a random percentage between two values and creates a
	 * knockback effect.
	 *
	 * @param percentageMin The minimal percentage added.
	 * @param percentageMax The maximal percentage added.
	 * @param aggressor     The aggressor. {@code null} if non applicable.
	 * @param origin        The origin of the knockback.
	 */
	public void damage(int percentageMin, int percentageMax, HeroBattlePlayer aggressor, Location origin)
	{
		damage(Utils.randomNumber(percentageMin, percentageMax), aggressor, origin);
	}

	/**
	 * Basic damaging method. Adds percentage to player. Does not knockbacks.
	 *
	 * @param percentageAdded The percentage added to this player.
	 * @param aggressor       The aggressor. {@code null} if non applicable.
	 */
	public void basicDamage(int percentageAdded, HeroBattlePlayer aggressor)
	{
		getPlayerIfOnline().damage(0);

		if (aggressor == null)
		{
			setPercentage(percentageAdded + percentage);
		}
		else
		{
			setPercentage(percentageAdded + percentage, aggressor);
		}
	}


	/**
	 * TODO Implementation in the SG API.
	 *
	 * @return
	 */
	public OfflinePlayer getOfflinePlayer()
	{
		return Bukkit.getOfflinePlayer(getUUID());
	}
}
