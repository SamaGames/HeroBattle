package net.lnfinity.HeroBattle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HeroBattle extends JavaPlugin {

	private HBTimer timer;
	private HBListener listener;
	Map<UUID, HBPlayer> players = new HashMap<UUID, HBPlayer>();

	// Global strings
	public static String NAME = ChatColor.DARK_PURPLE + "["
			+ ChatColor.LIGHT_PURPLE + "HeroBattle" + ChatColor.DARK_PURPLE
			+ "] " + ChatColor.RESET;

	public HeroBattle() {

	}

	@Override
	public void onEnable() {
		saveConfig();
		listener = new HBListener(this);
		timer = new HBTimer(this);
		getServer().getPluginManager().registerEvents(listener, this);
		for (Player player : getServer().getOnlinePlayers()) {
			players.put(player.getUniqueId(), new HBPlayer());
		}
	}

	public HBTimer getTimer() {
		return timer;
	}

	public HBListener getListener() {
		return listener;
	}

	@Override
	public void onDisable() {

	}

	public void addHBPlayer(UUID id) {
		players.put(id, new HBPlayer());
	}

	public void removeHBPlayer(UUID id) {
		players.remove(id);
	}

	public HBPlayer getHBPlayer(UUID id) {
		return players.get(id);
	}

	public int getPlayerCount() {
		int count = 0;
		for (Player player : getServer().getOnlinePlayers()) {
			count++;
		}
		return count;
	}
}
