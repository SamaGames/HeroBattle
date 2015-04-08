package net.lnfinity.HeroBattle.Powerups;


import net.minecraft.server.v1_8_R1.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;

import java.util.UUID;

public class ActivePowerup {

	// Location
	private Location location;

	// Powerup
	private Powerup powerup;

	// Entities of the powerup
	Item entityItem;
	ArmorStand entityBase;
	ArmorStand entityTitle;

	public ActivePowerup(Location location, Powerup powerup) {
		this.location = location;
		this.powerup = powerup;
	}

	public void spawn() {
		World world = location.getWorld();

		entityBase = world.spawn(location.clone().add(0, -0.5, 0), ArmorStand.class);
		entityBase.setVisible(false);
		entityBase.setSmall(true);
		entityBase.setGravity(false);

		entityItem = world.dropItem(location, powerup.getItem());
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
	}

	public void remove() {
		entityTitle.remove();
		entityItem.remove();
		entityBase.remove();
	}
}
