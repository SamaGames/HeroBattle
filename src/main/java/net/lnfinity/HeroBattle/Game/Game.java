package net.lnfinity.HeroBattle.Game;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Class.PlayerClass;
import net.lnfinity.HeroBattle.Tools.PlayerTool;
import net.lnfinity.HeroBattle.Utils.WinnerFirework;
import net.md_5.bungee.api.ChatColor;
import net.samagames.gameapi.GameAPI;
import net.samagames.gameapi.json.Status;
import net.samagames.gameapi.types.GameArena;
import net.zyuiop.MasterBundle.MasterBundle;
import net.zyuiop.MasterBundle.StarsManager;
import net.zyuiop.coinsManager.CoinsManager;
import net.zyuiop.statsapi.StatsApi;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Game implements GameArena {

	private HeroBattle p;
	private boolean waiting = true;

	private List<Location> spawnPoints = new LinkedList<>();
	private Location hub;

	public Game(HeroBattle plugin) {
		p = plugin;

		// Loads the spawn points and the hub from the world config.

		try {
			hub = stringToLocation(p.getArenaConfig().getString("map.hub"));
		} catch (IllegalArgumentException e) {
			p.getLogger().log(Level.SEVERE, "Invalid hub in arena.yml! " + e.getMessage());
		}

		for (Object spawn : p.getArenaConfig().getList("map.spawns")) {
			if (spawn instanceof String) {
				try {
					spawnPoints.add(stringToLocation((String) spawn));
				} catch (IllegalArgumentException e) {
					p.getLogger().log(Level.SEVERE, "Invalid spawn in arena.yml! " + e.getMessage());
				}
			}
		}

		if (spawnPoints.size() < getTotalMaxPlayers()) {
			p.getLogger().severe("#==================[Fatal exception report]==================#");
			p.getLogger().severe("# Not enough spawn points set in the configuration.          #");
			p.getLogger().severe("# The plugin cannot load, please fix that.                   #");
			p.getLogger().severe("#============================================================#");

			p.getServer().getPluginManager().disablePlugin(p);
		}
	}

	public void start() {
		p.getServer().broadcastMessage(HeroBattle.NAME + ChatColor.GREEN + "Que le meilleur gagne !");
		p.getScoreboardManager().init();
		teleportPlayers();

		GameAPI.getManager().sendArena();
	}

	public void teleportPlayers() {
		List<Location> tempLocs = new LinkedList<>(spawnPoints);
		Random rand = new Random();

		for (Player player : p.getServer().getOnlinePlayers()) {
			//p.addGamePlayer(player);

			int index = rand.nextInt(tempLocs.size());
			player.teleport(tempLocs.get(index));
			tempLocs.remove(index);

			player.getInventory().clear();

			GamePlayer hbPlayer = p.getGamePlayer(player);

			if (hbPlayer.getPlayerClass() == null) {
				chooseRandomClass(player);
			}

			int i = 0;
			for (PlayerTool tool : hbPlayer.getPlayerClass().getTools()) {
				player.getInventory().setItem(i, tool.generateCompleteItem());
				i++;
			}

			player.setGameMode(GameMode.ADVENTURE);
			player.setMaxHealth(hbPlayer.getPlayerClass().getLives() * 2);
			player.setHealth(hbPlayer.getPlayerClass().getLives() * 2d);
		}
		waiting = false;
	}

	public void teleportHub(UUID id) {
		p.getServer().getPlayer(id).teleport(hub);
	}

	public void teleportRandomSpot(UUID id) {
		p.getServer().getPlayer(id).teleport(spawnPoints.get((new Random()).nextInt(spawnPoints.size())));
	}

	public void chooseRandomClass(Player player) {
		Random rnd = new Random();
		int r = rnd.nextInt(p.getClassManager().getAvailableClasses().size());
		int i = 0;
		for (PlayerClass classe : p.getClassManager().getAvailableClasses()) {
			if (i == r) {
				p.getGamePlayer(player).setPlayerClass(classe);
				player.sendMessage(ChatColor.GREEN + "Vous n'avez pas choisi de classe, la classe "
						+ ChatColor.DARK_GREEN + classe.getName() + ChatColor.GREEN + " vous a été attribuée !");
				return;
			}
			i++;
		}
	}

	public void onPlayerDeath(UUID id) {
		if (waiting) {
			teleportHub(id);
			return;
		}
		Player player = (Player) p.getServer().getPlayer(id);
		GamePlayer HBplayer = p.getGamePlayer(player);
		Damageable d = (Damageable) player;
		HBplayer.setPercentage(0);
		player.setExp(0);
		player.setLevel(0);
		player.setTotalExperience(0);
		String lives = ChatColor.DARK_GRAY + " (" + ChatColor.RED + ((int) HBplayer.getLives() - 1) + ChatColor.DARK_GRAY + " vies)"; 
		if (HBplayer.getLastDamager() == null) {
			p.getServer().broadcastMessage(
					HeroBattle.NAME + ChatColor.YELLOW + player.getName() + ChatColor.YELLOW
							+ " est tombé dans le vide" + lives);
		} else {
			p.getServer().broadcastMessage(
					HeroBattle.NAME + ChatColor.YELLOW + player.getName() + ChatColor.YELLOW + " a été poussé par "
							+ p.getServer().getPlayer(HBplayer.getLastDamager()).getName() + lives);
		}
		
		if (d.getHealth() > 2) {
			HBplayer.setLives(HBplayer.getLives() - 1);
			player.setHealth(HBplayer.getLives() * 2);
			teleportRandomSpot(player.getUniqueId());
		} else {
			player.setGameMode(GameMode.SPECTATOR);
			HBplayer.setPlaying(false);
			teleportHub(player.getUniqueId());
			player.getInventory().clear();
			String s = "s";
			if (p.getPlayingPlayerCount() == 1) {
				s = "";
			}
			p.getServer().broadcastMessage(
					HeroBattle.NAME + ChatColor.YELLOW + player.getName() + ChatColor.YELLOW + " a perdu ! "
							+ ChatColor.DARK_GRAY + "[" + ChatColor.RED + p.getPlayingPlayerCount()
							+ ChatColor.DARK_GRAY + " joueur" + s + " restant" + s + ChatColor.DARK_GRAY + "]");
			StatsApi.increaseStat(player, p.getName(), "deaths", 1);
			if (p.getPlayingPlayerCount() == 1) {
				for (Player pl : p.getServer().getOnlinePlayers()) {
					if (p.getGamePlayer(pl.getUniqueId()).isPlaying()) {
						onPlayerWin(pl.getUniqueId());
						return;
					}
				}
			}
		}
	}

	public void onPlayerQuit(UUID id) {
		String s = "s";
		if (p.getPlayingPlayerCount() == 2) {
			s = "";
		}
		p.getServer().broadcastMessage(
				HeroBattle.NAME + ChatColor.YELLOW + p.getServer().getOfflinePlayer(id).getName() + ChatColor.YELLOW
						+ " a perdu ! " + ChatColor.DARK_GRAY + "[" + ChatColor.RED + ((int) p.getPlayingPlayerCount() - 1)
						+ ChatColor.DARK_GRAY + " joueur" + s + " restant" + s + ChatColor.DARK_GRAY + "]");
		p.getGamePlayer(id).setPlaying(false);
		if (p.getPlayingPlayerCount() == 1) {
			for (Player pl : p.getServer().getOnlinePlayers()) {
				if (p.getGamePlayer(pl.getUniqueId()).isPlaying()) {
					onPlayerWin(pl.getUniqueId());
					return;
				}
			}
		}
	}

	public void onPlayerWin(UUID id) {
		Player player = p.getServer().getPlayer(id);
		GamePlayer HBplayer = p.getGamePlayer(player);
		HBplayer.setPlaying(false);
		p.getServer().broadcastMessage(
				HeroBattle.NAME + ChatColor.GREEN + player.getDisplayName() + " remporte la partie !");
		new WinnerFirework(p, 30, player);
		p.getServer().getScheduler().runTaskLater(p, new Runnable() {
			public void run() {
				// p.getServer().shutdown();
				// ^- c'est pour quoi ça déjà ?
			}
		}, 300L);

		StarsManager.creditJoueur(player, 1, "Victoire !");
		CoinsManager.creditJoueur(player.getUniqueId(), 5, true, true, "Victoire !");
		StatsApi.increaseStat(player, p.getName(), "wins", 1);

		if (MasterBundle.isDbEnabled) {
			Bukkit.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					for (Player player : p.getServer().getOnlinePlayers()) {
						player.kickPlayer("");
					}
				}
			}, 25 * 20L);

			Bukkit.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					Bukkit.shutdown();
				}
			}, 30 * 20L);
		}
	}

	public boolean isWaiting() {
		return waiting;
	}

	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}

	/**
	 * Converts a string (in the config file) to a Location object.
	 * 
	 * @param locationInConfig
	 *            A string; format "x;y;z" or "x;y;z;yaw" or "x;y;z;yaw;pitch".
	 * @return The Location object, for the main world (first one).
	 * 
	 * @throws IllegalArgumentException
	 *             if the format is not good.
	 */
	private Location stringToLocation(String locationInConfig) {
		String[] coords = locationInConfig.split(";");
		if (coords.length < 3) {
			throw new IllegalArgumentException("Invalid location: " + locationInConfig);
		}

		try {
			Location location = new Location(p.getServer().getWorlds().get(0), Double.valueOf(coords[0]),
					Double.valueOf(coords[1]), Double.valueOf(coords[2]));

			if (coords.length >= 4) {
				location.setYaw(Float.valueOf(coords[3]));

				if (coords.length >= 5) {
					location.setPitch(Float.valueOf(coords[4]));
				}
			}

			return location;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid location (NaN!): " + locationInConfig);
		}
	}

	@Override
	public int countGamePlayers() {
		int count = 0;
		for (Player player : p.getServer().getOnlinePlayers()) {
			if (player.getGameMode() != GameMode.SPECTATOR)
				count++;
		}
		return count;
	}

	@Override
	public int getMaxPlayers() {
		return p.getArenaConfig().getInt("map.maxPlayers");
	}

	@Override
	public int getTotalMaxPlayers() {
		return getMaxPlayers() + getVIPSlots();
	}

	@Override
	public int getVIPSlots() {
		return p.getArenaConfig().getInt("map.maxVIP");
	}

	@Override
	public Status getStatus() {
		if (isWaiting()) {
			return Status.Available;
		} else {
			return Status.InGame;
		}
	}

	@Override
	public void setStatus(Status status) {

	}

	@Override
	public String getMapName() {
		return p.getArenaConfig().getString("map.name");
	}

	@Override
	public boolean hasPlayer(UUID uuid) {
		Player player = p.getServer().getPlayer(uuid);
		return player != null && player.getGameMode() != GameMode.SPECTATOR;
	}

	public int getMinPlayers() {
		return p.getArenaConfig().getInt("map.minPlayers");
	}

	public int getCountdownTime() {
		return p.getArenaConfig().getInt("map.waiting");
	}

	public Block getTargetBlock(Player player, int maxRange) {
		Block block;
		Location loc = player.getEyeLocation().clone();
		Vector progress = loc.getDirection().normalize().clone().multiply(0.70);
		maxRange = (100 * maxRange / 70);
		int loop = 0;
		while (loop < maxRange) {
			loop++;
			loc.add(progress);
			block = loc.getBlock();
			if (!block.getType().equals(Material.AIR)) {
				return loc.getBlock();
			}
		}
		return null;
	}
}
