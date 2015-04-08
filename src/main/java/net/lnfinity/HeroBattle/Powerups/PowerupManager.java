package net.lnfinity.HeroBattle.Powerups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Utils.Utils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

public class PowerupManager {

	HeroBattle p;
	private PowerupSpawner spawner;
	private List<Powerup> powerups = new ArrayList<Powerup>();
	Map<Location, Powerup> existingPowerups = new HashMap<Location, Powerup>();
	private List<Location> locations = new ArrayList<Location>();

	public PowerupManager(HeroBattle plugin) {
		p = plugin;
		spawner = new PowerupSpawner(p);
		registerLocations();
	}

	public void registerLocations() {
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

	public void addPowerup(Powerup powerup) {
		powerups.add(powerup);
	}

	public Location chooseRandomLocation() {
		return locations.get(Utils.randomNumber(0, locations.size() - 1));
	}

	public void spawnPowerup() {
		if (locations == null || locations.size() == 0 || existingPowerups.size() >= locations.size()) {
			return;
		}
		Location loc = locations.get(Utils.randomNumber(0, locations.size() - 1));

		if (!existingPowerups.containsKey(loc)) {
			p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Un bonus vient de faire son apparition !");

			for (Player player : p.getServer().getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
			}

			Firework fw = loc.getWorld().spawn(Utils.blockLocation(loc), Firework.class);
			FireworkMeta fwm = fw.getFireworkMeta();
			FireworkEffect effect = FireworkEffect.builder()
					.withColor(Color.BLUE.mixColors(Color.YELLOW.mixColors(Color.GREEN))).with(Type.BALL)
					.withFade(Color.RED).build();
			fwm.setPower(0);
			fwm.addEffects(effect);
			fw.setFireworkMeta(fwm);

			Powerup pw = powerups.get(Utils.randomNumber(0, powerups.size() - 1));

			existingPowerups.put(loc, pw);

			loc.getWorld().dropItem(Utils.blockLocation(loc), pw.getItem()).setVelocity(new Vector(0, 0, 0));
		}

	}

	public PowerupSpawner getSpawner() {
		return spawner;
	}

	public void removeSpawnedPowerup(Location loc) {
		if (existingPowerups.get(Utils.roundLocation(loc)) != null) {
			existingPowerups.remove(Utils.roundLocation(loc));
		}
	}

	public boolean hasItemSpawned(Location loc) {
		for (Map.Entry<Location, Powerup> entry : getExistingPowerups().entrySet()) {

			if (Utils.roundLocation(Utils.roundLocation(loc)).equals(entry.getKey())) {
				return true;
			}
		}
		return false;
	}

	public Powerup getItem(Location loc) {
		if (existingPowerups.get(Utils.roundLocation(loc)) != null) {
			return existingPowerups.get(Utils.roundLocation(loc));
		} else {
			return null;
		}
	}

	public HashMap<Location, Powerup> getExistingPowerups() {
		return (HashMap<Location, Powerup>) existingPowerups;
	}
}
