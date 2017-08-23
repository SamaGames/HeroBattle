package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.powerups.PositivePowerup;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.md_5.bungee.api.ChatColor;
import net.samagames.tools.GlowEffect;
import org.bukkit.Material;
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
public class ZeroCooldownPowerup implements PositivePowerup
{

	@Override
	public void onPickup(final Player player, final ItemStack pickupItem)
	{
		player.sendMessage(ChatColor.GREEN + "Toutes vos capacités ont été rafraichies !");

		for (int i = 1; i < 9; i++)
		{
			final ItemStack stack = player.getInventory().getItem(i);
			if (stack != null && stack.getType() != Material.AIR)
			{
				ToolsUtils.resetTool(stack);
			}
		}
	}

	@Override
	public ItemStack getItem()
	{
		final ItemStack item = new ItemStack(Material.CHEST);
		GlowEffect.addGlow(item);

		return item;
	}

	@Override
	public String getName()
	{
		return ChatColor.GOLD + "" + ChatColor.BOLD + "CAPACITÉS RAFRAICHIES";
	}

	@Override
	public double getWeight()
	{
		return 10;
	}
}
