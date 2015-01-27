package net.lnfinity.HeroBattle;

import java.util.Arrays;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

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
			player.getInventory().setItem(1, item);

			item = new ItemStack(Material.IRON_SWORD, 1);
			meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.RESET + "" + ChatColor.RED
					+ "Épée repoussante");
			meta.setLore(Arrays.asList(ChatColor.GRAY
					+ "Frappez les joueurs pour les repousser"));
			meta.spigot().setUnbreakable(true);
			item.setItemMeta(meta);
			player.getInventory().setItem(0, item);
			player.setGameMode(GameMode.ADVENTURE);
			player.setMaxHealth(6);
			player.setHealth(6);

			loc++;
		}
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		 
		Objective objective = board.registerNewObjective("percentage", "dummy");
		objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
		objective.setDisplayName("%");
		 
		for(Player online : Bukkit.getOnlinePlayers()){
		  Score score = objective.getScore(online);
		  score.setScore(p.getHBPlayer(online.getUniqueId()).getPercentage());
		}
		 
		for(Player online : Bukkit.getOnlinePlayers()){
		online.setScoreboard(board);
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
			player.setGameMode(GameMode.SPECTATOR);
			HBplayer.setPlaying(false);
			teleportHub(player.getUniqueId());
			p.getServer().broadcastMessage(
					HeroBattle.NAME + ChatColor.YELLOW
							+ player.getDisplayName() + ChatColor.YELLOW
							+ " a perdu ! " + ChatColor.DARK_GRAY + "["
							+ ChatColor.RED + p.getPlayingPlayerCount()
							+ ChatColor.DARK_GRAY + " joueurs restants"
							+ ChatColor.DARK_GRAY + "]");
		}

	}

	public void start() {
		teleportPlayers();
	}
}
