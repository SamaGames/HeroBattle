package net.lnfinity.HeroBattle.Powerups;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ActivePowerup {

	// Location
	private Location location;

	// Powerup
	private Powerup powerup;

	// Entities of the powerup
	private Item entityItem;
	private ArmorStand entityBase;
	private ArmorStand entityTitle;

	// Alive?
	private boolean alive = false;

	public ActivePowerup(Location location, Powerup powerup) {
		this.location = location;
		this.powerup = powerup;
	}

	public void spawn() {
		World world = location.getWorld();

		ItemStack powerupItem = powerup.getItem().clone();
		ItemMeta powerupItemMeta = powerupItem.getItemMeta();
			powerupItemMeta.setDisplayName(powerup.getName());
		powerupItem.setItemMeta(powerupItemMeta);


		entityBase = world.spawn(location.clone().add(0, -0.5, 0), ArmorStand.class);
		entityBase.setVisible(false);
		entityBase.setSmall(true);
		entityBase.setGravity(false);

		entityItem = world.dropItem(location, powerupItem);
		entityItem.setPickupDelay(0);

		entityTitle = world.spawn(location, ArmorStand.class);
		entityTitle.setGravity(false);
		entityTitle.setVisible(false);
		entityTitle.setSmall(true);
		entityTitle.setCustomName(powerup.getName());
		entityTitle.setCustomNameVisible(true);
		entityTitle.setCanPickupItems(false);


		entityBase.setPassenger(entityItem);
		entityItem.setPassenger(entityTitle);


		alive = true;
	}

	public void remove() {
		entityTitle.remove();
		entityItem.remove();
		entityBase.remove();

		alive = false;
	}


	public boolean isAlive() {
		return alive;
	}

	public Powerup getPowerup() {
		return powerup;
	}

	public Location getLocation() {
		return location;
	}
}
