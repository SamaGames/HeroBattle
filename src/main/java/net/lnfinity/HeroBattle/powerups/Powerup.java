package net.lnfinity.HeroBattle.powerups;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
