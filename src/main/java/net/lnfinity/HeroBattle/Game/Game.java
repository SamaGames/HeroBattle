package net.lnfinity.HeroBattle.Game;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Class.PlayerClass;
import net.lnfinity.HeroBattle.Tools.PlayerTool;
import net.lnfinity.HeroBattle.Utils.Utils;
import net.lnfinity.HeroBattle.Utils.WinnerFirework;
import net.md_5.bungee.api.ChatColor;
import net.samagames.gameapi.GameAPI;
import net.samagames.gameapi.json.Status;
import net.samagames.gameapi.types.GameArena;
import net.samagames.utils.Titles;
import net.zyuiop.MasterBundle.MasterBundle;
import net.zyuiop.MasterBundle.StarsManager;
import net.zyuiop.coinsManager.CoinsManager;
import net.zyuiop.statsapi.StatsApi;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Game implements GameArena {

	private HeroBattle p;
	private Status status = Status.Idle;

	private List<Location> spawnPoints = new LinkedList<>();
	private Location hub;

	private Double bottomHeight = 0.0;

	public Game(HeroBattle plugin) {
		p = plugin;

		// Loads the spawn points and the hub from the world config.

		try {
			hub = Utils.stringToLocation(p, p.getArenaConfig().getString("map.hub"));
		} catch (IllegalArgumentException e) {
			p.getLogger().log(Level.SEVERE, "Invalid hub in arena.yml! " + e.getMessage());
		}

		for (Object spawn : p.getArenaConfig().getList("map.spawns")) {
			if (spawn instanceof String) {
				try {
					spawnPoints.add(Utils.stringToLocation(p, (String) spawn));
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

		bottomHeight = p.getArenaConfig().getDouble("map.bottom", 0d);
	}

	public void start() {
		p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Que le meilleur gagne !");
		teleportPlayers();

		p.getScoreboardManager().init();
		p.getPowerupManager().getSpawner().startTimer();

		for (Player player : p.getServer().getOnlinePlayers()) {
			Titles.sendTitle(player, 2, 38, 6, ChatColor.AQUA + "C'est parti !", "");
		}

		setStatus(Status.InGame);
	}

	public void teleportPlayers() {
		List<Location> tempLocs = new LinkedList<>(spawnPoints);
		Random rand = new Random();

		for (Player player : p.getServer().getOnlinePlayers()) {
			GamePlayer hbPlayer = p.getGamePlayer(player);
			if (hbPlayer.getPlayerClass() == null) {
				chooseRandomClass(player);
			}

			int index = rand.nextInt(tempLocs.size());
			player.teleport(tempLocs.get(index));
			tempLocs.remove(index);

			player.getInventory().clear();
			player.setLevel(0);

			player.getInventory().setHelmet(hbPlayer.getPlayerClass().getHat());
			ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			LeatherArmorMeta meta = (LeatherArmorMeta) chest.getItemMeta();
			meta.setColor(Color.fromRGB(255, 255, 255));
			meta.spigot().setUnbreakable(true);
			chest.setItemMeta(meta);
			player.getInventory().setChestplate(chest);
			ItemStack leg = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			leg.setItemMeta(meta);
			player.getInventory().setLeggings(leg);
			ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
			boots.setItemMeta(meta);
			player.getInventory().setBoots(boots);

			int i = 0;
			for (PlayerTool tool : hbPlayer.getPlayerClass().getTools()) {
				player.getInventory().setItem(i, tool.generateCompleteItem());
				i++;
			}

			player.getInventory().setHeldItemSlot(0);
			player.updateInventory();

			player.setGameMode(GameMode.ADVENTURE);
			player.setMaxHealth(hbPlayer.getPlayerClass().getLives() * 2);
			player.setHealth(hbPlayer.getPlayerClass().getLives() * 2d);
		}
	}

	public void teleportHub(UUID id) {
		p.getServer().getPlayer(id).teleport(hub);
	}

	public void teleportRandomSpot(UUID id) {
		teleportRandomSpot(p.getServer().getPlayer(id));
	}

	public void teleportRandomSpot(Player player) {
		player.teleport(spawnPoints.get((new Random()).nextInt(spawnPoints.size())));
	}

	public void spawnPlayer(Player player) {
		GamePlayer hbPlayer = p.getGamePlayer(player);

		hbPlayer.setPercentage(0);
		player.setExp(0);
		player.setLevel(0);
		player.setTotalExperience(0);

		player.setHealth(hbPlayer.getLives() * 2);

		teleportRandomSpot(player);
	}

	public void enableSpectatorMode(Player player) {
		GamePlayer hbPlayer = p.getGamePlayer(player);

		hbPlayer.setPlaying(false);

		player.setGameMode(GameMode.SPECTATOR);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);

		teleportHub(player.getUniqueId());
	}

	public void chooseRandomClass(Player player) {
		Random rnd = new Random();
		int r = rnd.nextInt(p.getClassManager().getAvailableClasses().size());
		int i = 0;
		for (PlayerClass classe : p.getClassManager().getAvailableClasses()) {
			if (i == r) {
				p.getGamePlayer(player).setPlayerClass(classe);
				player.sendMessage(ChatColor.GREEN + "La classe "
						+ ChatColor.DARK_GREEN + classe.getName() + ChatColor.GREEN + " vous a été attribuée suite à un complexe jeu de dés !");
				return;
			}
			i++;
		}
	}

	public void onPlayerDeath(UUID id) {
		if (getStatus() != Status.InGame) {
			teleportHub(id);
			return;
		}

		final Player player = p.getServer().getPlayer(id);
		final GamePlayer hbPlayer = p.getGamePlayer(player);

		// Technical stuff
		hbPlayer.setLives(hbPlayer.getLives() - 1);


		// Broadcasts
		String lives = ChatColor.DARK_GRAY + " (" + ChatColor.RED + hbPlayer.getLives() + ChatColor.DARK_GRAY + " vies)";
		if (hbPlayer.getLastDamager() == null) {
			p.getServer().broadcastMessage(
					HeroBattle.GAME_TAG + ChatColor.YELLOW + player.getName() + ChatColor.YELLOW
							+ " est tombé dans le vide" + lives);
		} else {
			p.getServer().broadcastMessage(
					HeroBattle.GAME_TAG + ChatColor.YELLOW + player.getName() + ChatColor.YELLOW + " a été poussé par "
							+ p.getServer().getPlayer(hbPlayer.getLastDamager()).getName() + lives);

			StatsApi.increaseStat(hbPlayer.getLastDamager(), p.getName(), "kills", 1);
		}


		// Effects on the player
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
		p.getServer().getScheduler().runTaskLater(p, new Runnable() {
			@Override
			public void run() {
				player.playSound(player.getLocation(), Sound.IRONGOLEM_DEATH, 1, 1);
			}
		}, 5);


		// Death message
		if(hbPlayer.getLives() >= 1) {
			
			Titles.sendTitle(player, 3, 150, 10,  Utils.heartsToString(hbPlayer, true), ChatColor.RED + "Vous perdez une vie !");
			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					Titles.sendTitle(player, 15, 50, 8,  Utils.heartsToString(hbPlayer), ChatColor.RED + "Vous perdez une vie !");
				}
			}, 10L);

		} else {
			Titles.sendTitle(player, 3, 150, 0, Utils.heartsToString(hbPlayer, true), ChatColor.RED + "Vous êtes mort !");
			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					Titles.sendTitle(player, 15, 100, 18, Utils.heartsToString(hbPlayer), ChatColor.RED + "Vous êtes mort !");
				}
			}, 10L);
		}


		// Respawn
		if (hbPlayer.getLives() >= 1) {
			spawnPlayer(player);
		}
		else {
			enableSpectatorMode(player);

			String s = "s";
			if (p.getPlayingPlayerCount() == 1) s = "";

			p.getServer().broadcastMessage(
					HeroBattle.GAME_TAG + ChatColor.YELLOW + player.getName() + ChatColor.YELLOW + " a perdu ! "
							+ ChatColor.DARK_GRAY + "[" + ChatColor.RED + p.getPlayingPlayerCount()
							+ ChatColor.DARK_GRAY + " joueur" + s + " restant" + s + ChatColor.DARK_GRAY + "]");

			if (p.getPlayingPlayerCount() == 1) {
				for (Player pl : p.getServer().getOnlinePlayers()) {
					if (p.getGamePlayer(pl.getUniqueId()).isPlaying()) {
						onPlayerWin(pl.getUniqueId());
						return;
					}
				}
			}
		}


		// Scoreboard update
		p.getScoreboardManager().refresh();


		// Stats
		StatsApi.increaseStat(player, p.getName(), "deaths", 1);
	}

	public void onPlayerQuit(UUID id) {
		GamePlayer gPlayer = p.getGamePlayer(id);
		if(gPlayer.isPlaying()) {
			gPlayer.setPlaying(false);

			String s = "s";
			if (p.getPlayingPlayerCount() == 1) s = "";

			p.getServer().broadcastMessage(
					HeroBattle.GAME_TAG + ChatColor.YELLOW + p.getServer().getPlayer(id).getDisplayName() + ChatColor.YELLOW
							+ " a perdu ! " + ChatColor.DARK_GRAY + "[" + ChatColor.RED + (p.getPlayingPlayerCount() - 1)
							+ ChatColor.DARK_GRAY + " joueur" + s + " restant" + s + ChatColor.DARK_GRAY + "]");


			p.getScoreboardManager().refresh();
		}

		if (p.getPlayingPlayerCount() == 1) {
			for (GamePlayer pl : p.getGamePlayers().values()) {
				if (pl.isPlaying()) {
					onPlayerWin(pl.getPlayerUniqueID());
					return;
				}
			}
		}
	}

	public void onPlayerWin(UUID id) {

		p.getScoreboardManager().refresh();

		p.getPowerupManager().getSpawner().stopTimer();
		Player player = p.getServer().getPlayer(id);
		player.getInventory().clear();
		GamePlayer HBplayer = p.getGamePlayer(player);
		HBplayer.setPlaying(false);

		p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + player.getDisplayName() + " remporte la partie !");
		new WinnerFirework(p, 30, player);

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
		return status;
	}

	@Override
	public void setStatus(Status status) {
		this.status = status;
		GameAPI.getManager().sendArena();
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
		return p.getArenaConfig().getInt("map.waiting", 120);
	}

	/**
	 * Below this height, the players are dead.
	 */
	public Double getBottomHeight() {
		return bottomHeight;
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
