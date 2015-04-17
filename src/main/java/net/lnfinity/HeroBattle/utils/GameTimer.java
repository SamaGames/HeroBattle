package net.lnfinity.HeroBattle.utils;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;

public class GameTimer {

	private HeroBattle p;
	private final int maxMinutes;
	private int minutes = 0;
	private int seconds = 0;
	private int task = -1;
	private boolean isEnabled = false;

	public GameTimer(HeroBattle plugin, int max) {
		p = plugin;
		maxMinutes = max;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void startTimer() {
		if (isEnabled) {
			return;
		}
		isEnabled = true;
		task = p.getServer().getScheduler().runTaskTimer(p, new Runnable() {
			@Override
			public void run() {
				seconds++;
				if(seconds >= 60) {
					seconds = 0;
					minutes++;
				}
				if(minutes >= maxMinutes) {
					pauseTimer();
				}
				if(minutes == maxMinutes - 1 && seconds == 0) {
					p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.YELLOW + "La partie se termine dans 1 minute !");
				}
				if(minutes == maxMinutes - 1 && (seconds  == 30 || seconds == 50)) {
					p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.YELLOW + "La partie se termine dans " + (60 - seconds) + " secondes !");
				}
				if(minutes == maxMinutes) {
					p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.RED + "" + ChatColor.BOLD + "La partie se termine car le délai maximal est dépassé !");
					p.getGame().onPlayerWin(null);
				}
				p.getScoreboardManager().updateTimer();
			}
		}, 20L, 20L).getTaskId();
	}

	public void pauseTimer() {
		p.getServer().getScheduler().cancelTask(task);
		isEnabled = false;
	}
	
	public String getFormattedTime() {
		return getFormattedNumber(minutes) + ":" + getFormattedNumber(seconds);
	}
	
	private String getFormattedNumber(int n) {
		String str = n + "";
		if(str.length() == 1) {
			return "0" + str;
		}
		return str;
	}

}
