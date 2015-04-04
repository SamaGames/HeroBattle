package net.lnfinity.HeroBattle;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.lnfinity.HeroBattle.Class.ClassManager;
import net.lnfinity.HeroBattle.Game.Game;
import net.lnfinity.HeroBattle.Game.GamePlayer;
import net.lnfinity.HeroBattle.Game.ScoreboardManager;
import net.lnfinity.HeroBattle.Listeners.ClassSelectionCommand;
import net.lnfinity.HeroBattle.Listeners.ClassSelectorListener;
import net.lnfinity.HeroBattle.Listeners.CommandListener;
import net.lnfinity.HeroBattle.Listeners.GameListener;
import net.lnfinity.HeroBattle.Listeners.MasterListener;
import net.lnfinity.HeroBattle.Listeners.SystemListener;
import net.lnfinity.HeroBattle.Powerups.PowerupManager;
import net.lnfinity.HeroBattle.Tutorial.TutorialDisplayer;
import net.lnfinity.HeroBattle.Utils.CountdownTimer;
import net.lnfinity.HeroBattle.Utils.GameTimer;
import net.md_5.bungee.api.ChatColor;
import net.samagames.gameapi.GameAPI;
import net.samagames.gameapi.json.Status;
import net.samagames.gameapi.themachine.CoherenceMachine;
import net.zyuiop.MasterBundle.MasterBundle;
import net.zyuiop.statsapi.StatsApi;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HeroBattle extends JavaPlugin {

	// Displayed name with misc. formats
	public final static String GAME_NAME_WHITE = "HeroBattle";
	public final static String GAME_NAME = ChatColor.LIGHT_PURPLE + GAME_NAME_WHITE;
	public final static String GAME_NAME_BICOLOR = ChatColor.DARK_PURPLE + "Hero" + ChatColor.LIGHT_PURPLE + "Battle";
	public final static String GAME_NAME_BICOLOR_BOLD = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Hero"
			+ ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Battle";

	private static CoherenceMachine coherenceMachine = GameAPI.getCoherenceMachine(GAME_NAME_WHITE);
	public final static String GAME_TAG = coherenceMachine.getGameTag();

	private Game g;

	private CountdownTimer timer;
	private GameTimer gameTimer;

	private ClassManager classManager;
	private PowerupManager powerupManager;

	private ScoreboardManager scoreboardManager;

	private TutorialDisplayer tutorialDisplayer;

	private Configuration arenaConfig;

	private Map<UUID, GamePlayer> players = new HashMap<>();

	@Override
	public void onEnable() {
		saveDefaultConfig();

		File arenaFile = new File(getServer().getWorlds().get(0).getWorldFolder(), "arena.yml");
		if (!arenaFile.exists()) {
			getLogger().severe("#==================[Fatal exception report]==================#");
			getLogger().severe("# The arena.yml description file was NOT FOUND.              #");
			getLogger().severe("# The plugin cannot load without it, please create it.       #");
			getLogger().severe("# The file path is the following :                           #");
			getLogger().severe(arenaFile.getAbsolutePath());
			getLogger().severe("#============================================================#");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);

		arenaConfig.addDefault("map.name", "HeroBattle");
		arenaConfig.addDefault("map.maxPlayers", 10);
		arenaConfig.addDefault("map.maxVIP", 2);
		arenaConfig.addDefault("map.hub", "0;64;0");
		arenaConfig.addDefault("map.spawns", Arrays.asList("0;64;0", "0;64;0"));
		arenaConfig.addDefault("map.bottom", 0);

		MasterListener masterListener = new MasterListener(this);
		getServer().getPluginManager().registerEvents(masterListener, this);
		GameListener gameListener = new GameListener(this);
		getServer().getPluginManager().registerEvents(gameListener, this);
		SystemListener systemListener = new SystemListener(this);
		getServer().getPluginManager().registerEvents(systemListener, this);
		ClassSelectorListener classSelectorListener = new ClassSelectorListener(this);
		getServer().getPluginManager().registerEvents(classSelectorListener, this);

		CommandListener command = new CommandListener(this);
		this.getCommand("start").setExecutor(command);
		this.getCommand("forcestop").setExecutor(command);

		this.getCommand("classe").setExecutor(new ClassSelectionCommand(this));

		for (Player player : getServer().getOnlinePlayers()) {
			addGamePlayer(player);
		}

		timer = new CountdownTimer(this);
		g = new Game(this);
		gameTimer = new GameTimer(this, this.getArenaConfig().getInt("map.gameTime"));
		classManager = new ClassManager(this);
		scoreboardManager = new ScoreboardManager(this);
		powerupManager = new PowerupManager(this);
		tutorialDisplayer = new TutorialDisplayer(this);

		addOnlinePlayers();

		GameAPI.registerGame(getConfig().getString("gameName"), g);

		g.setStatus(Status.Available);
	}

	public void onDisable() {
		g.setStatus(Status.Stopping);
		GameAPI.getManager().sendSync();

		// Saving Elos
		this.getLogger().info("Trying to save players ELOs...");

		if (MasterBundle.isDbEnabled) {
			for (final GamePlayer gamePlayer : this.getGamePlayers().values()) {
				this.getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
					@Override
					public void run() {
						int old = StatsApi.getPlayerStat(gamePlayer.getPlayerUniqueID(), "herobattle", "elo");
						StatsApi.increaseStat(gamePlayer.getPlayerUniqueID(), "herobattle", "elo", gamePlayer.getElo() - old);
					}
				});
			}
		}

		this.getLogger().info("Players ELO succefully syncronized !");

		GameAPI.getManager().disable();
	}

	// For local debuging purpose only (/rl)
	public void addOnlinePlayers() {
		for (Player player : this.getServer().getOnlinePlayers()) {
			this.addGamePlayer(player);
			getClassManager().addPlayerClasses(player);
		}
	}

	public void addGamePlayer(Player p) {
		GamePlayer player = new GamePlayer(p.getUniqueId());
		players.put(p.getUniqueId(), player);
	}

	public void removeGamePlayer(Player p) {
		players.remove(p.getUniqueId());
	}

	public GamePlayer getGamePlayer(Player p) {
		return players.get(p.getUniqueId());
	}

	public GamePlayer getGamePlayer(UUID id) {
		return players.get(id);
	}

	public Map<UUID, GamePlayer> getGamePlayers() {
		return players;
	}

	public int getPlayerCount() {
		int count = 0;
		for (Player ignored : getServer().getOnlinePlayers()) {
			count++;
		}
		return count;
	}

	public int getPlayingPlayerCount() {
		int count = 0;
		for (GamePlayer player : players.values()) {
			if (player.isPlaying()) {
				count++;
			}
		}
		return count;
	}

	public Game getGame() {
		return g;
	}

	public CountdownTimer getTimer() {
		return timer;
	}

	public GameTimer getGameTimer() {
		return gameTimer;
	}

	public ClassManager getClassManager() {
		return classManager;
	}

	public ScoreboardManager getScoreboardManager() {
		return scoreboardManager;
	}

	public Configuration getArenaConfig() {
		return arenaConfig;
	}

	public PowerupManager getPowerupManager() {
		return powerupManager;
	}

	public TutorialDisplayer getTutorialDisplayer() {
		return tutorialDisplayer;
	}

	public CoherenceMachine getCoherenceMachine() {
		return coherenceMachine;
	}
}
