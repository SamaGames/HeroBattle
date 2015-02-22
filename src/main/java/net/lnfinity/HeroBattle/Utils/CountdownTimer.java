package net.lnfinity.HeroBattle.Utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

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
	}

	public void restartTimer() {
		p.getServer().getScheduler().cancelTask(task);
		isEnabled = true;
		seconds = p.getGame().getCountdownTime();
		task = p.getServer().getScheduler().runTaskTimer(p, new Runnable() {
			public void run() {
				if (seconds == 120 || seconds == 60 || seconds == 30 || seconds == 15 || seconds == 10 || seconds <= 5
						&& seconds != 1) {
					p.getServer().broadcastMessage(
							HeroBattle.NAME + ChatColor.YELLOW + "Le jeu commence dans " + ChatColor.RED + seconds
									+ ChatColor.YELLOW + " secondes");
					for (Player player : p.getServer().getOnlinePlayers()) {
						player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
					}
				} else if (seconds == 1) {
					p.getServer().broadcastMessage(
							HeroBattle.NAME + ChatColor.YELLOW + "Le jeu commence dans " + ChatColor.RED + seconds
									+ ChatColor.YELLOW + " seconde");
					for (Player player : p.getServer().getOnlinePlayers()) {
						player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
					}
				}
				if (seconds <= 1) {
					p.getServer().getScheduler().cancelTask(task);
					p.getGame().setWaiting(false);
					p.getServer().getScheduler().runTaskLater(p, new Runnable() {
						@Override
						public void run() {
							for (Player player : p.getServer().getOnlinePlayers()) {
								player.playSound(player.getLocation(), Sound.GHAST_DEATH, 1, 1);
							}
							p.getGame().start();
						}
					}, 20L);

				}
				seconds--;
			}
		}, 30L, 20L).getTaskId();
	}

}