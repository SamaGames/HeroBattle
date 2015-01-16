package net.lnfinity.HeroBattle;

import java.util.Arrays;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HBGame {

	private HeroBattle p;

	public HBGame(HeroBattle plugin) {
		p = plugin;
		teleportPlayers();
	}

	public void teleportPlayers() {
		int loc = 1;
		for (Player player : p.getServer().getOnlinePlayers()) {
			p.addHBPlayer(player.getUniqueId());
			player.teleport(new Location(player.getWorld(), p.getConfig()
					.getInt("locations.point" + loc + ".x"), p.getConfig()
					.getInt("locations.point" + loc + ".y"), p.getConfig()
					.getInt("locations.point" + loc + ".z")));
			p.getLogger().info("ok");
			ItemStack item = new ItemStack(Material.SUGAR, 1);
			ItemMeta meta = item.getItemMeta();
			p.getLogger().info("ok1");
			meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BLUE
					+ "Boost vitesse");
			meta.setLore(Arrays.asList(ChatColor.GRAY
					+ "Clic droit pour utiliser"));
			item.setItemMeta(meta);
			player.getInventory().setItem(2, item);

			item = new ItemStack(Material.RABBIT_FOOT, 1);
			meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN
					+ "Super Saut");
			meta.setLore(Arrays.asList(ChatColor.GRAY
					+ "Clic droit pour utiliser"));
			item.setItemMeta(meta);
			player.getInventory().setItem(1, item);

			loc++;
		}

	}
}
