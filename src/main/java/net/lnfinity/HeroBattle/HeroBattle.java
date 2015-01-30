package net.lnfinity.HeroBattle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.lnfinity.HeroBattle.Game.Game;
import net.lnfinity.HeroBattle.Game.GamePlayer;
import net.lnfinity.HeroBattle.Listeners.GameListener;
import net.lnfinity.HeroBattle.Listeners.MasterListener;
import net.lnfinity.HeroBattle.Utils.CountdownTimer;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HeroBattle extends JavaPlugin {

	private CountdownTimer timer;
	private Game g;
	Map<UUID, GamePlayer> players = new HashMap<UUID, GamePlayer>();

	// Global strings
	public static String NAME = ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + "HeroBattle"
			+ ChatColor.DARK_PURPLE + "] " + ChatColor.RESET;

	public HeroBattle() {

	}

	@Override
	public void onEnable() {
		saveConfig();
		timer = new CountdownTimer(this);
		g = new Game(this);
		
		MasterListener masterListener = new MasterListener(this);
		getServer().getPluginManager().registerEvents(masterListener, this);
		GameListener gameListener = new GameListener(this);
		getServer().getPluginManager().registerEvents(gameListener, this);
		
		for (Player player : getServer().getOnlinePlayers()) {
			players.put(player.getUniqueId(), new GamePlayer());
		}
		if (getPlayerCount() == 4) {
			timer.restartTimer();
		}
	}

	public CountdownTimer getTimer() {
		return timer;
	}

	@Override
	public void onDisable() {

	}

	public void addHBPlayer(UUID id) {
		players.put(id, new GamePlayer());
	}

	public void removeHBPlayer(UUID id) {
		players.remove(id);
	}

	public GamePlayer getHBPlayer(UUID id) {
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
			if (getHBPlayer(player.getUniqueId()).isPlaying()) {
				count++;
			}
		}
		return count;
	}

	public Game getGame() {
		return g;
	}
}
