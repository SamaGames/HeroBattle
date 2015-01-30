package net.lnfinity.HeroBattle.Listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MasterListener implements Listener {

	private HeroBattle plugin;

	public MasterListener(HeroBattle p) {
		plugin = p;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent ev) {
		Player p = ev.getPlayer();
		plugin.addHBPlayer(p.getUniqueId());
		p.getInventory().clear();
		p.setExp(0);
		p.setLevel(0);
		p.setTotalExperience(0);
		p.setGameMode(GameMode.ADVENTURE);
		ev.getPlayer().teleport(
				new Location(ev.getPlayer().getWorld(), plugin.getConfig().getInt("locations.hub.x"), plugin
						.getConfig().getInt("locations.hub.y"), plugin.getConfig().getInt("locations.hub.z")));
		ev.setJoinMessage(HeroBattle.NAME + ChatColor.YELLOW + p.getDisplayName() + ChatColor.YELLOW
				+ " a rejoint l'ar�ne " + ChatColor.DARK_GRAY + "[" + ChatColor.RED + plugin.getPlayerCount()
				+ ChatColor.DARK_GRAY + "/" + ChatColor.RED + "4" + ChatColor.DARK_GRAY + "]");
		if (plugin.getPlayerCount() == 2) {
			plugin.getTimer().restartTimer();
		}
		plugin.getHBPlayer(p.getUniqueId()).setLives(6);
		p.setMaxHealth(6);
		p.setHealth(6);
		p.updateInventory();
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent ev) {
		if (plugin.getGame().isWaiting()) {
			plugin.getTimer().cancelTimer();
		}
		plugin.removeHBPlayer(ev.getPlayer().getUniqueId());
	}
}