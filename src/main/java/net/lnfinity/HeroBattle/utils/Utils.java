package net.lnfinity.HeroBattle.utils;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public final class Utils
{

	private static Random rnd = new Random();
	private static DecimalFormat bigNumbersFormat;

	static
	{
		bigNumbersFormat = new DecimalFormat("###,###,###");
		bigNumbersFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.FRANCE));
	}


	/**
	 * Returns the given {@code charSequence} if it isn't {@code null} nor empty; returns {@code
	 * defaultValue} else.
	 *
	 * @param charSequence The char sequence.
	 * @param defaultValue The default value returned if {@code charSequence} is {@code null} or
	 *                     empty.
	 *
	 * @return The sequence if not {@code null} nor empty; the default value else.
	 */
	public static String toStringIfNotEmpty(CharSequence charSequence, CharSequence defaultValue)
	{
		return charSequence != null && charSequence.length() != 0 ? charSequence.toString() : defaultValue.toString();
	}


	/**
	 * Converts a string (in the config file) to a Location object.
	 *
	 * @param locationInConfig A string; format "x;y;z" or "x;y;z;yaw" or "x;y;z;yaw;pitch".
	 *
	 * @return The Location object, for the main world (first one).
	 * @throws IllegalArgumentException if the format is not good.
	 */
	public static Location stringToLocation(HeroBattle p, String locationInConfig)
	{
		Validate.notNull(locationInConfig, "The location cannot be null!");

		String[] coords = locationInConfig.split(";");
		if (coords.length < 3)
		{
			throw new IllegalArgumentException("Invalid location: " + locationInConfig);
		}

		try
		{
			Location location = new Location(p.getServer().getWorlds().get(0), Double.valueOf(coords[0]) + 0.5,
					Double.valueOf(coords[1]), Double.valueOf(coords[2]) + 0.5);

			if (coords.length >= 4)
			{
				location.setYaw(Float.valueOf(coords[3]));

				if (coords.length >= 5)
				{
					location.setPitch(Float.valueOf(coords[4]));
				}
			}

			return location;
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException("Invalid location (NaN!): " + locationInConfig);
		}
	}

	public static int randomNumber(int min, int max)
	{
		return rnd.nextInt(max - min + 1) + min;
	}

	public static String formatNumber(double number)
	{
		return bigNumbersFormat.format(number).replace(" ", " ");
	}

	public static Location blockLocation(Location loc)
	{
		Location blockLocation = loc.clone();
		blockLocation.setX(blockLocation.getBlockX() + 0.5);
		blockLocation.setY(blockLocation.getBlockY() + 0.5);
		blockLocation.setZ(blockLocation.getBlockZ() + 0.5);
		return blockLocation;
	}

	public static Location roundLocation(Location loc)
	{
		return new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public static double distance(Location loc1, Location loc2)
	{
		return Math.sqrt((loc1.getX() - loc2.getX()) * (loc1.getX() - loc2.getX()) + (loc1.getY() - loc2.getY())
				* (loc1.getY() - loc2.getY()) + (loc1.getZ() - loc2.getZ()) * (loc1.getZ() - loc2.getZ()));
	}

	public static String heartsToString(HeroBattlePlayer player)
	{
		return heartsToString(player, false, false);
	}

	/**
	 * Returns a representation of the hearts of a player.
	 *
	 * @param player          The player
	 * @param transitionBegin True if this is the beginning of a transition on the death screen
	 * @param transitionEnd   True if this is the end of a transition on the death screen
	 *
	 * @return
	 */
	public static String heartsToString(HeroBattlePlayer player, boolean transitionBegin, boolean transitionEnd)
	{
		if (player.getPlayerClass() == null)
		{
			return "";
		}

		Integer displayedLives;
		Integer displayedAdditionalLives;
		Integer displayedLostLives;
		Integer displayedLostAdditionalLives = 0;

		if (!transitionBegin)
		{
			displayedLives = player.getLives();
			displayedAdditionalLives = player.getAdditionalLives();
			displayedLostLives = player.getPlayerClass().getLives() - displayedLives;

			// In this case the player just lost an additional live, because he lost a live when
			// this is displayed with one of the transitions to true.
			if (transitionEnd && displayedLostLives == 0)
			{
				displayedLostAdditionalLives = 1;
			}
		}
		else
		{
			if (player.getLives() == player.getPlayerClass().getLives())
			{
				// The player just lost an additional live
				displayedLives = player.getLives();
				displayedAdditionalLives = player.getAdditionalLives() + 1;
				displayedLostLives = 0;
			}
			else
			{
				// No additional live here
				displayedLives = player.getLives() + 1;
				displayedAdditionalLives = 0;
				displayedLostLives = player.getPlayerClass().getLives() - displayedLives;
			}
		}


		return (displayedLives != 0 ? ChatColor.RED + getNHearts(displayedLives) : "")
				+ (displayedLostLives != 0 ? ChatColor.GRAY + getNHearts(displayedLostLives) : "")
				+ (displayedAdditionalLives != 0 ? ChatColor.GOLD + getNHearts(displayedAdditionalLives) : "")
				+ (displayedLostAdditionalLives != 0 ? ChatColor.GRAY + getNHearts(displayedLostAdditionalLives) : "");
	}

	private static String getNHearts(int n)
	{
		String hearts = "";

		for (int i = 0; i < n; i++)
		{
			hearts += "❤";
		}

		return hearts;
	}

	public static double logb(double a, double b)
	{
		return Math.log(a) / Math.log(b);
	}

	public static List<String> getToolDescription(String desc)
	{
		List<String> lines = new ArrayList<>();
		String[] words = desc.split(" ");
		int line = 0;
		lines.add(line, "");

		for (String word : words)
		{
			int chars = (lines.get(line) + " " + word).length() - countColors(lines.get(line) + " " + word);

			if (chars >= 45)
			{
				line++;
				lines.add(line, ChatColor.GRAY + word);
			}

			else
			{
				if (lines.get(line).equals(""))
				{
					lines.set(line, ChatColor.GRAY + word);
				}
				else
				{
					lines.set(line, lines.get(line) + " " + word);
				}
			}
		}

		for (int k = 0; k < lines.size(); k++)
		{
			lines.set(k, lines.get(k).trim());
		}

		return lines;
	}

	private static int countColors(String str)
	{
		int count = 0;
		for (int i = 0; i < str.length(); i++)
		{
			if (str.charAt(i) == '§')
			{
				count++;
			}
		}
		return count;
	}

	public static String tableToString(StackTraceElement[] table, String delimiter)
	{
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < table.length; i++)
		{
			result.append(table[i]);
			if (i + 1 != table.length)
			{
				result.append(delimiter);
			}
		}

		return result.toString();
	}

	public static String getRandomAvailableTeamName()
	{
		do
		{
			String teamName = rnd.nextInt(99999999) + "";
			if (HeroBattle.get().getScoreboardManager().getScoreboard().getTeam(teamName) == null)
			{
				return teamName;
			}
		} while (true);
	}

	public static String getPlayerColor(Player player)
	{
		/*if(MasterBundle.isDbEnabled) {
			return PermissionsBukkit.getPrefix(PermissionsAPI.permissionsAPI.getUser(player.getUniqueId()));
		} else {*/
		return ChatColor.getLastColors(player.getDisplayName().replaceAll(ChatColor.RESET.toString(), ""));
		/*}*/
	}
}
