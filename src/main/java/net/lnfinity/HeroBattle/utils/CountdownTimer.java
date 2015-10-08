package net.lnfinity.HeroBattle.utils;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;
import net.samagames.api.games.Status;
import net.samagames.tools.Titles;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class CountdownTimer
{

	private final Integer COUNTER_REDUCTION_WHEN_HALF_FULL;
	private final Integer COUNTER_REDUCTION_WHEN_FULL;
	private HeroBattle p;
	private int seconds = 30;
	private int task = -1;
	private boolean isEnabled = false;

	public CountdownTimer(HeroBattle plugin)
	{
		p = plugin;

		COUNTER_REDUCTION_WHEN_HALF_FULL = 30; // temp
		COUNTER_REDUCTION_WHEN_FULL = 10;
	}

	public boolean isEnabled()
	{
		return isEnabled;
	}

	public void cancelTimer()
	{
		p.getServer().getScheduler().cancelTask(task);
		isEnabled = false;

		p.getGame().setStatus(Status.WAITING_FOR_PLAYERS);

		for (Player player : p.getServer().getOnlinePlayers())
		{
			player.setLevel(0);
		}
	}

	public void restartTimer()
	{
		p.getServer().getScheduler().cancelTask(task);
		isEnabled = true;

		p.getGame().setStatus(Status.READY_TO_START);

		seconds = p.getGame().getCountdownTime();
		task = p.getServer().getScheduler().runTaskTimer(p, () -> {
			// Timer reduction if players count is high
			int playersCount = p.getGame().getConnectedPlayers();
			boolean changed = false;

			// Half-full
			if (playersCount == Math.max(p.getGame().getMinPlayers(), p.getGame().getTotalMaxPlayers() / 2)
					&& seconds > COUNTER_REDUCTION_WHEN_HALF_FULL)
			{
				seconds = COUNTER_REDUCTION_WHEN_HALF_FULL;
				changed = true;
			}
			// Full
			else if (playersCount == p.getGame().getMaxPlayers() && seconds > COUNTER_REDUCTION_WHEN_FULL)
			{
				seconds = COUNTER_REDUCTION_WHEN_FULL;
				changed = true;
			}

			// Message if counter changed
			if (changed)
			{
				p.getServer().broadcastMessage(
						HeroBattle.GAME_TAG + ChatColor.YELLOW + "Il y a désormais " + ChatColor.RED + playersCount
								+ ChatColor.YELLOW + " joueurs en jeu."
				);
				p.getServer().broadcastMessage(
						HeroBattle.GAME_TAG + ChatColor.YELLOW + "Le compteur a été raccourci à "
								+ ChatColor.RED + seconds + ChatColor.YELLOW + " secondes."
				);
			}

			// Counter display (chat + title)
			if (seconds == 120 || seconds == 60 || seconds == 30 || seconds == 15 || seconds == 10 || seconds <= 5
					&& seconds != 0)
			{

				// Messages
				if (!changed)
				{
					if (seconds == 1)
					{
						p.getServer().broadcastMessage(
								HeroBattle.GAME_TAG + ChatColor.YELLOW + "Le jeu commence dans " + ChatColor.RED
										+ seconds + ChatColor.YELLOW + " seconde");
					}
					else
					{
						p.getServer().broadcastMessage(
								HeroBattle.GAME_TAG + ChatColor.YELLOW + "Le jeu commence dans " + ChatColor.RED
										+ seconds + ChatColor.YELLOW + " secondes");
					}
				}

				// Sound
				if (seconds <= 10)
				{
					for (Player player : p.getServer().getOnlinePlayers())
					{
						player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
					}
				}

				// Title
				ChatColor color;
				if (seconds >= 30)
				{
					color = ChatColor.GREEN;
				}
				else if (seconds >= 4)
				{
					color = ChatColor.YELLOW;
				}
				else
				{
					color = ChatColor.RED;
				}

				p.getServer().getOnlinePlayers().stream().filter(player -> !p.getTutorialDisplayer().isWatchingTutorial(player.getUniqueId())).forEach(player -> {
					Titles.sendTitle(player, 2, 16, 2, color + "" + seconds, "");
				});
			}

			// Seconds in XP
			for (Player player : p.getServer().getOnlinePlayers())
			{
				player.setLevel(seconds);
			}

			// Start
			if (seconds < 1)
			{
				p.getServer().getScheduler().cancelTask(task);

				p.getGame().startGame();

				Bukkit.getScheduler().runTaskLater(p, () -> {
					for (Player player : p.getServer().getOnlinePlayers())
					{
						player.playSound(player.getLocation(), Sound.GHAST_DEATH, 1, 1);
					}
				}, 1L);
			}

			seconds--;
		}, 30L, 20L).getTaskId();
	}

	/**
	 * Returns the seconds left before the game.
	 */
	public int getSecondsLeft()
	{
		return seconds;
	}
}
