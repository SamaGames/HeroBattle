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
	
	public abstract ItemStack getItem();

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
