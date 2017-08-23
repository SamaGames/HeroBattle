package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.powerups.PositivePowerup;
import net.md_5.bungee.api.ChatColor;
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
public class TripleJumpPowerup implements PositivePowerup
{
	private final HeroBattle p;

	public TripleJumpPowerup(final HeroBattle plugin)
	{
		p = plugin;
	}

	@Override
	public void onPickup(final Player player, final ItemStack pickupItem)
	{
		p.getGamePlayer(player).setMaxJumps(3, 15);
		player.sendMessage(ChatColor.GREEN + "Vous pouvez désormais faire des triple sauts !");
	}

	@Override
	public ItemStack getItem()
	{
		return new ItemStack(Material.FEATHER);
	}

	@Override
	public String getName()
	{
		return ChatColor.RED + "" + ChatColor.BOLD + "TRIPLE SAUT";
	}

	@Override
	public double getWeight()
	{
		return 10;
	}
}
