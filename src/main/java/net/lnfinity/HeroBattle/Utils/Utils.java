package net.lnfinity.HeroBattle.Utils;

import java.util.Random;

import net.lnfinity.HeroBattle.Game.GamePlayer;
import net.lnfinity.HeroBattle.HeroBattle;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Utils {

	static Random rnd = new Random();

	/**
	 * Converts a string (in the config file) to a Location object.
	 * 
	 * @param locationInConfig
	 *            A string; format "x;y;z" or "x;y;z;yaw" or "x;y;z;yaw;pitch".
	 * @return The Location object, for the main world (first one).
	 * 
	 * @throws IllegalArgumentException
	 *             if the format is not good.
	 */
	public static Location stringToLocation(HeroBattle p, String locationInConfig) {
		String[] coords = locationInConfig.split(";");
		if (coords.length < 3) {
			throw new IllegalArgumentException("Invalid location: " + locationInConfig);
		}

		try {
			Location location = new Location(p.getServer().getWorlds().get(0), Double.valueOf(coords[0]),
					Double.valueOf(coords[1]), Double.valueOf(coords[2]));

			if (coords.length >= 4) {
				location.setYaw(Float.valueOf(coords[3]));

				if (coords.length >= 5) {
					location.setPitch(Float.valueOf(coords[4]));
				}
			}

			return location;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid location (NaN!): " + locationInConfig);
		}
	}

	public static int randomNumber(int min, int max) {
		return rnd.nextInt(max - min + 1) + min;
	}

	public static Location blockLocation(Location loc) {
		Location blockLocation = loc.clone();
		blockLocation.setX(blockLocation.getBlockX() + 0.5);
		blockLocation.setY(blockLocation.getBlockY() + 0.5);
		blockLocation.setZ(blockLocation.getBlockZ() + 0.5);
		return blockLocation;
	}

	public static Location roundLocation(Location loc) {
		return new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public static double distance(Location loc1, Location loc2) {
		return Math.sqrt((loc1.getX() - loc2.getX()) * (loc1.getX() - loc2.getX()) + (loc1.getY() - loc2.getY())
				* (loc1.getY() - loc2.getY()) + (loc1.getZ() - loc2.getZ()) * (loc1.getZ() - loc2.getZ()));
	}

	public static String heartsToString(GamePlayer player) {
		if(player.getPlayerClass() == null) {
			return "";
		}

		return heartsToString(player.isPlaying() ? player.getLives() : 0, player.getPlayerClass().getLives());
	}

	public static String heartsToString(int hearts, int maxHearts) {
		String str = ChatColor.RED + "";
		for (int i = 1; i <= maxHearts; i++) {
			if (i <= hearts) {
				str += "❤";
			} else {
				str += ChatColor.GRAY + "❤";
			}
		}
		return str;
	}
}
