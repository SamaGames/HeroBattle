package net.lnfinity.HeroBattle.Game;

import net.lnfinity.HeroBattle.Class.BruteClass;
import net.lnfinity.HeroBattle.Class.PlayerClass;

public class GamePlayer {

	private PlayerClass classe = new BruteClass();
	private int doubleJump = 2;
	private int percentage = 0;
	private int lives = classe.getLives();
	private boolean playing = true;
	private boolean doubleDamages = false;

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
	
	public PlayerClass getPlayerClass() {
		return classe;
	}
	
	public void setPlayerClass(PlayerClass classe) {
		this.classe = classe;
	}

}
