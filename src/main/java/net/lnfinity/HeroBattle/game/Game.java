package net.lnfinity.HeroBattle.game;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.displayers.BruteClass;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ActionBar;
import net.lnfinity.HeroBattle.utils.Utils;
import net.lnfinity.HeroBattle.utils.WinnerFirework;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.samagames.gameapi.GameAPI;
import net.samagames.gameapi.json.Status;
import net.samagames.gameapi.types.GameArena;
import net.samagames.utils.GlowEffect;
import net.samagames.utils.Titles;
import net.zyuiop.MasterBundle.MasterBundle;
import net.zyuiop.statsapi.StatsApi;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class Game implements GameArena {

	private HeroBattle p;
	private Status status = Status.Idle;


	private List<Location> spawnPoints = new LinkedList<>();
	private Location hub;

	private Double bottomHeight = 0.0;

	private ArrayList<Location> tutorialLocations = new ArrayList<Location>();

	private ArrayList<Location> teleportationPortalsDestinations = new ArrayList<Location>();



	/**
	 * We store here the last players who launched a lightning bolt and where,
	 * to associate the damages to the good damager.
	 */
	private Map<UUID,Location> lastLightningBolts = new ConcurrentHashMap<>();

	/**
	 * Map player -> who poisonned him
	 */
	private Map<UUID,UUID> poisonsInProgress = new ConcurrentHashMap<>();

	/**
	 * Map player -> who inflammed him
	 */
	private Map<UUID,UUID> firesInProgress = new ConcurrentHashMap<>();

	/**
	 * Map fireballs UUID -> who launched them
	 */
	private Map<UUID,UUID> fireballsLaunched = new ConcurrentHashMap<>();


	private Random random = new Random();

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


		try {
			for (Object location : p.getArenaConfig().getList("map.teleportationPortalsDestinations")) {
				if (location instanceof String) {
					try {
						teleportationPortalsDestinations.add(Utils.stringToLocation(p, (String) location));
					} catch (IllegalArgumentException e) {
						p.getLogger().log(Level.SEVERE, "Invalid teleportation portal location in arena.yml! " + e.getMessage());
					}
				}
			}
		} catch (Exception ex) {
			p.getLogger().info("No teleportation portal locations set in arena.yml. Feature disabled.");
		}
	}

	public void start() {

		p.getTutorialDisplayer().stopForAll("Le jeu démarre...");

		setStatus(Status.InGame);

		Integer partyELO = getTotalElo() / p.getGamePlayers().size();

		p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.DARK_GREEN + "ELO" + ChatColor.GREEN + " de la partie " + ChatColor.DARK_GREEN + partyELO);
		p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Que le meilleur gagne !");

		p.getServer().getWorlds().get(0).setFullTime(p.getArenaConfig().getLong("map.dayTime"));

		teleportPlayers();

		p.getGameTimer().startTimer();
		p.getScoreboardManager().init();
		p.getPowerupManager().getSpawner().startTimer();

		for (Player player : p.getServer().getOnlinePlayers()) {
			Titles.sendTitle(player, 2, 38, 6, ChatColor.AQUA + "C'est parti !", ChatColor.GREEN + "ELO de la partie : " + ChatColor.DARK_GREEN + partyELO);

			if(MasterBundle.isDbEnabled) {
				StatsApi.increaseStat(player.getUniqueId(), HeroBattle.GAME_NAME_WHITE, "played", 1);
			}

			ActionBar.removeMessage(player);
		}

		// Old stuff
		/*
		p.getServer().getScheduler().runTaskLater(p, new Runnable() {
			@Override
			public void run() {
				// Bêta olala
				p.getServer().broadcastMessage(ChatColor.DARK_RED + "------------------------------------------------");
				p.getServer().broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Attention : ce jeu est encore en bêta !");
				p.getServer().broadcastMessage(ChatColor.GOLD + "Les classes, compétences ou powerups sont " + ChatColor.YELLOW + "susceptibles d'évoluer" + ChatColor.GOLD + ", ceci dans le but d'améliorer et d'équilibrer le jeu.");
				p.getServer().broadcastMessage(ChatColor.GOLD + "D'autres fonctionnalités pourraient être ajoutées ou modifiées.");
				p.getServer().broadcastMessage("");
				p.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "N'hésitez pas à nous " + ChatColor.YELLOW + "" + ChatColor.BOLD + "transmettre votre avis" + ChatColor.GOLD + "" + ChatColor.BOLD + " en jeu ou via le forum, pour que nous puissions faire évoluer au mieux le jeu !");
				p.getServer().broadcastMessage(ChatColor.DARK_RED + "------------------------------------------------");
			}
		}, 60l);*/

		p.getServer().getScheduler().runTaskLater(p, new Runnable() {
			@Override
			public void run() {
				// Toxic water
				if(p.getArenaConfig().getBoolean("map.toxicWater", false)) {
					p.getServer().broadcastMessage("");
					p.getServer().broadcastMessage(ChatColor.DARK_AQUA + "-----------------------------------------------------");
					p.getServer().broadcastMessage(ChatColor.DARK_AQUA + "[×] " + ChatColor.AQUA + "Attention, l'eau est " + ChatColor.DARK_AQUA + "toxique" + ChatColor.AQUA + " sur cette carte !");
					p.getServer().broadcastMessage(ChatColor.DARK_AQUA + "-----------------------------------------------------");
					p.getServer().broadcastMessage("");
				}

				// Toxic lava
				if(p.getArenaConfig().getBoolean("map.toxicLava", false)) {
					p.getServer().broadcastMessage("");
					p.getServer().broadcastMessage(ChatColor.DARK_RED + "-----------------------------------------------------");
					p.getServer().broadcastMessage(ChatColor.DARK_RED + "[×] " + ChatColor.GOLD + "Attention, la lave est " + ChatColor.RED + "instantanément mortelle" + ChatColor.GOLD + " ici !");
					p.getServer().broadcastMessage(ChatColor.DARK_RED + "-----------------------------------------------------");
					p.getServer().broadcastMessage("");
				}

			}
		}, 70l);
	}

	public void teleportPlayers() {
		List<Location> tempLocs = new LinkedList<>(spawnPoints);
		Random rand = new Random();

		for (GamePlayer hbPlayer : p.getGamePlayers().values()) {
			Player player = p.getServer().getPlayer(hbPlayer.getPlayerUniqueID());

			if(player == null) continue;

			if (hbPlayer.getPlayerClass() == null) {
				chooseRandomClass(player);
			}
			
			CraftPlayer cp = (CraftPlayer) player;
			EntityPlayer ep = cp.getHandle();
			int ping = ep.ping;
			if(ping > 500) {
				player.sendMessage("");
				player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Attention" + ChatColor.RED + ", nous venons de détecter que votre connexion était instable. Des effets secondaires peuvent se faire ressentir en jeu.");
				player.sendMessage("");
			}

			int index = rand.nextInt(tempLocs.size());
			player.teleport(tempLocs.get(index));
			tempLocs.remove(index);

			player.getInventory().clear();
			player.setLevel(0);
			
			player.setAllowFlight(true);
			player.setFlySpeed(0.00F);
			
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

		hbPlayer.setPercentage(0, null);
		player.setExp(0);
		player.setLevel(0);
		player.setTotalExperience(0);

		player.setHealth(hbPlayer.getLives() * 2);

		teleportRandomSpot(player);
	}

	public void enableSpectatorMode(final Player player) {
		GamePlayer hbPlayer = p.getGamePlayer(player);

		hbPlayer.setPlaying(false);

		player.setGameMode(GameMode.SPECTATOR);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);

		teleportRandomSpot(player);
	}

	public void chooseRandomClass(Player player) {
		GamePlayer gamePlayer = p.getGamePlayer(player);

		if(gamePlayer == null || gamePlayer.getAvaibleClasses().size() == 0) return;

		Random rnd = new Random();

		int r = rnd.nextInt(gamePlayer.getAvaibleClasses().size());
		int i = 0;
		
		// Avoid game crashing
		if(gamePlayer.getAvaibleClasses() == null || gamePlayer.getAvaibleClasses().isEmpty()) {
			gamePlayer.setPlayerClass(new BruteClass(p, 0, 0, 0));
			return;
		}
		
		for (PlayerClass classe : gamePlayer.getAvaibleClasses()) {
			if (i == r) {
				gamePlayer.setPlayerClass(classe);
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

		// Avoid the death to be handled if the player is already completely dead (no life left)
		if(hbPlayer.getLives() <= 0) return;


		// Avoids this to be called a lot of times
		if(hbPlayer.isDeathHandled()) return;

		hbPlayer.setDeathHandled(true);
		p.getServer().getScheduler().runTaskLater(p, new Runnable() {
			@Override
			public void run() {
				hbPlayer.setDeathHandled(false);
			}
		}, 15l);


		// Technical stuff
		hbPlayer.setLives(hbPlayer.getLives() - 1);
		player.setVelocity(new Vector());

		// Broadcasts
		String s = hbPlayer.getLives() <= 1 ? "" : "s";
		String lives = ChatColor.DARK_GRAY + " (" + ChatColor.RED + hbPlayer.getLives() + ChatColor.DARK_GRAY
				+ " vie" + s + ")";

		Player lastDamagerPlayer = hbPlayer.getLastDamager() != null ? p.getServer().getPlayer(hbPlayer.getLastDamager()) : null;
		GamePlayer lastDamagerGPlayer = p.getGamePlayer(lastDamagerPlayer);

		String killedByMessage = hbPlayer.getLives() >= 1 ? ChatColor.RED + "Vous perdez une vie !" : ChatColor.RED + "C'est fini pour vous !";

		if (hbPlayer.getLastDamager() == null || lastDamagerPlayer == null || lastDamagerPlayer.getGameMode() == GameMode.SPECTATOR) {
			switch (death) {
				case FALL:
					p.getServer().broadcastMessage(
							HeroBattle.GAME_TAG + ChatColor.DARK_RED + player.getName() + ChatColor.YELLOW
									+ " est tombé dans le vide" + lives);
					break;

				case QUIT:
					p.getServer().broadcastMessage(
							HeroBattle.GAME_TAG + ChatColor.DARK_RED + player.getName() + ChatColor.YELLOW
									+ " a quitté la partie");
					break;

				case KO:
					p.getServer().broadcastMessage(
							HeroBattle.GAME_TAG + ChatColor.DARK_RED + player.getName() + ChatColor.YELLOW + " est K.O. !"
									+ lives);

					killedByMessage = ChatColor.RED + "Vous êtes K.O. !";

					break;

				case WATER:
					p.getServer().broadcastMessage(
							HeroBattle.GAME_TAG + ChatColor.DARK_RED + player.getName() + ChatColor.YELLOW + " est tombé dans l'eau !"
									+ lives);

					killedByMessage = ChatColor.RED + "Vous êtes tombé dans l'eau !";

					break;

				case LAVA:
					p.getServer().broadcastMessage(
							HeroBattle.GAME_TAG + ChatColor.DARK_RED + player.getName() + ChatColor.YELLOW + " s'est consumé dans la lave !"
									+ lives);

					killedByMessage = ChatColor.RED + "Vous vous êtes consumé dans la lave !";

					break;
			}
		}

		else {
			String groupColor = Utils.getPlayerColor(lastDamagerPlayer);
			switch (death) {
				case FALL:
					if(player.getUniqueId().equals(lastDamagerPlayer.getUniqueId())) {
						p.getServer().broadcastMessage(
								HeroBattle.GAME_TAG + ChatColor.DARK_RED + player.getName() + ChatColor.YELLOW
										+ " est tombé dans le vide" + lives);
					} else {
						killedByMessage = groupColor + lastDamagerPlayer.getName() + ChatColor.RED + " vous a éjecté !";
						p.getServer().broadcastMessage(
								HeroBattle.GAME_TAG + ChatColor.DARK_RED + player.getName() + ChatColor.YELLOW
										+ " a été poussé par " + ChatColor.DARK_GREEN + p.getServer().getPlayer(hbPlayer.getLastDamager()).getName()
										+ lives);
						StatsApi.increaseStat(hbPlayer.getLastDamager(), p.getName(), "kills", 1);
						lastDamagerGPlayer.creditCoins(3, "Un joueur poussé !");
					}
					break;

				case QUIT:
					if(!player.getUniqueId().equals(lastDamagerPlayer.getUniqueId())) {
						StatsApi.increaseStat(hbPlayer.getLastDamager(), p.getName(), "kills", 1);
						lastDamagerGPlayer.creditCoins(3, "Un froussard !");
					}
					p.getServer().broadcastMessage(
							HeroBattle.GAME_TAG + ChatColor.DARK_RED + player.getName() + ChatColor.YELLOW
									+ " a quitté la partie");
					break;

				case KO:
					killedByMessage = groupColor + lastDamagerPlayer.getName() + ChatColor.RED + " vous a mis K.O. !";
					if(player.getUniqueId().equals(lastDamagerPlayer.getUniqueId())) {
						killedByMessage = ChatColor.RED + "Vous vous êtes suicidé !";
						p.getServer().broadcastMessage(
								HeroBattle.GAME_TAG + ChatColor.DARK_RED + player.getName() + ChatColor.YELLOW
										+ " s'est suicidé" + lives);
					} else {
						p.getServer().broadcastMessage(
								HeroBattle.GAME_TAG + ChatColor.DARK_RED + player.getName() + ChatColor.YELLOW
										+ " a été mis K.O. par " + ChatColor.DARK_GREEN + p.getServer().getPlayer(hbPlayer.getLastDamager()).getName()
										+ lives);

						StatsApi.increaseStat(hbPlayer.getLastDamager(), p.getName(), "kills", 1);
						lastDamagerGPlayer.creditCoins(3, "Un joueur K.O. !");
					}

					break;

				case WATER:
					killedByMessage = groupColor + lastDamagerPlayer.getName() + ChatColor.RED + " vous a poussé dans l'eau";

					p.getServer().broadcastMessage(
							HeroBattle.GAME_TAG + ChatColor.DARK_RED + player.getName() + ChatColor.YELLOW
									+ " a été poussé dans l'eau par " + ChatColor.DARK_GREEN + p.getServer().getPlayer(hbPlayer.getLastDamager()).getName()
									+ lives);

					StatsApi.increaseStat(hbPlayer.getLastDamager(), p.getName(), "kills", 1);
					lastDamagerGPlayer.creditCoins(3, "Un joueur poussé à l'eau !");

					break;

				case LAVA:
					killedByMessage = groupColor + lastDamagerPlayer.getName() + ChatColor.RED + " vous a poussé dans la lave";

					p.getServer().broadcastMessage(
							HeroBattle.GAME_TAG + ChatColor.DARK_RED + player.getName() + ChatColor.YELLOW
									+ " a été poussé dans la lave par " + ChatColor.DARK_GREEN + p.getServer().getPlayer(hbPlayer.getLastDamager()).getName()
									+ lives);

					StatsApi.increaseStat(hbPlayer.getLastDamager(), p.getName(), "kills", 1);
					lastDamagerGPlayer.creditCoins(3, "Un joueur poussé dans la lave !");

					break;
			}

			p.getGamePlayer(hbPlayer.getLastDamager()).addPlayersKilled();
		}


		// Effects on the player
		for(PotionEffect effect : player.getActivePotionEffects()) {
			// Clears current effects
		    player.removePotionEffect(effect.getType());
		}
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
		p.getServer().getScheduler().runTaskLater(p, new Runnable() {
			@Override
			public void run() {
				player.playSound(player.getLocation(), Sound.IRONGOLEM_DEATH, 1, 1);
			}
		}, 5);


		// Removes the fire
		p.getServer().getScheduler().runTaskLater(p, new Runnable() {
			@Override
			public void run() {
				player.setFireTicks(0);
			}
		}, 5l);


		// Death message
		final String finalKilledByMessage = killedByMessage;
		if (hbPlayer.getLives() >= 1) {

			Titles.sendTitle(player, 3, 150, 10, Utils.heartsToString(hbPlayer, true), killedByMessage);
			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					Titles.sendTitle(player, 15, 50, 8, Utils.heartsToString(hbPlayer), ChatColor.RED
							+ finalKilledByMessage);
				}
			}, 10L);

		} else {
			Titles.sendTitle(player, 3, 150, 0, Utils.heartsToString(hbPlayer, true), ChatColor.RED
					+ killedByMessage);
			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					Titles.sendTitle(player, 15, 100, 18, Utils.heartsToString(hbPlayer), ChatColor.RED
							+ finalKilledByMessage);
				}
			}, 10L);
		}


		// Respawn
		if (hbPlayer.getLives() >= 1) {
			hbPlayer.setRespawning(true);

			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					hbPlayer.setRespawning(false);
				}
			}, 40L);

			spawnPlayer(player);

			// Très important ! Sinon le joueur conserve sa vélocité
			player.setVelocity(player.getVelocity().zero());

		}

		else {
			player.setFlySpeed(0.1F);
			enableSpectatorMode(player);

			s = p.getPlayingPlayerCount() <= 1 ? "" : "s";

			p.getServer().broadcastMessage(
					HeroBattle.GAME_TAG + ChatColor.DARK_RED + player.getName() + ChatColor.YELLOW + " a perdu ! "
							+ ChatColor.DARK_GRAY + "[" + ChatColor.RED + p.getPlayingPlayerCount()
							+ ChatColor.DARK_GRAY + " joueur" + s + ChatColor.DARK_GRAY + "]");


			if (p.getPlayingPlayerCount() == 1) {
				for (GamePlayer pl : p.getGamePlayers().values()) {
					if (pl.isPlaying()) {
						onPlayerWin(pl.getPlayerUniqueID());
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
		if(gPlayer == null) {
			return;
		}

		if (gPlayer.isPlaying()) {
			gPlayer.setPlaying(false);

			String s = "s";
			if (p.getPlayingPlayerCount() == 1)
				s = "";

			p.getServer().broadcastMessage(
					HeroBattle.GAME_TAG + ChatColor.YELLOW + p.getServer().getPlayer(id).getDisplayName()
							+ ChatColor.YELLOW + " a perdu ! " + ChatColor.DARK_GRAY + "[" + ChatColor.RED
							+ (p.getPlayingPlayerCount() - 1) + ChatColor.DARK_GRAY + " joueur" + s +
							ChatColor.DARK_GRAY + "]");

			p.getScoreboardManager().refresh();
		}

		if (p.getPlayingPlayerCount() == 1) {
			for (GamePlayer pl : p.getGamePlayers().values()) {
				if (pl.isPlaying()) {
					onPlayerWin(pl.getPlayerUniqueID());
					return;
				}
			}

			onPlayerWin(null);
		}
	}

	/**
	 * Call this when the game is finished.
	 *
	 * @param id The UUID of the winner. If {@code null}, no winner for this game (counter timed out, as example).
	 */
	public void onPlayerWin(UUID id) {

		this.setStatus(Status.Stopping);

		p.getScoreboardManager().refresh();
		p.getScoreboardManager().refreshTab();

		if(id != null) {
			Player player = p.getServer().getPlayer(id);
			GamePlayer gPlayer = p.getGamePlayer(player);

			player.getInventory().clear();

			gPlayer.setPlaying(false);

			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(ChatColor.GOLD + "----------------------------------------------------");
			Bukkit.broadcastMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + player.getDisplayName() + ChatColor.GREEN + ChatColor.BOLD + " remporte la partie !");
			Bukkit.broadcastMessage(ChatColor.GOLD + "----------------------------------------------------");
			Bukkit.broadcastMessage("");

			new WinnerFirework(p, 30, player);

			gPlayer.creditStars(1, "Victoire !");
			gPlayer.creditCoins(16, "Victoire !");
			StatsApi.increaseStat(player, p.getName(), "wins", 1);
		}

		p.getPowerupManager().getSpawner().stopTimer();
		p.getGameTimer().pauseTimer();


		calculateElos(id);

		
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


		p.getServer().getScheduler().runTaskLaterAsynchronously(p, new Runnable() {
			@Override
			public void run() {
				Map<UUID, Long> percentagesInflicted = new TreeMap<>(new Comparator<UUID>() {
					@Override
					public int compare(UUID a, UUID b) {
						try {
							Long prcA = p.getGamePlayer(a).getPercentageInflicted();
							Long prcB = p.getGamePlayer(b).getPercentageInflicted();

							if (prcA >= prcB) return -1;
							else return 1;

						} catch (NullPointerException e) {
							return 0;
						}
					}
				});

				Map<UUID, Integer> kills = new TreeMap<>(new Comparator<UUID>() {
					@Override
					public int compare(UUID a, UUID b) {
						try {
							Integer killsA = p.getGamePlayer(a).getPlayersKilled();
							Integer killsB = p.getGamePlayer(b).getPlayersKilled();

							if (killsA >= killsB) return -1;
							else return 1;

						} catch (NullPointerException e) {
							return 0;
						}
					}
				});

				for (GamePlayer player : p.getGamePlayers().values()) {
					percentagesInflicted.put(player.getPlayerUniqueID(), player.getPercentageInflicted());
					kills.put(player.getPlayerUniqueID(), player.getPlayersKilled());
				}


				String[] topsPercentages = new String[]{"", "", ""};
				String[] topsKills       = new String[]{"", "", ""};


				// Kills
				int i = 0;
				Iterator<Map.Entry<UUID, Integer>> iterKills = kills.entrySet().iterator();
				while (i < 3 && iterKills.hasNext()) {
					Map.Entry<UUID, Integer> entry = iterKills.next();
					topsKills[i] = Bukkit.getOfflinePlayer(entry.getKey()).getName() + ChatColor.AQUA + " (" + entry.getValue() + ")";

					GamePlayer gPlayer = p.getGamePlayer(entry.getKey());
					if(gPlayer != null) {
						gPlayer.creditCoins(i == 0 ? 10 : i == 1 ? 6 : 4, "Rang " + (i + 1) + " au classement des kills !");
					}

					i++;
				}

				// Percentages
				i = 0;
				Iterator<Map.Entry<UUID, Long>> iterPercentages = percentagesInflicted.entrySet().iterator();
				while (i < 3 && iterPercentages.hasNext()) {
					Map.Entry<UUID, Long> entry = iterPercentages.next();
					topsPercentages[i] = Bukkit.getOfflinePlayer(entry.getKey()).getName() + ChatColor.AQUA + " (" + Utils.formatNumber(entry.getValue()) + "%)";

					GamePlayer gPlayer = p.getGamePlayer(entry.getKey());
					if(gPlayer != null) {
						gPlayer.creditCoins(i == 0 ? 10 : i == 1 ? 6 : 4, "Rang " + (i + 1) + " au classement des dégâts infligés !");
					}

					i++;
				}

				Bukkit.broadcastMessage(ChatColor.GOLD + "----------------------------------------------------");
				Bukkit.broadcastMessage(ChatColor.GOLD + "                        Classement des Kills        ");
				Bukkit.broadcastMessage(ChatColor.GOLD + "                                                    ");
				Bukkit.broadcastMessage(ChatColor.YELLOW + " " + topsKills[0] + ChatColor.DARK_GRAY + (!topsKills[1].isEmpty() ? " ⋅ " : "") + ChatColor.GRAY + topsKills[1] + ChatColor.DARK_GRAY + (!topsKills[2].isEmpty() ? " ⋅ " : "") + ChatColor.GOLD + topsKills[2]);
				Bukkit.broadcastMessage(ChatColor.GOLD + "                                                    ");
				Bukkit.broadcastMessage(ChatColor.GOLD + "                  Classement des dégâts infligés    ");
				Bukkit.broadcastMessage(ChatColor.GOLD + "                                                    ");
				Bukkit.broadcastMessage(ChatColor.YELLOW + " " + topsPercentages[0] + ChatColor.DARK_GRAY + (!topsPercentages[1].isEmpty() ? " ⋅ " : "") + ChatColor.GRAY + topsPercentages[1] + ChatColor.DARK_GRAY + (!topsPercentages[2].isEmpty() ? " ⋅ " : "") + ChatColor.GOLD + topsPercentages[2]);
				Bukkit.broadcastMessage(ChatColor.GOLD + "                                                    ");
				Bukkit.broadcastMessage(ChatColor.GOLD + "----------------------------------------------------");

			}
		}, 20 * 5l);


		p.getServer().getScheduler().runTaskLater(p, new Runnable() {
			@Override
			public void run() {
				for (GamePlayer gPlayer : p.getGamePlayers().values()) {
					Player player = p.getServer().getPlayer(gPlayer.getPlayerUniqueID());
					if (player != null) {
						int starsGained = gPlayer.getStarsGained();
						int coinsGained = gPlayer.getCoinsGained();

						String sStars = starsGained > 1 ? "s" : "";
						String sCoins = coinsGained > 1 ? "s" : "";

						if (starsGained > 0 || coinsGained > 0) {
							player.sendMessage(ChatColor.YELLOW + "Vous avez gagné en tout " + ChatColor.GOLD + coinsGained + ChatColor.YELLOW + " pièce" + sCoins + " et " + ChatColor.GOLD + starsGained + ChatColor.YELLOW + " étoile" + sStars + ".");
						}
					}
				}
			}
		}, 20 * 8l);


		// Analytics to help us improve the game
		if(!p.getArenaConfig().getBoolean("block-analytics")) {

			final String serverName = MasterBundle.getServerName();
			final String mapName = p.getGame().getMapName();
			final Integer playersCount = p.getGamePlayers().size();
			final String duration = p.getGameTimer().getFormattedTime();

			final String winnerName;
			final Integer winnerELO;
			final String winnerClass;

			if(id != null) {
				Player player = p.getServer().getPlayer(id);
				GamePlayer gPlayer = p.getGamePlayer(player);

				winnerName = player.getName();
				winnerELO = gPlayer.getElo();
				winnerClass = gPlayer.getPlayerClass().getType().toString().toLowerCase();
			}
			else {
				winnerName = "(No winner)";
				winnerELO = 0;
				winnerClass = "";
			}

			p.getServer().getScheduler().runTaskAsynchronously(p, new Runnable() {
				@Override
				public void run() {
					try {
						URL u = new URL("http://lnfinity.net/tasks/herobattle-stats?v=1&s=" + URLEncoder.encode(serverName, "UTF-8") + "&m=" + URLEncoder.encode(mapName, "UTF-8") + "&p=" + playersCount + "&d=" + URLEncoder.encode(duration, "UTF-8") + "&w=" + URLEncoder.encode(winnerName, "UTF-8") + "&we=" + winnerELO + "&wc=" + URLEncoder.encode(winnerClass, "UTF-8"));
						u.openStream();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
		}
		
		if (MasterBundle.isDbEnabled) {
			Bukkit.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					for (Player player : p.getServer().getOnlinePlayers()) {
						GameAPI.kickPlayer(player);
					}
				}
			}, 14 * 20L);

			Bukkit.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					Bukkit.shutdown();
				}
			}, 18 * 20L);
		}
	}

	/**
	 * Calculates the ELOs of the players.
	 *
	 * @param winner The winner. Can be {@code null}.
	 */
	public void calculateElos(UUID winner) {
		double total = getTotalElo();
		
		for(GamePlayer gamePlayer : p.getGamePlayers().values()) {
			double esp = (gamePlayer.getElo() / total); // Espérance de gain pour gamePlayer
			double k = 16;

			if(gamePlayer.getPlayerUniqueID() == winner) { // Le joueur a gagné
				int elo = gamePlayer.getElo();
				k = 20 * p.getGamePlayers().size() * ((total / p.getGamePlayers().size()) / elo);
				int elo1 = (int) (k * (1 - esp));
				double mult = 1 / Utils.logb((elo + 1000)/1000, 2);

				gamePlayer.setElo((int) (mult * elo1 + elo));

				if(gamePlayer.getElo() > 10000) {
					gamePlayer.setElo(10000);
				}
				
			} else { // Le joueur n'a pas gagné
				int elo = gamePlayer.getElo();
				k = 40 * elo / (total / p.getGamePlayers().size());
				int elo1 = (int) (k * - esp);
				double mult = Utils.logb((elo + 1000)/1000, 2);

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


	public Map<UUID, Location> getLastLightningBolts() {
		return lastLightningBolts;
	}

	public Map<UUID, UUID> getPoisonsInProgress() {
		return poisonsInProgress;
	}

	public Map<UUID, UUID> getFiresInProgress() {
		return firesInProgress;
	}

	public Map<UUID, UUID> getFireballsLaunched() {
		return fireballsLaunched;
	}


	public ArrayList<Location> getTutorialLocations() {
		return tutorialLocations;
	}

	public ArrayList<Location> getTeleportationPortalsDestinations() {
		return teleportationPortalsDestinations;
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

		GamePlayer gamePlayer = p.getGamePlayer(player);

		if(!gamePlayer.isPlaying()) return;


		if (gamePlayer.isInvisible()) {
			player.getInventory().setArmorContents(null);
		}

		else {

			ItemStack hat = gamePlayer.getPlayerClass().getHat();
			ItemMeta hatMeta = hat.getItemMeta();
			hatMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + gamePlayer.getPlayerClass().getName());
			hatMeta.setLore(gamePlayer.getPlayerClass().getClassDetailsLore());
			hat.setItemMeta(hatMeta);
			player.getInventory().setHelmet(hat);


			// Armor colors
			int R = 470 - gamePlayer.getPercentage();
			int G = 255 - gamePlayer.getPercentage();
			int B = 255 - gamePlayer.getPercentage() * 2;

			if (R > 255)    R = 255;
			else if (R < 0) R = 0;

			if (G > 255)    G = 255;
			else if (G < 0) G = 0;

			if (B > 255)    B = 255;
			else if (B < 0) B = 0;


			ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);

			LeatherArmorMeta meta = (LeatherArmorMeta) chest.getItemMeta();
			meta.setColor(Color.fromRGB(R, G, B));
			meta.spigot().setUnbreakable(true);

			chest.setItemMeta(meta);

			ItemStack leg = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			leg.setItemMeta(meta);

			ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
			boots.setItemMeta(meta);

			player.getInventory().setChestplate(chest);
			player.getInventory().setLeggings(leg);
			player.getInventory().setBoots(boots);
		}
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
