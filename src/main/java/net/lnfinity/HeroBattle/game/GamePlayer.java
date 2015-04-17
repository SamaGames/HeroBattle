package net.lnfinity.HeroBattle.game;

import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.tasks.Task;
import org.bukkit.Bukkit;
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
	
	private int originalElo = 0;
	private int Elo = 0;

	private PlayerClass classe = null;
	private int jumps = 2;
	private int maxJumps = 2;
	private int percentage = 0;
	private int lives = 3;
	private boolean playing = true;
	private boolean doubleDamages = false;
	private boolean isInvisible = false;
	private boolean isInvulnerable = false;
	private UUID lastDamager = null;
	private List<PlayerClass> avaible = new ArrayList<PlayerClass>();
	private List<Task> tasks = new ArrayList<Task>();

	private BukkitTask checkIsOnGroundTask = null;

	private long percentageInflicted = 0l;
	private int playersKilled = 0;

	public GamePlayer(UUID id) {
		playerID = id;
		playerName = Bukkit.getServer().getPlayer(id).getName();
	}

	public int getJumps() {
		return jumps;
	}

	public void setJumps(int jumps) {
		this.jumps = jumps;
	}
	
	public int getMaxJumps() {
		return maxJumps;
	}

	public void setMaxJumps(int maxJumps) {
		this.maxJumps = maxJumps;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		setPercentage(percentage, null);
	}

	public void setPercentage(int percentage, GamePlayer aggressor) {
		if(isInvulnerable() && percentage >= this.percentage) return;

		this.percentage = percentage;

		if(aggressor != null) aggressor.addPercentageInflicted(percentage);
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean bool) {
		playing = bool;
	}

	public boolean hasDoubleDamages() {
		return doubleDamages;
	}

	public void setDoubleDamages(boolean doubleDamages) {
		this.doubleDamages = doubleDamages;
	}

	public boolean isInvisible() {
		return isInvisible;
	}

	public void setInvisible(boolean isInvisible) {
		this.isInvisible = isInvisible;
	}

	public boolean isInvulnerable() {
		return isInvulnerable;
	}

	public void setInvulnerable(boolean isInvulnerable) {
		this.isInvulnerable = isInvulnerable;
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
		} else {
			lives = 3;
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
		Player player = Bukkit.getServer().getPlayer(playerID);
		if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
			setJumps(maxJumps);
		}

		if (getJumps() > 0) {
			setJumps(getJumps() - 1);
			Vector direction = player.getLocation().getDirection().multiply(0.5);
			Vector vector = new Vector(direction.getX(), 0.85, direction.getZ());
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
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getClass() == t.getClass()) {
				return true;
			}
		}
		return false;
	}

	public List<PlayerClass> getAvaibleClasses() {
		return avaible;
	}

	public void setAvaibleClasses(List<PlayerClass> avaible) {
		this.avaible = avaible;
	}
	
	public void addAvaibleClass(PlayerClass theClass) {
		this.avaible.add(theClass);
	}
	
	public int getOriginalElo() {
		return originalElo;
	}

	public void setOriginalElo(int originalElo) {
		this.originalElo = originalElo;
	}

	public int getElo() {
		return Elo;
	}

	public void setElo(int elo) {
		Elo = elo;
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
}
