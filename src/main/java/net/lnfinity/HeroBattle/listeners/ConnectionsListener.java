package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class ConnectionsListener implements Listener {

	private HeroBattle plugin;

	public ConnectionsListener(HeroBattle p) {
		plugin = p;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent ev) {

		/*
		if (plugin.getGame().getStatus() == Status.Available || plugin.getGame().getStatus() == Status.Starting) {
			if (ev.getPlayer().getName().equals("6infinity8") || ev.getPlayer().getName().equals("AmauryPi")) {
				plugin.getServer().broadcastMessage(
						HeroBattle.GAME_TAG
								+ ChatColor.RED + ChatColor.MAGIC + "|||"
								+ ChatColor.GREEN + ChatColor.BOLD + " " + ev.getPlayer().getName() + " "
								+ ChatColor.RED + ChatColor.MAGIC + "|||"
								+ ChatColor.YELLOW + " a quitté la partie");
			} else {
				plugin.getServer().broadcastMessage(
						HeroBattle.GAME_TAG + ChatColor.YELLOW + ev.getPlayer().getName() + ChatColor.YELLOW
								+ " a quitté la partie");
			}
		}
		*/

		if (plugin.getGame().getStatus() == Status.Starting || plugin.getGame().getStatus() == Status.Available) {
			if (plugin.getTimer().isEnabled() && plugin.getPlayerCount() - 1 < plugin.getGame().getMinPlayers()) {
				plugin.getTimer().cancelTimer();
				plugin.getServer().broadcastMessage(
						HeroBattle.GAME_TAG + ChatColor.YELLOW
								+ "Il n'y a plus assez de joueurs pour commencer la partie !");
			}
		}

		else if (plugin.getGame().getStatus() == Status.InGame) {
			if (plugin.getPlayerCount() == 0 && MasterBundle.isDbEnabled) {
				plugin.getGame().setStatus(Status.Stopping);
				Bukkit.shutdown();
			} else {
				plugin.getGame().onPlayerQuit(ev.getPlayer().getUniqueId());
			}
		}
		
		for (Player player : plugin.getServer().getOnlinePlayers()) {
				player.showPlayer(ev.getPlayer());
				ev.getPlayer().showPlayer(player);
		}

		ActionBar.removeMessage(ev.getPlayer());

		plugin.getScoreboardManager().removePlayer(ev.getPlayer());
		
		GameAPI.getManager().sendArena();
	}
}
