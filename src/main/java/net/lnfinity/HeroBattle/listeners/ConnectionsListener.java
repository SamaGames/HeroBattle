package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.DeathType;
import net.lnfinity.HeroBattle.utils.ActionBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

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
public class ConnectionsListener implements Listener
{

	private final HeroBattle plugin;

	public ConnectionsListener(final HeroBattle p)
	{
		plugin = p;
	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent ev)
	{
		if (plugin.getGame().isGameStarted())
		{
			if (plugin.getGame().getInGamePlayers().size() == 0 && !plugin.isTestServer())
			{
				plugin.getGame().onPlayerWin(null);
			}
			else
			{
				plugin.getGame().onPlayerDeath(ev.getPlayer().getUniqueId(), DeathType.QUIT);
			}
		}

		for (final Player player : plugin.getServer().getOnlinePlayers())
		{
			player.showPlayer(ev.getPlayer());
			ev.getPlayer().showPlayer(player);
		}

		ActionBar.removeMessage(ev.getPlayer());

		plugin.getScoreboardManager().removePlayer(ev.getPlayer());
	}
}
