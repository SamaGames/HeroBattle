package net.lnfinity.HeroBattle.utils;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;
import net.samagames.tools.Titles;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/*
 * This file is part of HeroBattle.
 *
 * HeroBattle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HeroBattle is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HeroBattle.  If not, see <http://www.gnu.org/licenses/>.
 */
public class GameTimer
{
	private final int maxMinutes;
	private HeroBattle p;
	private int minutes = 0;
	private int seconds = 0;
	private int task = -1;
	private boolean isEnabled = false;


	public GameTimer(HeroBattle plugin, int max)
	{
		p = plugin;
		maxMinutes = max;
	}

	public boolean isEnabled()
	{
		return isEnabled;
	}

	public void startTimer()
	{
		if (isEnabled)
		{
			return;
		}
		isEnabled = true;
		task = p.getServer().getScheduler().runTaskTimer(p, () -> {
			seconds++;
			if (seconds >= 60)
			{
				seconds = 0;
				minutes++;
			}
			if (minutes >= maxMinutes)
			{
				pauseTimer();
			}

			if (minutes == 10 && seconds == 0)
			{
				p.getGame().setDamagesMultiplier(2);
				p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.YELLOW + "10 minutes de jeu se sont écoulées");
				p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.RED + "" + ChatColor.BOLD + "Tous les dégâts infligés sont multipliés par 2 !");
				for (Player player : p.getServer().getOnlinePlayers())
				{
					player.playSound(player.getLocation(), Sound.ZOMBIE_UNFECT, 1, 1);
					Titles.sendTitle(player, 5, 40, 5, ChatColor.RED + "Dégâts globaux " + ChatColor.BOLD + "x2", "");
				}
			}

			if (minutes == 12 && seconds == 30)
			{
				p.getGame().setDamagesMultiplier(3);
				p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.YELLOW + "12 minutes et 30 secondes de jeu se sont écoulées");
				p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.RED + "" + ChatColor.BOLD + "Tous les dégâts infligés sont multipliés par 3 !");
				for (Player player : p.getServer().getOnlinePlayers())
				{
					player.playSound(player.getLocation(), Sound.ZOMBIE_UNFECT, 1, 1);
					Titles.sendTitle(player, 5, 40, 5, ChatColor.RED + "Dégâts globaux " + ChatColor.BOLD + "x3", "");
				}
			}

			if (minutes == maxMinutes - 1 && seconds == 0)
			{
				p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.YELLOW + "La partie se termine dans 1 minute !");
			}
			if (minutes == maxMinutes - 1 && (seconds == 30 || seconds == 50))
			{
				p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.YELLOW + "La partie se termine dans " + (60 - seconds) + " secondes !");
			}
			if (minutes == maxMinutes)
			{
				p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.RED + "" + ChatColor.BOLD + "La partie se termine car le délai maximal est dépassé !");
				p.getGame().onPlayerWin(null);
			}
			p.getScoreboardManager().updateTimer();
		}, 20L, 20L).getTaskId();
	}

	public void pauseTimer()
	{
		p.getServer().getScheduler().cancelTask(task);
		isEnabled = false;
	}

	public String getFormattedTime()
	{
		return getFormattedNumber(minutes) + ":" + getFormattedNumber(seconds);
	}

	private String getFormattedNumber(int n)
	{
		String str = n + "";
		if (str.length() == 1)
		{
			return "0" + str;
		}
		return str;
	}
}
