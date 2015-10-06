package net.lnfinity.HeroBattle.powerups;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public interface Powerup
{
	/**
	 * Called when the powerup is picked up by a player.
	 *
	 * @param player     The player who picked up this powerup.
	 * @param pickupItem The picked up item.
	 */
	void onPickup(Player player, ItemStack pickupItem);

	/**
	 * The item used to represent this powerup.
	 */
	ItemStack getItem();

	/**
	 * The name of this powerup, displayed above.
	 */
	String getName();


	/**
	 * Returns the weight of this powerup. Used to randomly choose the powerup to spawn. <p/> <p>
	 * Powerups with a higher weight will have an higher chance to spawn. </p> <p> Between {@code 0}
	 * and {@code Double.MAX_VALUE}. </p>
	 */
	double getWeight();
}
