package net.lnfinity.HeroBattle;

public class HBPlayer {

	private int doubleJump = 2;
	private int percentage = 0;
	private int speedCooldown = 20;

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

	public int getSpeedCooldown() {
		return speedCooldown;
	}

	public void setSpeedCooldown(int speedCooldown) {
		this.speedCooldown = speedCooldown;
	}

}
