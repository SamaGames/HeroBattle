package net.lnfinity.HeroBattle;

import net.lnfinity.HeroBattle.classes.ClassManager;
import net.lnfinity.HeroBattle.game.HeroBattleGame;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import net.lnfinity.HeroBattle.game.HeroBattleProperties;
import net.lnfinity.HeroBattle.game.ScoreboardManager;
import net.lnfinity.HeroBattle.gui.core.Gui;
import net.lnfinity.HeroBattle.gui.core.GuiUtils;
import net.lnfinity.HeroBattle.listeners.GameListener;
import net.lnfinity.HeroBattle.listeners.PowerupsListener;
import net.lnfinity.HeroBattle.listeners.PreStartInteractionsListener;
import net.lnfinity.HeroBattle.listeners.SystemListener;
import net.lnfinity.HeroBattle.listeners.commands.ClassPreviewCommand;
import net.lnfinity.HeroBattle.listeners.commands.ClassSelectionCommand;
import net.lnfinity.HeroBattle.listeners.commands.CommandListener;
import net.lnfinity.HeroBattle.powerups.PowerupManager;
import net.lnfinity.HeroBattle.tutorial.TutorialDisplayer;
import net.lnfinity.HeroBattle.utils.GameTimer;
import net.md_5.bungee.api.ChatColor;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.shops.AbstractShopsManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.logging.Level;

/*
 * This file is part of HeroBattle.
 *
 * HeroBattle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HeroBattle is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HeroBattle.  If not, see <http://www.gnu.org/licenses/>.
 */
public class HeroBattle extends JavaPlugin
{
	// Displayed name with misc. formats
	public final static String GAME_NAME_WHITE = "HeroBattle";
	public final static String GAME_NAME = ChatColor.LIGHT_PURPLE + GAME_NAME_WHITE;
	public final static String GAME_NAME_BICOLOR = ChatColor.DARK_PURPLE + "Hero" + ChatColor.LIGHT_PURPLE + "Battle";
	public final static String GAME_NAME_BICOLOR_BOLD = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Hero"
			+ ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Battle";

	public final static String GAME_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "HB" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET; // TODO Temp (use coherence machine here)

	private static HeroBattle instance;

	private HeroBattleProperties properties;
	private HeroBattleGame game;

	private GameTimer gameTimer;

	private ClassManager classManager;
	private PowerupManager powerupManager;

	private ScoreboardManager scoreboardManager;
	private TutorialDisplayer tutorialDisplayer;

	public static HeroBattle get()
	{
		return instance;
	}

	@Override
	public void onEnable()
	{
		instance = this;

		try
		{
			properties = new HeroBattleProperties();
		}
		catch (HeroBattleProperties.InvalidConfigurationException e)
		{
			getLogger().log(Level.SEVERE, "Invalid configuration in " + e.getFile() + "#" + e.getKey(), e);

			if(e.isFatal())
			{
				getLogger().log(Level.SEVERE, "Cannot run the game with such a malformed configuration. Aborting.");
				Bukkit.shutdown();
			}
		}

		PluginManager pluginManager = getServer().getPluginManager();

        // Outdated (delete ?)
		//pluginManager.registerEvents(new ConnectionsListener(this), this);
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
		world.setFullTime(getProperties().getHubDayTime());


		game = new HeroBattleGame();
		gameTimer = new GameTimer(this, getProperties().getGameDuration());
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

	public PowerupManager getPowerupManager()
	{
		return powerupManager;
	}

	public TutorialDisplayer getTutorialDisplayer()
	{
		return tutorialDisplayer;
	}

	public HeroBattleProperties getProperties()
	{
		return properties;
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
