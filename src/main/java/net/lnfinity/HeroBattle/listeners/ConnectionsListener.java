package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.DeathType;
import net.lnfinity.HeroBattle.utils.ActionBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


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
