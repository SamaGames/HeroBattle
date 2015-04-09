package net.lnfinity.HeroBattle.Powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Powerups.powerups.HealPowerup;
import net.lnfinity.HeroBattle.Powerups.powerups.PowerPowerup;
import net.lnfinity.HeroBattle.Powerups.powerups.ToastPowerup;
import net.lnfinity.HeroBattle.Powerups.powerups.TripleJumpPowerup;
import net.lnfinity.HeroBattle.Powerups.powerups.ZeroCooldownPowerup;
import net.lnfinity.HeroBattle.Powerups.powerups.ZeroPercentagePowerup;
import net.lnfinity.HeroBattle.Utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

public class PowerupManager {

	private HeroBattle p;
	private PowerupSpawner spawner;

	public  final static int  INVERSE_PROBABILITY_OF_SPAWN_PER_TICK = 2000; // 50 = dev value; 2000 = prod value
	private final static long DELAY_UNSPAWN_POSITIVE_POWERUP = 60 * 20l;
	private final static long DELAY_UNSPAWN_NEGATIVE_POWERUP = 45 * 20l;

	private List<Powerup> powerups = new ArrayList<>();
	private List<Location> locations = new ArrayList<>();

	private double totalWeight;

	private Set<ActivePowerup> activePowerups = new HashSet<>();

	private Random random = new Random();

	public PowerupManager(HeroBattle plugin) {
		p       = plugin;
		spawner = new PowerupSpawner(p);

		registerLocations();


		/** **  Powerups registry  ** **/

		registerPowerup(new ToastPowerup());
		registerPowerup(new HealPowerup(plugin));
		registerPowerup(new ZeroPercentagePowerup(plugin));
		registerPowerup(new TripleJumpPowerup(plugin));
		registerPowerup(new ZeroCooldownPowerup());
		registerPowerup(new PowerPowerup(plugin));
		
		/** **  Total weight  ** **/

		totalWeight = 0.0;
		for(Powerup powerup : powerups) {
			totalWeight += powerup.getWeight();
		}
	}


	/**
	 * Tries to spawn a powerup. Nothing will be done if there isn't any location available.
	 */
	public void spawnRandomPowerup() {
		if(locations.size() == 0) return; // There isn't any location available.


		final Location location = locations.get(random.nextInt(locations.size()));

		// Weighted random choice of the powerup
		Powerup powerup = null;
		double randomIndex = random.nextDouble() * totalWeight;
		for(Powerup testedPowerup : powerups) {
			randomIndex -= testedPowerup.getWeight();
			if(randomIndex <= 0.0d) {
				powerup = testedPowerup;
				break;
			}
		}

		// Should never happens, but just to be sure
		if(powerup == null) {
			throw new RuntimeException("Cannot find a powerup to spawn");
		}


		// The chosen location is removed from the list, to avoid two powerups at the same place
		locations.remove(location);


		final ActivePowerup activePowerup = new ActivePowerup(p, location, powerup);
		activePowerups.add(activePowerup);

		activePowerup.spawn();


		long despawnDelay = powerup instanceof PositivePowerup ?
				DELAY_UNSPAWN_POSITIVE_POWERUP : DELAY_UNSPAWN_NEGATIVE_POWERUP;

		Bukkit.getScheduler().runTaskLater(p, new Runnable() {
			@Override
			public void run() {
				if (activePowerup.isAlive()) {
					unspawnPowerup(activePowerup, false);
				}
			}
		}, despawnDelay);
	}

	/**
	 * Removes an active powerup.
	 *
	 * @param powerup The active powerup to remove.
	 * @param got If true the powerup is removed because someone picked-up it.
	 */
	private void unspawnPowerup(ActivePowerup powerup, boolean got) {
		powerup.remove(got);
		locations.add(powerup.getLocation());
		activePowerups.remove(powerup);
	}


	/**
	 * When someone pickup a powerup, executes the good action.
	 *
	 * @param itemPicked The item picked up.
	 * @param player The player who picked up the item.
	 */
	public void onPowerupPickup(Item itemPicked, Player player) {
		String powerupUUID = itemPicked.getItemStack().getItemMeta().getDisplayName();
		ActivePowerup activePowerup = null;

		// Powerup lookup
		for(ActivePowerup powerup : activePowerups) {
			if(powerup.getActivePowerupUniqueID().toString().equals(powerupUUID)) {
				activePowerup = powerup;
				break;
			}
		}

		if(activePowerup == null) return;  // Not a powerup


		activePowerup.getPowerup().onPickup(player, itemPicked.getItemStack());
		unspawnPowerup(activePowerup, true);
	}


	public void registerPowerup(Powerup powerup) {
		powerups.add(powerup);
	}

	private void registerLocations() {
		List powerSpawns = p.getArenaConfig().getList("map.powerups");

		if(powerSpawns != null) {
			for (Object powerSpawn : powerSpawns) {
				if (powerSpawn instanceof String) {
					try {
						locations.add(Utils.stringToLocation(p, (String) powerSpawn));
					} catch (IllegalArgumentException e) {
						p.getLogger().log(Level.SEVERE, "Invalid powerup location in arena.yml! " + e.getMessage());
					}
				}
			}
		}
	}


	public PowerupSpawner getSpawner() {
		return spawner;
	}
}
