package net.lnfinity.HeroBattle.Utils;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;

public class CountdownTimer {

	private HeroBattle p;
	private int seconds = 30;
	private int task = -1;
	private boolean isEnabled = false;

	public CountdownTimer(HeroBattle plugin) {
		p = plugin;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}

	public void cancelTimer() {
		p.getServer().getScheduler().cancelTask(task);
		isEnabled = false;
		p.getServer().broadcastMessage(HeroBattle.NAME + ChatColor.YELLOW + "Il n'y a plus assez de joueurs pour commencer la partie !");
	}

	public void restartTimer() {
		p.getServer().getScheduler().cancelTask(task);
		isEnabled = true;
		seconds = p.getGame().getCountdownTime();
		task = p.getServer().getScheduler().runTaskTimer(p, new Runnable() {
			public void run() {
				if (seconds == 120 || seconds == 60 || seconds == 30 || seconds == 15 || seconds == 10 || seconds <= 5 && seconds != 1) {
					p.getServer().broadcastMessage(
							HeroBattle.NAME + ChatColor.YELLOW + "Le jeu commence dans " + ChatColor.RED + seconds
									+ ChatColor.YELLOW + " secondes");
				} else if (seconds == 1) {
					p.getServer().broadcastMessage(
							HeroBattle.NAME + ChatColor.YELLOW + "Le jeu commence dans " + ChatColor.RED + seconds
									+ ChatColor.YELLOW + " seconde");
				}
				if (seconds <= 1) {
					p.getServer().getScheduler().cancelTask(task);
					p.getGame().setWaiting(false);
					p.getServer().getScheduler().runTaskLater(p, new Runnable() {
						@Override
						public void run() {
							p.getGame().start();
						}
					}, 20L);

				}
				seconds--;
			}
		}, 30L, 20L).getTaskId();
	}

}