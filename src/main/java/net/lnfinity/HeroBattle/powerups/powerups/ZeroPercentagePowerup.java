package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.powerups.PositivePowerup;
import org.bukkit.ChatColor;
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
public class ZeroPercentagePowerup implements PositivePowerup
{

	private final HeroBattle p;

	public ZeroPercentagePowerup(final HeroBattle plugin)
	{
		p = plugin;
	}

	@Override
	public void onPickup(final Player player, final ItemStack pickupItem)
	{
		p.getGamePlayer(player).setPercentage(0, null);

		player.sendMessage(ChatColor.GREEN + "Votre pourcentage a été remis à " + ChatColor.DARK_GREEN + "0 " + ChatColor.GREEN + "!");
	}

	@Override
	public ItemStack getItem()
	{
		return new ItemStack(Material.NETHER_STAR);
	}

	@Override
	public String getName()
	{
		return ChatColor.AQUA + "" + ChatColor.BOLD + "POURCENTAGE À ZÉRO";
	}

	@Override
	public double getWeight()
	{
		return 5;
	}
}
