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
		Location tempLoc = loc.clone();
		tempLoc.setX(Math.floor(tempLoc.getX()) + 0.5);
		tempLoc.setY(Math.floor(tempLoc.getY()) + 0.5);
		tempLoc.setZ(Math.floor(tempLoc.getZ()) + 0.5);
		return tempLoc;
	}

	public static Location roundLocation(Location loc) {
		int x = (int) Math.floor(loc.getX());
		int y = (int) Math.floor(loc.getY());
		int z = (int) Math.floor(loc.getZ());
		return new Location(loc.getWorld(), x, y, z);
	}

	public static double distance(Location loc1, Location loc2) {
		return Math.sqrt((loc1.getX() - loc2.getX()) * (loc1.getX() - loc2.getX()) + (loc1.getY() - loc2.getY())
				* (loc1.getY() - loc2.getY()) + (loc1.getZ() - loc2.getZ()) * (loc1.getZ() - loc2.getZ()));
	}

	public static String heartsToString(GamePlayer player) {
		if(player.getPlayerClass() == null) {
			return "";
		}

		return heartsToString(player.getLives(), player.getPlayerClass().getLives());
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
