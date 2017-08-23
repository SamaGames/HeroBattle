package net.lnfinity.HeroBattle.powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.powerups.powerups.BlindnessPowerup;
import net.lnfinity.HeroBattle.powerups.powerups.HealPowerup;
import net.lnfinity.HeroBattle.powerups.powerups.InvulnerabilityPowerup;
import net.lnfinity.HeroBattle.powerups.powerups.NauseaPowerup;
import net.lnfinity.HeroBattle.powerups.powerups.NightVisionPowerup;
import net.lnfinity.HeroBattle.powerups.powerups.PercentageDividedPowerup;
import net.lnfinity.HeroBattle.powerups.powerups.PercentageIncreasedPowerup;
import net.lnfinity.HeroBattle.powerups.powerups.PlayersSwapPowerup;
import net.lnfinity.HeroBattle.powerups.powerups.PowerPowerup;
import net.lnfinity.HeroBattle.powerups.powerups.ResetToolCooldownPowerup;
import net.lnfinity.HeroBattle.powerups.powerups.SpeedPowerup;
import net.lnfinity.HeroBattle.powerups.powerups.ToastPowerup;
import net.lnfinity.HeroBattle.powerups.powerups.TripleJumpPowerup;
import net.lnfinity.HeroBattle.powerups.powerups.ZeroCooldownPowerup;
import net.lnfinity.HeroBattle.powerups.powerups.ZeroPercentagePowerup;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
public class PowerupManager
{
	public final static int INVERSE_PROBABILITY_OF_SPAWN_PER_TICK = 750;
	private final static long DELAY_UNSPAWN_POSITIVE_POWERUP = 60 * 20l;
	private final static long DELAY_UNSPAWN_NEGATIVE_POWERUP = 45 * 20l;

	private final HeroBattle p;

	private final PowerupSpawner spawner;

	private final List<Powerup> powerups = new ArrayList<>();
	private final Set<ActivePowerup> activePowerups = new HashSet<>();
	private double totalWeight = 0d;

	private final List<Location> locations;

	private final Random random = new Random();


	public PowerupManager(final HeroBattle plugin)
	{
		p = plugin;
		spawner = new PowerupSpawner(p);

		locations = new ArrayList<>(p.getProperties().getPowerupSpawns());


		/** **  Powerups registry  ** **/

		registerPowerup(new ToastPowerup());
		registerPowerup(new HealPowerup(p));
		registerPowerup(new PercentageDividedPowerup(p));
		registerPowerup(new InvulnerabilityPowerup(p));
		registerPowerup(new ResetToolCooldownPowerup(p));
		registerPowerup(new ZeroPercentagePowerup(p));
		registerPowerup(new TripleJumpPowerup(p));
		registerPowerup(new ZeroCooldownPowerup());
		registerPowerup(new PowerPowerup(p));
		registerPowerup(new SpeedPowerup());

		registerPowerup(new BlindnessPowerup());
		registerPowerup(new NauseaPowerup());
		registerPowerup(new PercentageIncreasedPowerup(p));
		registerPowerup(new PlayersSwapPowerup(p));

		final long worldTime = p.getProperties().getGameDayTime() % 24000;
		if (worldTime >= 13500 && worldTime < 22500 && !plugin.getProperties().getPermanentNightVision())
		{
			registerPowerup(new NightVisionPowerup());
		}
	}


	/**
	 * Tries to spawn a powerup. Nothing will be done if there isn't any location available.
	 */
	public void spawnRandomPowerup()
	{
		if (locations.size() == 0) return; // There isn't any location available.


		final Location location = locations.get(random.nextInt(locations.size()));

		// Weighted random choice of the powerup
		Powerup powerup = null;
		double randomIndex = random.nextDouble() * totalWeight;
		for (final Powerup testedPowerup : powerups)
		{
			randomIndex -= testedPowerup.getWeight();
			if (randomIndex <= 0.0d)
			{
				powerup = testedPowerup;
				break;
			}
		}

		// Should never happens, but just to be sure
		if (powerup == null)
		{
			throw new RuntimeException("Cannot find a powerup to spawn");
		}


		// The chosen location is removed from the list, to avoid two powerups at the same place
		locations.remove(location);


		final ActivePowerup activePowerup = new ActivePowerup(p, location, powerup);
		activePowerups.add(activePowerup);

		activePowerup.spawn();


		final long unspawnDelay = powerup instanceof PositivePowerup ?
				DELAY_UNSPAWN_POSITIVE_POWERUP : DELAY_UNSPAWN_NEGATIVE_POWERUP;

		Bukkit.getScheduler().runTaskLater(p, () -> {
			if (activePowerup.isAlive())
			{
				unspawnPowerup(activePowerup, false);
			}
		}, unspawnDelay);
	}

	/**
	 * Removes an active powerup.
	 *
	 * @param powerup The active powerup to remove.
	 * @param got     If true the powerup is removed because someone picked-up it.
	 */
	private void unspawnPowerup(final ActivePowerup powerup, final boolean got)
	{
		powerup.remove(got);
		locations.add(powerup.getLocation());
		activePowerups.remove(powerup);
	}


	/**
	 * When someone pickup a powerup, executes the good action.
	 *
	 * @param itemPicked The item picked up.
	 * @param player     The player who picked up the item.
	 */
	public void onPowerupPickup(final Item itemPicked, final Player player)
	{
		final String powerupUUID = itemPicked.getItemStack().getItemMeta().getDisplayName();
		ActivePowerup activePowerup = null;

		// Powerup lookup
		for (final ActivePowerup powerup : activePowerups)
		{
			if (powerup.getActivePowerupUniqueID().toString().equals(powerupUUID))
			{
				activePowerup = powerup;
				break;
			}
		}

		if (activePowerup == null) return;  // Not a powerup


		activePowerup.getPowerup().onPickup(player, itemPicked.getItemStack());
		unspawnPowerup(activePowerup, true);


		HeroBattle.get().getGame().increaseStat(player.getUniqueId(), "powerup_taken", 1);
	}


	public void registerPowerup(final Powerup powerup)
	{
		powerups.add(powerup);
		totalWeight += powerup.getWeight();
	}


	public PowerupSpawner getSpawner()
	{
		return spawner;
	}
}
