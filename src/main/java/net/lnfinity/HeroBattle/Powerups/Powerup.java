package net.lnfinity.HeroBattle.Powerups;

import net.lnfinity.HeroBattle.HeroBattle;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Powerup {

	protected HeroBattle p;
	protected Location location;
	
	public Powerup(HeroBattle plugin, Location loc) {
		p = plugin;
		location = loc;
	}
	
	public abstract void onPickup(Player player, ItemStack pickupItem);

	/**
	 * The item used to represent this powerup.
	 */
	public abstract ItemStack getItem();

	/**
	 * The name of this powerup, displayed above.
	 */
	public abstract String getName();


	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
