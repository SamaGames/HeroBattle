package net.lnfinity.HeroBattle.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.lnfinity.HeroBattle.Class.PlayerClass;
import net.lnfinity.HeroBattle.Tasks.Task;

public class GamePlayer {

	private PlayerClass classe = null;
	private int doubleJump = 2;
	private int percentage = 0;
	private int lives = 3;
	private boolean playing = true;
	private boolean doubleDamages = false;
	private boolean deathCooldown = false;
	private UUID lastDamager = null;
	private List<Task> tasks = new ArrayList<Task>();

	public GamePlayer() {

	}

	public int getDoubleJump() {
		return doubleJump;
	}

	public void setDoubleJump(int doubleJump) {
		this.doubleJump = doubleJump;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
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
	}

	public List<Task> getTasks() {
		return tasks;
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
}
