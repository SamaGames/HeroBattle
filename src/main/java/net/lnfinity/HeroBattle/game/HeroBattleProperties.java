/*
 * Copyright or © or Copr. AmauryCarrade (2015)
 * 
 * http://amaury.carrade.eu
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package net.lnfinity.HeroBattle.game;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.utils.Utils;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.IGameProperties;
import org.bukkit.Location;

import java.util.List;


public class HeroBattleProperties
{
	private IGameProperties properties;

	// Rewards
	private final Integer coinsPerKill;
	private final Integer coinsPerAssist;
	private final Integer coinsPerVictory;
	private final Integer starsPerVictory;
	private final Integer coinsIfFirstRanked;
	private final Integer coinsIfSecondRanked;
	private final Integer coinsIfThirdRanked;

	// Locations
	private final Location hub;
	private final List<Location> playerSpawns;
	private final List<Location> powerupSpawns;
	private final List<Location> tutorialPOV;
	private final List<Location> teleportationPortalsDestinations;

	private final Double bottomHub;
	private final Double bottomGame;

	// Times of the day
	private final Long hubDayTime;
	private final Long gameDayTime;

	// Toxicities
	private final Boolean toxicWater;
	private final Boolean toxicLava;

	// Cosmetics
	private final Boolean marioPortalsTeleportationsSound;
	private final Boolean permanentNightVision;


	public HeroBattleProperties() throws InvalidConfigurationException
	{
		this.properties = SamaGamesAPI.get().getGameManager().getGameProperties();


		/* **  Rewards  ** */

		coinsPerKill        = getConfigInt("rewards.coins.per-kill", 5);
		coinsPerAssist      = getConfigInt("rewards.coins.per-assist", 3);
		coinsPerVictory     = getConfigInt("rewards.coins.per-victory", 20);
		starsPerVictory     = getConfigInt("rewards.stars.per-victory", 1);

		coinsIfFirstRanked  = getConfigInt("rewards.coins.if-first-ranked", 10);
		coinsIfSecondRanked = getConfigInt("rewards.coins.if-second-ranked", 6);
		coinsIfThirdRanked  = getConfigInt("rewards.coins.if-third-ranked", 4);


		/* **  Locations  ** */


		hub = getArenaLocation("locations.hub", true);

		playerSpawns = getArenaLocations("locations.player-spawns", true);
		powerupSpawns = getArenaLocations("locations.powerup-spawns", false);

		teleportationPortalsDestinations = getArenaLocations("locations.teleportation-portals-destinations", false);
		if(teleportationPortalsDestinations.size() == 0)
		{
			HeroBattle.get().getLogger().info("No teleportation portal locations set in arena.yml. Feature disabled.");
		}

		tutorialPOV = getArenaLocations("locations.tutorial-points-of-views", false);
		if(tutorialPOV.size() != 0 && tutorialPOV.size() < 4)
		{
			tutorialPOV.clear();
			throw new InvalidConfigurationException("If set, at least 4 tutorial points of view are required (and the points after the 4th will be ignored).", "arena.json", "locations.tutorial-points-of-views", false);
		}

		bottomHub = getArenaDouble("locations.bottom-altitude.waiting-lobby", 0);
		bottomGame = getArenaDouble("locations.bottom-altitude.in-game", 0);


		/* **  Times of the day  ** */

		hubDayTime  = getArenaLong("times-of-day.waiting-lobby", 6000l);
		gameDayTime = getArenaLong("times-of-day.in-game", 6000l);


		/* **  Toxicities  ** */
		toxicWater = getArenaBoolean("toxicities.water", false);
		toxicLava = getArenaBoolean("toxicities.lava", false);


		/* **  Cosmetics  ** */
		marioPortalsTeleportationsSound = getArenaBoolean("mario-portal-teleportation-sound", true);
		permanentNightVision = getArenaBoolean("permanent-night-vision", false);
	}


	/**
	 * Returns a configuration integer from the `game.json` file.
	 *
	 * @param key The key.
	 * @param defaultValue The default value.
	 *
	 * @return The Integer value found, the default value if not set or invalid.
	 */
	public Integer getConfigInt(String key, Number defaultValue)
	{
		return properties.getOption(key, new JsonPrimitive(defaultValue)).getAsInt();
	}

	/**
	 * Returns a configuration double from the `arena.json` file.
	 *
	 * @param key The key.
	 * @param defaultValue The default value.
	 *
	 * @return The Double value found, the default value if not set or invalid.
	 */
	public Double getArenaDouble(String key, Number defaultValue)
	{
		return properties.getConfig(key, new JsonPrimitive(defaultValue)).getAsDouble();
	}

	/**
	 * Returns a configuration double from the `arena.json` file.
	 *
	 * @param key The key.
	 * @param defaultValue The default value.
	 *
	 * @return The Double value found, the default value if not set or invalid.
	 */
	public Long getArenaLong(String key, Number defaultValue)
	{
		return properties.getConfig(key, new JsonPrimitive(defaultValue)).getAsLong();
	}

	/**
	 * Returns a configuration double from the `arena.json` file.
	 *
	 * @param key The key.
	 * @param defaultValue The default value.
	 *
	 * @return The Double value found, the default value if not set or invalid.
	 */
	public Boolean getArenaBoolean(String key, Boolean defaultValue)
	{
		return properties.getConfig(key, new JsonPrimitive(defaultValue)).getAsBoolean();
	}

	/**
	 * Returns a location from the given key in `arena.json`, using the default world.
	 *
	 * @param key The key.
	 * @param disallowInvalid If true, an {@link IllegalArgumentException} will be thrown if the location is invalid.
	 * @return The location, or {@code null} if invalid with {@code disallowInvalid = false}.
	 */
	public Location getArenaLocation(String key, boolean disallowInvalid)
	{
		return getArenaLocation(properties.getConfig(key, new JsonPrimitive("")), disallowInvalid);
	}

	/**
	 * Returns a location from the given {@link JsonElement}, using the default world.
	 *
	 * @param key The {@link JsonElement}.
	 * @param disallowInvalid If true, an {@link IllegalArgumentException} will be thrown if the location is invalid.
	 * @return The location, or {@code null} if invalid with {@code disallowInvalid = false}.
	 */
	public Location getArenaLocation(JsonElement key, boolean disallowInvalid)
	{
		String rawLocation = key.getAsString();

		if(rawLocation.isEmpty() && disallowInvalid)
			throw new IllegalArgumentException("Invalid location in arena.json: " + key + ": " + rawLocation);

		try
		{
			return Utils.stringToLocation(rawLocation);
		}
		catch (IllegalArgumentException e)
		{
			if(disallowInvalid)
				throw new IllegalArgumentException("Invalid location in arena.json: " + key + ": " + rawLocation);
			else
				return null;
		}
	}

	/**
	 * Returns an immutable list of locations from the given key in `arena.json`. The key MUST
	 * point to a JSON array containing a list of strings.
	 *
	 * @param key The key.
	 * @param disallowEmpty If true, an {@link InvalidConfigurationException} will be thrown if the
	 *                      final list (the JSON list without the invalid entries) is empty.
	 * @return The list.
	 *
	 * @throws InvalidConfigurationException if the final list (the JSON list without the invalid entries) is empty.
	 */
	public List<Location> getArenaLocations(String key, boolean disallowEmpty) throws InvalidConfigurationException
	{
		List<Location> locations;

		JsonArray rawPlayerSpawns = properties.getConfig(key, new JsonArray()).getAsJsonArray();
		if(rawPlayerSpawns.size() == 0)
		{
			locations = ImmutableList.of();
		}
		else
		{
			ImmutableList.Builder<Location> builder = ImmutableList.builder();

			for(JsonElement rawPlayerSpawn : rawPlayerSpawns)
			{
				Location spawn = getArenaLocation(rawPlayerSpawn, false);
				if(spawn != null)
					builder.add(spawn);
			}

			locations = builder.build();
		}

		if(disallowEmpty && locations.size() == 0)
		{
			throw new InvalidConfigurationException("No valid location found: " + key, "arena.json", key);
		}

		return locations;
	}


	public Integer getCoinsPerKill()
	{
		return coinsPerKill;
	}

	public Integer getCoinsPerAssist()
	{
		return coinsPerAssist;
	}

	public Integer getCoinsPerVictory()
	{
		return coinsPerVictory;
	}

	public Integer getStarsPerVictory()
	{
		return starsPerVictory;
	}

	public Integer getCoinsIfFirstRanked()
	{
		return coinsIfFirstRanked;
	}

	public Integer getCoinsIfSecondRanked()
	{
		return coinsIfSecondRanked;
	}

	public Integer getCoinsIfThirdRanked()
	{
		return coinsIfThirdRanked;
	}

	public Location getHub()
	{
		return hub;
	}

	public List<Location> getPlayerSpawns()
	{
		return playerSpawns;
	}

	public List<Location> getPowerupSpawns()
	{
		return powerupSpawns;
	}

	public List<Location> getTutorialPOV()
	{
		return tutorialPOV;
	}

	public Double getBottomHub()
	{
		return bottomHub;
	}

	public Double getBottomGame()
	{
		return bottomGame;
	}

	public Long getHubDayTime()
	{
		return hubDayTime;
	}

	public Long getGameDayTime()
	{
		return gameDayTime;
	}

	public List<Location> getTeleportationPortalsDestinations()
	{
		return teleportationPortalsDestinations;
	}

	public Boolean getMarioPortalsTeleportationsSound()
	{
		return marioPortalsTeleportationsSound;
	}

	public Boolean getPermanentNightVision()
	{
		return permanentNightVision;
	}

	public Boolean isWaterToxic()
	{
		return toxicWater;
	}

	public Boolean isLavaToxic()
	{
		return toxicLava;
	}

	public class InvalidConfigurationException extends Exception
	{
		private String file;
		private String key;
		private boolean fatal;

		/**
		 * Constructs a fatal error.
		 *
		 * @see InvalidConfigurationException#InvalidConfigurationException(String, String, String, boolean)
		 */
		public InvalidConfigurationException(String message, String file, String key)
		{
			this(message, file, key, true);
		}

		/**
		 * @param message Exception message.
		 * @param file The file where the problem is located.
		 * @param key The key where the problem is located.
		 * @param fatal {@code true} if the game cannot run with this error.
		 */
		public InvalidConfigurationException(String message, String file, String key, boolean fatal)
		{
			super(message);

			this.file  = file;
			this.key   = key;
			this.fatal = fatal;
		}

		public String getFile()
		{
			return file;
		}

		public String getKey()
		{
			return key;
		}

		public boolean isFatal()
		{
			return fatal;
		}
	}
}
