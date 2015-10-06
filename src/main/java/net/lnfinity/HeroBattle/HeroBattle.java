package net.lnfinity.HeroBattle;

import net.lnfinity.HeroBattle.classes.ClassManager;
import net.lnfinity.HeroBattle.game.HeroBattleGame;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import net.lnfinity.HeroBattle.game.ScoreboardManager;
import net.lnfinity.HeroBattle.gui.core.Gui;
import net.lnfinity.HeroBattle.gui.core.GuiUtils;
import net.lnfinity.HeroBattle.listeners.ConnectionsListener;
import net.lnfinity.HeroBattle.listeners.GameListener;
import net.lnfinity.HeroBattle.listeners.PowerupsListener;
import net.lnfinity.HeroBattle.listeners.PreStartInteractionsListener;
import net.lnfinity.HeroBattle.listeners.SystemListener;
import net.lnfinity.HeroBattle.listeners.commands.ClassPreviewCommand;
import net.lnfinity.HeroBattle.listeners.commands.ClassSelectionCommand;
import net.lnfinity.HeroBattle.listeners.commands.CommandListener;
import net.lnfinity.HeroBattle.powerups.PowerupManager;
import net.lnfinity.HeroBattle.tutorial.TutorialDisplayer;
import net.lnfinity.HeroBattle.utils.CountdownTimer;
import net.lnfinity.HeroBattle.utils.GameTimer;
import net.md_5.bungee.api.ChatColor;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.shops.AbstractShopsManager;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;


public class HeroBattle extends JavaPlugin
{

	// Displayed name with misc. formats
	public final static String GAME_NAME_WHITE = "HeroBattle";
	public final static String GAME_NAME = ChatColor.LIGHT_PURPLE + GAME_NAME_WHITE;
	public final static String GAME_NAME_BICOLOR = ChatColor.DARK_PURPLE + "Hero" + ChatColor.LIGHT_PURPLE + "Battle";
	public final static String GAME_NAME_BICOLOR_BOLD = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Hero"
			+ ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Battle";

	public final static String GAME_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "HB" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET; // TODO Temp (use coherence machine here)

	public static int errorCalls = 0;

	private static HeroBattle instance;

	private HeroBattleGame game;

	private CountdownTimer timer;
	private GameTimer gameTimer;

	private ClassManager classManager;
	private PowerupManager powerupManager;

	private ScoreboardManager scoreboardManager;

	private TutorialDisplayer tutorialDisplayer;

	private Configuration arenaConfig;

	public static HeroBattle get()
	{
		return instance;
	}

	@Override
	public void onEnable()
	{

		instance = this;

		saveDefaultConfig();

		// TODO migrate to the new configuration format.
		File arenaFile = new File(getServer().getWorlds().get(0).getWorldFolder(), "arena.yml");
		if (!arenaFile.exists())
		{
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

		PluginManager pluginManager = getServer().getPluginManager();

		pluginManager.registerEvents(new ConnectionsListener(this), this);
		pluginManager.registerEvents(new GameListener(this), this);
		pluginManager.registerEvents(new SystemListener(this), this);
		pluginManager.registerEvents(new PreStartInteractionsListener(), this);
		pluginManager.registerEvents(new PowerupsListener(this), this);

		final CommandListener command = new CommandListener(this);
		this.getCommand("forcestop").setExecutor(command);
		this.getCommand("powerup").setExecutor(command);

		this.getCommand("classe").setExecutor(new ClassSelectionCommand());
		this.getCommand("preview").setExecutor(new ClassPreviewCommand());

		this.getCommand("elo").setExecutor(command);


		World world = getServer().getWorlds().get(0);
		world.setGameRuleValue("doDaylightCycle", "false");
		world.setFullTime(arenaConfig.getLong("map.hubDayTime", 6000));


		timer = new CountdownTimer(this);
		game = new HeroBattleGame();
		gameTimer = new GameTimer(this, this.getArenaConfig().getInt("map.gameTime"));
		classManager = new ClassManager(this);
		scoreboardManager = new ScoreboardManager(this);
		powerupManager = new PowerupManager(this);
		tutorialDisplayer = new TutorialDisplayer(this);

		Gui.init(this);
		GuiUtils.init();

		SamaGamesAPI.get().getGameManager().registerGame(game);

		// /reload support
		addOnlinePlayers();
	}

	public void onDisable()
	{
		Gui.exit();
	}

	// For local debugging purposes only (/rl)
	public void addOnlinePlayers()
	{
		getServer().getOnlinePlayers().forEach(game::handleLogin);
	}

	public boolean isTestServer()
	{
		return SamaGamesAPI.get().getServerName().startsWith("TestServer_");
	}

	public HeroBattleGame getGame()
	{
		return game;
	}

	public CountdownTimer getTimer()
	{
		return timer;
	}

	public GameTimer getGameTimer()
	{
		return gameTimer;
	}

	public ClassManager getClassManager()
	{
		return classManager;
	}

	public ScoreboardManager getScoreboardManager()
	{
		return scoreboardManager;
	}

	public Configuration getArenaConfig()
	{
		return arenaConfig;
	}

	public PowerupManager getPowerupManager()
	{
		return powerupManager;
	}

	public TutorialDisplayer getTutorialDisplayer()
	{
		return tutorialDisplayer;
	}

	public AbstractShopsManager getShopManager()
	{
		return SamaGamesAPI.get().getShopsManager(getGame().getGameCodeName());
	}

	public HeroBattlePlayer getGamePlayer(UUID uuid)
	{
		return getGame().getPlayer(uuid);
	}

	public HeroBattlePlayer getGamePlayer(Player player)
	{
		return getGame().getPlayer(player.getUniqueId());
	}
}
