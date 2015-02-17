package net.lnfinity.HeroBattle.Listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;
import net.samagames.gameapi.GameAPI;
import net.samagames.gameapi.events.FinishJoinPlayerEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MasterListener implements Listener {

	private HeroBattle plugin;

	public MasterListener(HeroBattle p) {
		plugin = p;
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(FinishJoinPlayerEvent ev) {
		Player p = plugin.getServer().getPlayer(ev.getPlayer());
		plugin.addGamePlayer(p);
		p.getInventory().clear();
		p.setExp(0);
		p.setLevel(0);
		p.setTotalExperience(0);
		p.setGameMode(GameMode.ADVENTURE);

		p.teleport(
				new Location(p.getWorld(), plugin.getConfig().getInt("locations.hub.x"), plugin
						.getConfig().getInt("locations.hub.y"), plugin.getConfig().getInt("locations.hub.z")));
		p.getServer().broadcastMessage(HeroBattle.NAME + ChatColor.YELLOW + p.getDisplayName() + ChatColor.YELLOW
				+ " a rejoint l'arène " + ChatColor.DARK_GRAY + "[" + ChatColor.RED + plugin.getPlayerCount()
				+ ChatColor.DARK_GRAY + "/" + ChatColor.RED + "4" + ChatColor.DARK_GRAY + "]");

		if (plugin.getPlayerCount() == 4) {
			plugin.getTimer().restartTimer();
		}

		p.setScoreboard(plugin.getScoreboardManager().getScoreboard());

		p.setMaxHealth(20);
		p.setHealth(20);

		// TODO Better display.
		p.getInventory().addItem(new ItemStack(Material.NETHER_STAR));

		p.updateInventory();

		GameAPI.getManager().sendArena();
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent ev) {
		if (plugin.getGame().isWaiting()) {
			plugin.getTimer().cancelTimer();
		}

		GameAPI.getManager().sendArena();
	}
}
