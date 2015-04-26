package net.lnfinity.HeroBattle.utils;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.GamePlayer;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public final class Utils {

	private static Random rnd = new Random();
	private static DecimalFormat bigNumbersFormat;
	private static HttpURLConnection httpConn;

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
			Location location = new Location(p.getServer().getWorlds().get(0), Double.valueOf(coords[0]) + 0.5,
					Double.valueOf(coords[1]), Double.valueOf(coords[2]) + 0.5);

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

	public static String formatNumber(double number) {
		DecimalFormat bigNumbersFormat = new DecimalFormat("###,###,###");
		bigNumbersFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.FRANCE));

		return bigNumbersFormat.format(number).replace(" ", " ");
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
		if (player.getPlayerClass() == null) {
			return "";
		}

		return heartsToString(player.isPlaying() ? player.getLives() : 0, player.getPlayerClass().getLives());
	}

	public static String heartsToString(GamePlayer player, boolean transition) {
		if (player.getPlayerClass() == null) {
			return "";
		}
		if (transition == false) {
			return heartsToString(player.getLives(), player.getPlayerClass().getLives());
		} else {
			return heartsToString(player.getLives() + 1, player.getPlayerClass().getLives());
		}
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

	public static double logb(double a, double b) {
		return Math.log(a) / Math.log(b);
	}

	public static List<String> getToolDescription(String desc) {
		List<String> lines = new ArrayList<String>();
		String[] words = desc.split(" ");
		int line = 0;
		lines.add(line, "");
		for (int k = 0; k < words.length; k++) {
			int chars = new String(lines.get(line) + " " + words[k]).length() - countColors(lines.get(line) + " " + words[k]);
			if(chars >= 45) {
				line++;
				lines.add(line, ChatColor.GRAY + words[k]);
			} else {
				if(lines.equals("")) {
					lines.set(line, ChatColor.GRAY + words[k]);
				} else {
					lines.set(line, lines.get(line) + " " + words[k]);
				}
			}
		}
		lines.set(0, lines.get(0).trim());
		for(int k = 0; k < lines.size(); k++) {
			lines.set(k, lines.get(k).trim());
		}
		return lines;
	}
	
	private static int countColors(String str) {
		int count = 0;
		for(int i = 0; i < str.length(); i++) {
			if(str.charAt(i) == '§') {
				count++;
			}
		}
		return count;
	}

	public static String tableToString(StackTraceElement[] table, String delimiter) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < table.length; i++) {
		   result.append(table[i]);
		   if(i + 1 != table.length) {
			   result.append(delimiter);
		   }
		}
		return result.toString();
	}

	static {
		DecimalFormat bigNumbersFormat = new DecimalFormat("###,###,###");
		bigNumbersFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.FRANCE));
	}
}
