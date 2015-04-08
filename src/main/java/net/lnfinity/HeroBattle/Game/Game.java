package net.lnfinity.HeroBattle.Game;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Class.PlayerClass;
import net.lnfinity.HeroBattle.Powerups.ActivePowerup;
import net.lnfinity.HeroBattle.Powerups.powerups.ToastPowerup;
import net.lnfinity.HeroBattle.Tools.PlayerTool;
import net.lnfinity.HeroBattle.Utils.Utils;
import net.lnfinity.HeroBattle.Utils.WinnerFirework;
import net.md_5.bungee.api.ChatColor;
import net.samagames.gameapi.GameAPI;
import net.samagames.gameapi.json.Status;
import net.samagames.gameapi.types.GameArena;
import net.samagames.utils.GlowEffect;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Game implements GameArena {

	private HeroBattle p;
	private Status status = Status.Idle;

	private List<Location> spawnPoints = new LinkedList<>();
	private Location hub;

	private ArrayList<Location> tutorialLocations = new ArrayList<Location>();

	private Random random = new Random();

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

		try {
			for (Object location : p.getArenaConfig().getList("map.tutorial")) {
				if (location instanceof String) {
					try {
						tutorialLocations.add(Utils.stringToLocation(p, (String) location));
					} catch (IllegalArgumentException e) {
						p.getLogger().log(Level.SEVERE, "Invalid tutorial locations in arena.yml! " + e.getMessage());
					}
				}
			}
			if (tutorialLocations != null) {
				if (tutorialLocations.size() != 4) {
					p.getLogger().warning("Not enough / too many tutorial locations in arena.yml, disabling tutorial.");
					tutorialLocations = null;
				}
			}
		} catch (Exception ex) {
			p.getLogger().warning("No tutorial locations set in arena.yml");
		}
	}

	public void start() {

		p.getTutorialDisplayer().stopForAll("Le jeu démarre...");

		p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.DARK_GREEN + "ELO" + ChatColor.GREEN + " de la partie " + ChatColor.DARK_GREEN + ((int) getTotalElo() / p.getGamePlayers().size()));
		
		p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Que le meilleur gagne !");
		teleportPlayers();

		p.getGameTimer().startTimer();
		p.getScoreboardManager().init();
		p.getPowerupManager().getSpawner().startTimer();

		for (Player player : p.getServer().getOnlinePlayers()) {
			Titles.sendTitle(player, 2, 38, 6, ChatColor.AQUA + "C'est parti !", "");
			if(MasterBundle.isDbEnabled) {
				StatsApi.increaseStat(player.getUniqueId(), HeroBattle.GAME_NAME_WHITE, "played", 1);
			}
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

			p.getGame().updatePlayerArmor(player);

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
		p.getGame().updatePlayerArmor(player);
		player.teleport(spawnPoints.get(random.nextInt(spawnPoints.size())));
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
		GamePlayer gamePlayer = p.getGamePlayer(player);
		Random rnd = new Random();
		int r = rnd.nextInt(gamePlayer.getAvaibleClasses().size());
		int i = 0;
		for (PlayerClass classe : gamePlayer.getAvaibleClasses()) {
			if (i == r) {
				p.getGamePlayer(player).setPlayerClass(classe);
				player.sendMessage(ChatColor.GREEN + "La classe " + ChatColor.DARK_GREEN + classe.getName()
						+ ChatColor.GREEN + " vous a été attribuée suite à un complexe jeu de dés !");
				return;
			}
			i++;
		}
	}

	public void onPlayerDeath(UUID id, DeathType death) {
		if (getStatus() != Status.InGame) {
			teleportHub(id);
			return;
		}

		final Player player = p.getServer().getPlayer(id);
		final GamePlayer hbPlayer = p.getGamePlayer(player);

		// Technical stuff
		hbPlayer.setLives(hbPlayer.getLives() - 1);

		// Broadcasts
		String lives = ChatColor.DARK_GRAY + " (" + ChatColor.RED + hbPlayer.getLives() + ChatColor.DARK_GRAY
				+ " vies restantes)";
		if (hbPlayer.getLastDamager() == null) {
			switch (death) {
			case FALL:
				p.getServer().broadcastMessage(
						HeroBattle.GAME_TAG + ChatColor.YELLOW + player.getName() + ChatColor.YELLOW
								+ " est tombé dans le vide" + lives);
				break;
			case QUIT:
				p.getServer().broadcastMessage(
						HeroBattle.GAME_TAG + ChatColor.YELLOW + player.getName() + ChatColor.YELLOW
								+ " a quitté la partie");
				break;
			case KO:
				p.getServer().broadcastMessage(
						HeroBattle.GAME_TAG + ChatColor.YELLOW + player.getName() + ChatColor.YELLOW + " est K.O. !"
								+ lives);
				break;
			}
		} else {
			switch (death) {
			case FALL:
				p.getServer().broadcastMessage(
						HeroBattle.GAME_TAG + ChatColor.YELLOW + player.getName() + ChatColor.YELLOW
								+ " a été poussé par " + p.getServer().getPlayer(hbPlayer.getLastDamager()).getName()
								+ lives);
				StatsApi.increaseStat(hbPlayer.getLastDamager(), p.getName(), "kills", 1);
				CoinsManager.creditJoueur(hbPlayer.getLastDamager(), 3, true, true, "Un joueur poussé !");
				break;
			case QUIT:
				p.getServer().broadcastMessage(
						HeroBattle.GAME_TAG + ChatColor.YELLOW + player.getName() + ChatColor.YELLOW
								+ " a quitté la partie");
				StatsApi.increaseStat(hbPlayer.getLastDamager(), p.getName(), "kills", 1);
				CoinsManager.creditJoueur(hbPlayer.getLastDamager(), 3, true, true, "Un froussard !");
				break;
			case KO:
				p.getServer().broadcastMessage(
						HeroBattle.GAME_TAG + ChatColor.YELLOW + player.getName() + ChatColor.YELLOW
								+ " a été mis K.O. par " + p.getServer().getPlayer(hbPlayer.getLastDamager()).getName()
								+ lives);
				StatsApi.increaseStat(hbPlayer.getLastDamager(), p.getName(), "kills", 1);
				CoinsManager.creditJoueur(hbPlayer.getLastDamager(), 3, true, true, "Un joueur K.O. !");
				break;
			}
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
		if (hbPlayer.getLives() >= 1) {

			Titles.sendTitle(player, 3, 150, 10, Utils.heartsToString(hbPlayer, true), ChatColor.RED
					+ "Vous perdez une vie !");
			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					Titles.sendTitle(player, 15, 50, 8, Utils.heartsToString(hbPlayer), ChatColor.RED
							+ "Vous perdez une vie !");
				}
			}, 10L);

		} else {
			Titles.sendTitle(player, 3, 150, 0, Utils.heartsToString(hbPlayer, true), ChatColor.RED
					+ "Vous êtes mort !");
			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					Titles.sendTitle(player, 15, 100, 18, Utils.heartsToString(hbPlayer), ChatColor.RED
							+ "Vous êtes mort !");
				}
			}, 10L);
		}

		// Respawn
		if (hbPlayer.getLives() >= 1) {
			spawnPlayer(player);
		} else {
			enableSpectatorMode(player);

			String s = "s";
			if (p.getPlayingPlayerCount() == 1)
				s = "";

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
		if (gPlayer.isPlaying()) {
			gPlayer.setPlaying(false);

			String s = "s";
			if (p.getPlayingPlayerCount() == 1)
				s = "";

			p.getServer().broadcastMessage(
					HeroBattle.GAME_TAG + ChatColor.YELLOW + p.getServer().getPlayer(id).getDisplayName()
							+ ChatColor.YELLOW + " a perdu ! " + ChatColor.DARK_GRAY + "[" + ChatColor.RED
							+ (p.getPlayingPlayerCount() - 1) + ChatColor.DARK_GRAY + " joueur" + s + " restant" + s
							+ ChatColor.DARK_GRAY + "]");

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
		this.setStatus(Status.Stopping);
		Player player = p.getServer().getPlayer(id);
		GamePlayer HBplayer = p.getGamePlayer(player);
		p.getGameTimer().pauseTimer();
		HBplayer.setPlaying(true);
		p.getScoreboardManager().refresh();

		p.getPowerupManager().getSpawner().stopTimer();
		player.getInventory().clear();
		
		HBplayer.setPlaying(false);

		p.getServer().broadcastMessage(
				HeroBattle.GAME_TAG + ChatColor.GREEN + player.getDisplayName() + ChatColor.GREEN + ChatColor.BOLD + " remporte la partie !");
		new WinnerFirework(p, 30, player);

		StarsManager.creditJoueur(player, 1, "Victoire !");
		CoinsManager.creditJoueur(player.getUniqueId(), 8, true, true, "Victoire !");
		StatsApi.increaseStat(player, p.getName(), "wins", 1);

		calculateElos(id);
		
		p.getScoreboardManager().refreshTab();
		
		if (MasterBundle.isDbEnabled) {
			for (final GamePlayer gamePlayer : p.getGamePlayers().values()) {
				int old = StatsApi.getPlayerStat(gamePlayer.getPlayerUniqueID(), HeroBattle.GAME_NAME_WHITE, "elo");
				StatsApi.increaseStat(gamePlayer.getPlayerUniqueID(), HeroBattle.GAME_NAME_WHITE, "elo", gamePlayer.getElo() - old);
			}
		}
		
		p.getServer().getScheduler().runTaskLater(p, new Runnable() {
			@Override
			public void run() {
				for(GamePlayer gamePlayer : p.getGamePlayers().values()) {
					Player player = p.getServer().getPlayer(gamePlayer.getPlayerUniqueID());
					if(player != null) {
						int change = gamePlayer.getElo() - gamePlayer.getOriginalElo();
						if(change >= 0) {
							player.sendMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Votre " + ChatColor.DARK_GREEN +  "ELO" + ChatColor.GREEN + " augmente de " + ChatColor.DARK_GREEN + change + ChatColor.GREEN + " (" + ChatColor.DARK_GREEN + gamePlayer.getElo() + ChatColor.GREEN + ")");
						} else if(change < 0) {
							player.sendMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Votre " + ChatColor.DARK_GREEN +  "ELO" + ChatColor.GREEN + " diminue de " + ChatColor.RED + Math.abs(change) + ChatColor.GREEN + " (" + ChatColor.DARK_GREEN + gamePlayer.getElo() + ChatColor.GREEN + ")");
						}
					}
				}	
			}
		}, 3 * 20l);
		
		// Analytics to help us improve the game
		if(!p.getConfig().getBoolean("block-analytics")) {
			try {
				URL u = new URL("http://lnfinity.net/tasks/herobattle-stats?v=1&s=" + MasterBundle.getServerName() + "&m=" + p.getGame().getMapName() + "&p=" + p.getGamePlayers().size() + "&d=" + p.getGameTimer().getFormattedTime() + "&w=" + player.getName() + "&we=" + HBplayer.getElo() + "&wc=" + HBplayer.getPlayerClass().getType().toString().toLowerCase());
				u.openStream();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
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
	
	public void calculateElos(UUID winner) {
		double total = getTotalElo();
		
		for(GamePlayer gamePlayer : p.getGamePlayers().values()) {
			double esp = (gamePlayer.getElo() / total); // Espérance de gain pour gamePlayer
			double k = 16;

			if(gamePlayer.getPlayerUniqueID() == winner) { // Le joueur a gagné
				int elo = gamePlayer.getElo();
				k = 20 * p.getGamePlayers().size() * ((total / p.getGamePlayers().size()) / elo);
				int elo1 = (int) (k * (1 - esp));
				double mult = 1;

				mult = 1 / Utils.logb((elo + 1000)/1000, 2);
				gamePlayer.setElo((int) (mult * elo1 + elo));
				if(gamePlayer.getElo() > 10000) {
					gamePlayer.setElo(10000);
				}
				
			} else { // Le joueur n'a pas gagné
				int elo = gamePlayer.getElo();
				k = 40 * elo / (total / p.getGamePlayers().size());
				int elo1 = (int) (k * - esp);
				double mult = 1;

				mult = Utils.logb((elo + 1000)/1000, 2);
				gamePlayer.setElo((int) (mult * elo1 + elo));
				if(gamePlayer.getElo() < 1000) {
					gamePlayer.setElo(1000);
				}
			}
		}
	}
	
	public int getTotalElo() {
		int total = 0; // Total
		for(GamePlayer gamePlayer : p.getGamePlayers().values()) {
			if(gamePlayer.getElo() < 1000) { // Pour les nouveaux joueurs
				gamePlayer.setElo(2000);
			}
			if(gamePlayer.getElo() > 10000) { // Ne devrait jamais arriver
				gamePlayer.setElo(10000);
			}
			total += gamePlayer.getElo();
		}
		return total;
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

	public void updatePlayerArmor(Player player) {
		if (p.getGamePlayer(player).isInvisible()) {
			player.getInventory().setArmorContents(null);
		} else {
			GamePlayer gamePlayer = p.getGamePlayer(player);
			player.getInventory().setHelmet(gamePlayer.getPlayerClass().getHat());
			ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			LeatherArmorMeta meta = (LeatherArmorMeta) chest.getItemMeta();
			int R = 470 - gamePlayer.getPercentage();
			int G = 255 - gamePlayer.getPercentage();
			int B = 255 - gamePlayer.getPercentage() * 2;
			if (R > 255) {
				R = 255;
			} else if (R < 0) {
				R = 0;
			}
			if (G > 255) {
				G = 255;
			} else if (G < 0) {
				G = 0;
			}
			if (B > 255) {
				B = 255;
			} else if (B < 0) {
				B = 0;
			}
			meta.setColor(Color.fromRGB(R, G, B));
			meta.spigot().setUnbreakable(true);
			chest.setItemMeta(meta);
			player.getInventory().setChestplate(chest);
			ItemStack leg = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			leg.setItemMeta(meta);
			player.getInventory().setLeggings(leg);
			ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
			boots.setItemMeta(meta);
			player.getInventory().setBoots(boots);
		}
	}

	public ArrayList<Location> getTutorialLocations() {
		return tutorialLocations;
	}
	
	public void equipPlayer(Player player) {

		player.getInventory().clear();

		// Class selector
		ItemStack classSelectorItem = new ItemStack(Material.NETHER_STAR);
		ItemMeta classSelectorItemMeta = classSelectorItem.getItemMeta();
			classSelectorItemMeta.setDisplayName(
					ChatColor.LIGHT_PURPLE + "Choisissez une " + ChatColor.DARK_PURPLE + "classe"
			);
			classSelectorItemMeta.setLore(Arrays.asList(
					ChatColor.GRAY + "Cliquez-droit pour choisir la classe",
					ChatColor.GRAY + "avec laquelle vous allez jouer."
			));
		classSelectorItem.setItemMeta(classSelectorItemMeta);
		player.getInventory().setItem(0, classSelectorItem);


		// Tutorial
		ItemStack tutorialItem = new ItemStack(Material.BOOK);
		ItemMeta meta = tutorialItem.getItemMeta();
			meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Voir le Tutoriel");
			meta.setLore(Arrays.asList("", ChatColor.GRAY + "Assistez à un tutoriel interactif !"));
		tutorialItem.setItemMeta(meta);
		GlowEffect.addGlow(tutorialItem);
		player.getInventory().setItem(4, tutorialItem);

		// Leave item
		player.getInventory().setItem(8, p.getCoherenceMachine().getLeaveItem());


		player.updateInventory();
		player.getInventory().setHeldItemSlot(0);

	}
}
