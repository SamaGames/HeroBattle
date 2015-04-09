package net.lnfinity.HeroBattle;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.lnfinity.HeroBattle.classes.ClassManager;
import net.lnfinity.HeroBattle.game.Game;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.game.ScoreboardManager;
import net.lnfinity.HeroBattle.listeners.*;
import net.lnfinity.HeroBattle.powerups.PowerupManager;
import net.lnfinity.HeroBattle.tutorial.TutorialDisplayer;
import net.lnfinity.HeroBattle.utils.CountdownTimer;
import net.lnfinity.HeroBattle.utils.GameTimer;
import net.md_5.bungee.api.ChatColor;
import net.samagames.gameapi.GameAPI;
import net.samagames.gameapi.json.Status;
import net.samagames.gameapi.themachine.CoherenceMachine;

import org.bukkit.World;
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
		arenaConfig.setDefaults(YamlConfiguration.loadConfiguration(new File(getDataFolder(), "arena.yml")));


		getServer().getPluginManager().registerEvents(new MasterListener(this), this);
		getServer().getPluginManager().registerEvents(new GameListener(this), this);
		getServer().getPluginManager().registerEvents(new SystemListener(this), this);
		getServer().getPluginManager().registerEvents(new ClassSelectorListener(this), this);
		getServer().getPluginManager().registerEvents(new PowerupsListener(this), this);

		CommandListener command = new CommandListener(this);
		this.getCommand("start").setExecutor(command);
		this.getCommand("forcestop").setExecutor(command);
		this.getCommand("powerup").setExecutor(command);

		this.getCommand("classe").setExecutor(new ClassSelectionCommand(this));


		timer = new CountdownTimer(this);
		g = new Game(this);
		gameTimer = new GameTimer(this, this.getArenaConfig().getInt("map.gameTime"));
		classManager = new ClassManager(this);
		scoreboardManager = new ScoreboardManager(this);
		powerupManager = new PowerupManager(this);
		tutorialDisplayer = new TutorialDisplayer(this);

		addOnlinePlayers();


		World world = getServer().getWorlds().get(0);
		world.setGameRuleValue("doDaylightCycle", "false");
		world.setTime(arenaConfig.getLong("map.dayTime"));


		GameAPI.registerGame(getConfig().getString("gameName"), g);

		g.setStatus(Status.Available);
	}

	public void onDisable() {
		g.setStatus(Status.Stopping);
		GameAPI.getManager().sendSync();

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
