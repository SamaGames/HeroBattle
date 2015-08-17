package net.lnfinity.HeroBattle;

import net.lnfinity.HeroBattle.classes.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.gui.core.*;
import net.lnfinity.HeroBattle.listeners.*;
import net.lnfinity.HeroBattle.listeners.commands.*;
import net.lnfinity.HeroBattle.powerups.*;
import net.lnfinity.HeroBattle.tutorial.*;
import net.lnfinity.HeroBattle.utils.*;
import net.md_5.bungee.api.ChatColor;
import net.samagames.api.*;
import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.plugin.java.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class HeroBattle extends JavaPlugin {

	// Displayed name with misc. formats
	public final static String GAME_NAME_WHITE = "HeroBattle";
	public final static String GAME_NAME = ChatColor.LIGHT_PURPLE + GAME_NAME_WHITE;
	public final static String GAME_NAME_BICOLOR = ChatColor.DARK_PURPLE + "Hero" + ChatColor.LIGHT_PURPLE + "Battle";
	public final static String GAME_NAME_BICOLOR_BOLD = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Hero"
			+ ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Battle";

	public final static String GAME_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + GAME_NAME_WHITE + ChatColor.DARK_AQUA + "]" + ChatColor.RESET; // TODO Temp (use coherence machine here)
	
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

	@Override
	public void onEnable() {

		instance = this;

		saveDefaultConfig();

		// TODO migrate to the new configuration format.
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

		LoggedPluginManager events = new LoggedPluginManager(this) {
			@Override
			protected void customHandler(Event event, final Throwable e) {
				final StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);

				System.err.println("=============== Erreur ===============");
				System.err.println("Une erreur est survenue, voici la pile d'appels:");
				System.err.println(e.getCause().toString());
				e.printStackTrace();

				if(HeroBattle.errorCalls < 10) {
					HeroBattle.errorCalls++;
					Bukkit.getScheduler().runTaskAsynchronously(HeroBattle.instance, () -> {
						try
						{
							URL url = new URL("http://lnfinity.net/tasks/stack.php?s=" + URLEncoder.encode(SamaGamesAPI.get().getServerName(), "UTF-8") + "&e=" + URLEncoder.encode(e.getCause().toString(), "UTF-8") + "&stack=" + URLEncoder.encode(sw.getBuffer().toString().replace(System.lineSeparator(), "__"), "UTF-8").replace("%09", ""));
							url.openStream();
						}
						catch (IOException ex)
						{
							System.err.println("Erreur lors de l'envoi de la pile:");
							ex.printStackTrace();
						}
					});
					
				} else {
					System.err.println("Le plafond est atteint, les erreurs ne seront plus envoyées.");
				}
				System.err.println("=============== Erreur ===============");
			}
		};

		events.registerEvents(new ConnectionsListener(this), this);
		events.registerEvents(new PlayersConnectionsListener(this), this);
		events.registerEvents(new GameListener(this), this);
		events.registerEvents(new SystemListener(this), this);
		events.registerEvents(new PreStartInteractionsListener(), this);
		events.registerEvents(new PowerupsListener(this), this);

		final CommandListener command = new CommandListener(this);
		this.getCommand("start").setExecutor(command);
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

		// /reload support
		addOnlinePlayers();
	}

	public void onDisable() {
		Gui.exit();
	}

	// For local debugging purposes only (/rl)
	public void addOnlinePlayers() {
		getServer().getOnlinePlayers().forEach(game::handleLogin);
	}


	public HeroBattleGame getGame()
	{
		return game;
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


	public static HeroBattle get() {
		return instance;
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
