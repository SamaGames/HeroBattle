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
import net.lnfinity.HeroBattle.Listeners.ClassSelectorListener;
import net.lnfinity.HeroBattle.Listeners.CommandListener;
import net.lnfinity.HeroBattle.Listeners.GameListener;
import net.lnfinity.HeroBattle.Listeners.MasterListener;
import net.lnfinity.HeroBattle.Listeners.SystemListener;
import net.lnfinity.HeroBattle.Powerups.PowerupManager;
import net.lnfinity.HeroBattle.Tools.ToolsManager;
import net.lnfinity.HeroBattle.Utils.CountdownTimer;
import net.md_5.bungee.api.ChatColor;
import net.samagames.gameapi.GameAPI;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HeroBattle extends JavaPlugin {

	private CountdownTimer timer;
	private Game g;

	private ClassManager classManager;
	private ToolsManager toolsManager;
	
	private PowerupManager powerupManager;

	private ScoreboardManager scoreboardManager;

	private Configuration arenaConfig;

	Map<UUID, GamePlayer> players = new HashMap<>();

	// Global strings
	public final static String NAME = ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + "HeroBattle"
			+ ChatColor.DARK_PURPLE + "] " + ChatColor.RESET;

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

		for (Player player : getServer().getOnlinePlayers()) {
			addGamePlayer(player);
		}

		timer = new CountdownTimer(this);
		g = new Game(this);
		toolsManager = new ToolsManager(this);
		classManager = new ClassManager(this);
		scoreboardManager = new ScoreboardManager(this);
		powerupManager = new PowerupManager(this);
		
		addOnlinePlayers();

		GameAPI.registerGame(getConfig().getString("gameName"), g);
	}

	public void onDisable() {
		// TODO g.setStatus(Status.Stopping)
		// GameAPI.getManager().sendSync();
		// GameAPI.getManager().disable();
	}
	
	// For local debuging purpose only (/rl)
	public void addOnlinePlayers() {
		for (Player player : this.getServer().getOnlinePlayers()) {
			this.addGamePlayer(player);
		}
	}

	public void addGamePlayer(Player p) {
		GamePlayer player = new GamePlayer();
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

	public int getPlayerCount() {
		int count = 0;
		for (Player ignored : getServer().getOnlinePlayers()) {
			count++;
		}
		return count;
	}

	public int getPlayingPlayerCount() {
		int count = 0;
		for (Player player : getServer().getOnlinePlayers()) {
			if (getGamePlayer(player).isPlaying()) {
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

	public ClassManager getClassManager() {
		return classManager;
	}

	public ToolsManager getToolsManager() {
		return toolsManager;
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
}
