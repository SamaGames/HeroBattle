package net.lnfinity.HeroBattle;

import java.util.Arrays;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HBGame {

	private HeroBattle p;

	public HBGame(HeroBattle plugin) {
		p = plugin;
	}

	public void teleportPlayers() {
		int loc = 1;
		for (Player player : p.getServer().getOnlinePlayers()) {
			p.addHBPlayer(player.getUniqueId());
			player.teleport(new Location(player.getWorld(), p.getConfig()
					.getInt("locations.point" + loc + ".x"), p.getConfig()
					.getInt("locations.point" + loc + ".y"), p.getConfig()
					.getInt("locations.point" + loc + ".z")));
			ItemStack item = new ItemStack(Material.SUGAR, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BLUE
					+ "Boost vitesse");
			meta.setLore(Arrays.asList(ChatColor.GRAY
					+ "Clic droit pour utiliser"));
			item.setItemMeta(meta);
			GlowEffect.addGlow(item);
			player.getInventory().setItem(2, item);

			item = new ItemStack(Material.RABBIT_FOOT, 1);
			meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN
					+ "Super Saut");
			meta.setLore(Arrays.asList(ChatColor.GRAY
					+ "Clic droit pour utiliser"));
			item.setItemMeta(meta);
			player.getInventory().setItem(1, item);
			player.setMaxHealth(6);

			loc++;
		}
	}

	public void teleportHub(UUID id) {
		Player player = p.getServer().getPlayer(id);
		player.teleport(new Location(player.getWorld(), p.getConfig().getInt(
				"locations.hub.x"), p.getConfig().getInt("locations.hub.y"), p
				.getConfig().getInt("locations.hub.z")));
	}

	public void onPlayerKill(UUID id) {
		Player player = p.getServer().getPlayer(id);
		HBPlayer HBplayer = p.getHBPlayer(id);
		Damageable d = (Damageable) player;
		if (d.getHealth() > 2) {
			HBplayer.setLives(HBplayer.getLives() - 1);
			player.setHealth(HBplayer.getLives() * 2);
			int loc = 1;
			player.teleport(new Location(player.getWorld(), p.getConfig()
					.getInt("locations.point" + loc + ".x"), p.getConfig()
					.getInt("locations.point" + loc + ".y"), p.getConfig()
					.getInt("locations.point" + loc + ".z")));
		} else {
			p.getServer().broadcastMessage(
					HeroBattle.NAME + ChatColor.YELLOW
							+ player.getDisplayName() + ChatColor.YELLOW
							+ " a perdu ! " + ChatColor.DARK_GRAY + "["
							+ ChatColor.RED + p.getPlayerCount()
							+ ChatColor.DARK_GRAY + " joueurs restants"
							+ ChatColor.DARK_GRAY + "]");
			player.setGameMode(GameMode.SPECTATOR);
			teleportHub(player.getUniqueId());
		}

	}

	public void start() {
		teleportPlayers();
	}
}
