package net.lnfinity.HeroBattle.Utils;

import net.samagames.gameapi.json.Status;
import net.samagames.utils.Titles;
import org.bukkit.Bukkit;
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

		p.getGame().setStatus(Status.Available);
	}

	public void restartTimer() {
		p.getServer().getScheduler().cancelTask(task);
		isEnabled = true;

		p.getGame().setStatus(Status.Starting);

		seconds = p.getGame().getCountdownTime();
		task = p.getServer().getScheduler().runTaskTimer(p, new Runnable() {
			public void run() {
				// Timer reduction if players count is high
				int playersCount = p.getGame().countGamePlayers();
				boolean changed = false;

				// Half-full
				if (playersCount == Math.max(p.getGame().getMinPlayers(), p.getGame().getTotalMaxPlayers() / 2) && seconds > 60) {
					seconds = 60;
					changed = true;
				}
				// Full
				else if (playersCount == p.getGame().getTotalMaxPlayers() && seconds > 15) {
					seconds = 15;
					changed = true;
				}

				// Message if counter changed
				if (changed) {
					p.getServer().broadcastMessage(
							HeroBattle.NAME + ChatColor.YELLOW + "Il y a désormais "
									+ ChatColor.RED + playersCount
									+ ChatColor.YELLOW + " joueurs en jeu : le compteur a été raccourci à "
									+ ChatColor.RED + seconds + ChatColor.YELLOW + " secondes");
				}


				// Counter display (chat + title)
				if (seconds == 120 || seconds == 60 || seconds == 30 || seconds == 15 || seconds == 10 || seconds <= 5 && seconds != 0) {

					// Messages
					if (seconds == 1) {
						p.getServer().broadcastMessage(
								HeroBattle.NAME + ChatColor.YELLOW + "Le jeu commence dans "
										+ ChatColor.RED + seconds + ChatColor.YELLOW + " seconde");
					} else {
						p.getServer().broadcastMessage(
								HeroBattle.NAME + ChatColor.YELLOW + "Le jeu commence dans "
										+ ChatColor.RED + seconds + ChatColor.YELLOW + " secondes");
					}

					// Sound
					if (seconds <= 10) {
						for (Player player : p.getServer().getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
						}
					}

					// Title
					ChatColor color;
					if (seconds >= 30) {
						color = ChatColor.GREEN;
					} else if (seconds >= 4) {
						color = ChatColor.YELLOW;
					} else {
						color = ChatColor.RED;
					}

					for (Player player : p.getServer().getOnlinePlayers()) {
						Titles.sendTitle(player, 2, 16, 2, color + "" + seconds, "");
					}
				}

				// Seconds in XP
				for (Player player : p.getServer().getOnlinePlayers()) {
					player.setLevel(seconds);
				}

				// Start
				if (seconds < 1) {
					p.getServer().getScheduler().cancelTask(task);

					p.getGame().start();

					Bukkit.getScheduler().runTaskLater(p, new Runnable() {
						@Override
						public void run() {
							for (Player player : p.getServer().getOnlinePlayers()) {
								player.playSound(player.getLocation(), Sound.GHAST_DEATH, 1, 1);
							}
						}
					}, 1L);
				}

				seconds--;
			}
		}, 30L, 20L).getTaskId();
	}

}