package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.powerups.NegativePowerup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

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
public class ToastPowerup implements NegativePowerup
{

	@Override
	public void onPickup(final Player player, final ItemStack pickupItem)
	{
		player.setVelocity(player.getVelocity().add(new Vector(0, (new Random()).nextDouble() + 3, 0)));
		player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.8f, 1);

		player.sendMessage(ChatColor.RED + "Wooosh !");
	}

	@Override
	public ItemStack getItem()
	{
		return new ItemStack(Material.ANVIL);
	}

	@Override
	public String getName()
	{
		return ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "TOAST";
	}

	@Override
	public double getWeight()
	{
		return 0.1;
	}
}
