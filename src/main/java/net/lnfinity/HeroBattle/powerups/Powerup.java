package net.lnfinity.HeroBattle.powerups;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Powerup {
	
	public void onPickup(Player player, ItemStack pickupItem);

	/**
	 * The item used to represent this powerup.
	 */
	public ItemStack getItem();

	/**
	 * The name of this powerup, displayed above.
	 */
	public String getName();


	/**
	 * Returns the weight of this powerup. Used to randomly choose the powerup to spawn.
	 *
	 * <p>
	 *     Powerups with a higher weight will have an higher chance to spawn.
	 * </p>
	 * <p>
	 *     Between {@code 0} and {@code Double.MAX_VALUE}.
	 * </p>
	 */
	public double getWeight();

}
