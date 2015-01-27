package net.lnfinity.HeroBattle;

public class HBPlayer {

	private int doubleJump = 2;
	private int percentage = 0;
	private int lives = 3;
	private boolean playing = true;

	public HBPlayer() {

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

}
