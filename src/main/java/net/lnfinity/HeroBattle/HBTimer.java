package net.lnfinity.HeroBattle;

import net.md_5.bungee.api.ChatColor;


public class HBTimer {

	private HeroBattle p;
	private int seconds = 10;
	private int task = -1;

	public HBTimer(HeroBattle plugin) {
		p = plugin;
	}

	public void cancelTimer() {
		p.getServer().getScheduler().cancelTask(task);
	}

	public void restartTimer() {
		p.getServer().getScheduler().cancelTask(task);
		seconds = 10;
		task = p.getServer().getScheduler().runTaskTimer(p, new Runnable() {
			public void run() {
				if(seconds == 30) {
					p.getServer().broadcastMessage(
					p.NAME + ChatColor.YELLOW + "Le jeu commence dans " + ChatColor.RED + seconds + ChatColor.YELLOW + " secondes.");
				} else if(seconds == 15) {
					p.getServer().broadcastMessage(
					p.NAME + ChatColor.YELLOW + "Le jeu commence dans " + ChatColor.RED + seconds + ChatColor.YELLOW + " secondes.");
				} else if(seconds <= 5 && seconds != 1) {
					p.getServer().broadcastMessage(
					p.NAME + ChatColor.YELLOW + "Le jeu commence dans " + ChatColor.RED + seconds + ChatColor.YELLOW + " secondes.");
				} else if(seconds == 1) {
					p.getServer().broadcastMessage(
					p.NAME + ChatColor.YELLOW + "Le jeu commence dans " + ChatColor.RED + seconds + ChatColor.YELLOW + " seconde.");
				}
				if (seconds <= 1) {
					p.getServer().getScheduler().cancelTask(task);
					p.getListener().setWaiting(false);
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