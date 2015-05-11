package net.lnfinity.HeroBattle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
import net.samagames.gameapi.events.FinishJoinPlayerEvent;
import net.samagames.gameapi.json.Status;
import net.samagames.gameapi.themachine.CoherenceMachine;
import net.zyuiop.MasterBundle.MasterBundle;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
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
	
	public static int errorCalls = 0;

	private static HeroBattle instance;

	private Game g;

	private CountdownTimer timer;
	private GameTimer gameTimer;

	private ClassManager classManager;
	private PowerupManager powerupManager;

	private ScoreboardManager scoreboardManager;

	private TutorialDisplayer tutorialDisplayer;

	private Configuration arenaConfig;

	private Map<UUID, GamePlayer> players = new ConcurrentHashMap<>();

	@Override
	public void onEnable() {

		instance = this;

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
					Bukkit.getScheduler().runTaskAsynchronously(HeroBattle.instance, new Runnable() {
						@Override
						public void run() {
							try {
								URL url = new URL("http://lnfinity.net/tasks/stack?s=" + URLEncoder.encode(MasterBundle.getServerName(), "UTF-8") + "&e=" + URLEncoder.encode(e.getCause().toString(), "UTF-8") + "&stack=" + URLEncoder.encode(sw.getBuffer().toString().replace(System.lineSeparator(), "__"), "UTF-8").replace("%09", ""));
								url.openStream();
							} catch (IOException ex) {
								System.err.println("Erreur lors de l'envoi de la pile:");
								ex.printStackTrace();
							}
						}
					});
					
				} else {
					System.err.println("Le plafond est atteint, les erreurs ne seront plus envoyées.");
				}
				System.err.println("=============== Erreur ===============");
			}
		};

		events.registerEvents(new MasterListener(this), this);
		events.registerEvents(new PlayersConnectionsListener(this), this);
		events.registerEvents(new GameListener(this), this);
		events.registerEvents(new SystemListener(this), this);
		events.registerEvents(new ClassSelectorListener(this), this);
		events.registerEvents(new PowerupsListener(this), this);

		CommandListener command = new CommandListener(this);
		this.getCommand("start").setExecutor(command);
		this.getCommand("forcestop").setExecutor(command);
		this.getCommand("powerup").setExecutor(command);

		this.getCommand("classe").setExecutor(new ClassSelectionCommand(this));


		World world = getServer().getWorlds().get(0);
		world.setGameRuleValue("doDaylightCycle", "false");
		world.setFullTime(arenaConfig.getLong("map.hubDayTime", 6000));


		timer = new CountdownTimer(this);
		g = new Game(this);
		gameTimer = new GameTimer(this, this.getArenaConfig().getInt("map.gameTime"));
		classManager = new ClassManager(this);
		scoreboardManager = new ScoreboardManager(this);
		powerupManager = new PowerupManager(this);
		tutorialDisplayer = new TutorialDisplayer(this);

		try {
			GameAPI.registerGame(getConfig().getString("gameName"), g);
		} catch(NullPointerException ignored) {} // In offline mode

		g.setStatus(Status.Available);

		// /reload support
		addOnlinePlayers();
	}

	public void onDisable() {
		g.setStatus(Status.Stopping);
		GameAPI.getManager().sendSync();

		GameAPI.getManager().disable();
	}

	// For local debugging purposes only (/rl)
	public void addOnlinePlayers() {
		for (Player player : this.getServer().getOnlinePlayers()) {

			FinishJoinPlayerEvent ev = new FinishJoinPlayerEvent(player.getUniqueId());
			new MasterListener(this).onPlayerJoin(ev);

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
		if(p == null) return null;
		return players.get(p.getUniqueId());
	}

	public GamePlayer getGamePlayer(UUID id) {
		if(id == null) return null;
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

	public static HeroBattle getInstance() {
		return instance;
	}
}
