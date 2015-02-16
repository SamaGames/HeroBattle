package net.lnfinity.HeroBattle;

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
import net.lnfinity.HeroBattle.Tools.ToolsManager;
import net.lnfinity.HeroBattle.Utils.ConfigAccessor;
import net.lnfinity.HeroBattle.Utils.CountdownTimer;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HeroBattle extends JavaPlugin {

	private CountdownTimer timer;
	private Game g;

	private ClassManager classManager;
	private ToolsManager toolsManager;

	private ScoreboardManager scoreboardManager;

	private ConfigAccessor config;

	Map<UUID, GamePlayer> players = new HashMap<UUID, GamePlayer>();

	// Global strings
	public final static String NAME = ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + "HeroBattle"
			+ ChatColor.DARK_PURPLE + "] " + ChatColor.RESET;

	@Override
	public void onEnable() {
		initWorldConfig();

		timer = new CountdownTimer(this);
		g = new Game(this);
		toolsManager = new ToolsManager(this);
		classManager = new ClassManager(this);
		scoreboardManager = new ScoreboardManager(this);

		MasterListener masterListener = new MasterListener(this);
		getServer().getPluginManager().registerEvents(masterListener, this);
		GameListener gameListener = new GameListener(this);
		getServer().getPluginManager().registerEvents(gameListener, this);
		SystemListener systemListener = new SystemListener(this);
		getServer().getPluginManager().registerEvents(systemListener, this);
		ClassSelectorListener classSelectorListener = new ClassSelectorListener(this);
		getServer().getPluginManager().registerEvents(classSelectorListener, this);

		this.getCommand("start").setExecutor(new CommandListener(this));

		for (Player player : getServer().getOnlinePlayers()) {
			players.put(player.getUniqueId(), new GamePlayer());
		}
		if (getPlayerCount() == 4) {
			timer.restartTimer();
		}
	}

	public void addGamePlayer(Player p) {
		players.put(p.getUniqueId(), new GamePlayer());
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
		for (Player player : getServer().getOnlinePlayers()) {
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

	public void initWorldConfig() {
		config = new ConfigAccessor(this, this.getServer().getWorlds().get(0).getWorldFolder(), "config");

		// TODO
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

	public ConfigAccessor getWorldConfig() {
		return config;
	}
}
