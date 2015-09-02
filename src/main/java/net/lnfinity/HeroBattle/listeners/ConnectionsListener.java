package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;


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
		if (!plugin.getGame().isGameStarted())
		{
			if (plugin.getTimer().isEnabled() && plugin.getGame().getConnectedPlayers() - 1 < plugin.getGame().getMinPlayers())
			{
				plugin.getTimer().cancelTimer();
				plugin.getServer().broadcastMessage(
						HeroBattle.GAME_TAG + ChatColor.YELLOW
								+ "Il n'y a plus assez de joueurs pour commencer la partie !");
			}
		}
		else
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
