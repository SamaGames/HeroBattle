package net.lnfinity.HeroBattle.powerups;

import net.lnfinity.HeroBattle.HeroBattle;

import java.util.Random;

public class PowerupSpawner {

	private HeroBattle p;

	private boolean isEnabled = false;
	private int task = -1;

	Random random = new Random();

	public PowerupSpawner(HeroBattle plugin) {
		p = plugin;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public void stopTimer() {
		p.getServer().getScheduler().cancelTask(task);
		isEnabled = false;
	}
	
	public void startTimer() {
		if(isEnabled) {
			return;
		}
		
		task = p.getServer().getScheduler().runTaskTimer(p, () -> {
			if(random.nextInt(PowerupManager.INVERSE_PROBABILITY_OF_SPAWN_PER_TICK) == 0) {
				p.getPowerupManager().spawnRandomPowerup();
			}
		}, 1L, 1L).getTaskId();
	}
}
