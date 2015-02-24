package net.lnfinity.HeroBattle.Powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Utils.Utils;

public class PowerupSpawner {

	HeroBattle p;
	private boolean isEnabled = false;
	private int task = -1;

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
		
		task = p.getServer().getScheduler().runTaskTimer(p, new Runnable() {
			@Override
			public void run() {
				if(Utils.randomNumber(0, 2000) == 0) {
					p.getPowerupManager().spawnPowerup();
				}
			}
		}, 1L, 1L).getTaskId();
	}
}