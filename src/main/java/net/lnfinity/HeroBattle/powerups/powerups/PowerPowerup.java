package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
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
public class PowerPowerup implements PositivePowerup
{
	private final HeroBattle p;

	public PowerPowerup(final HeroBattle plugin)
	{
		p = plugin;
	}

	@Override
	public void onPickup(final Player player, final ItemStack pickupItem)
	{
		final HeroBattlePlayer heroBattlePlayer = p.getGamePlayer(player);
		player.sendMessage(ChatColor.GREEN + "Vous " + ChatColor.DARK_GREEN + "doublez votre puissance" + ChatColor.GREEN + " pour 24 secondes !");
		heroBattlePlayer.addRemainingDoubleDamages(24);
	}

	@Override
	public ItemStack getItem()
	{
		return new ItemStack(Material.BLAZE_POWDER);
	}

	@Override
	public String getName()
	{
		return ChatColor.DARK_RED + "" + ChatColor.BOLD + "FORCE";
	}

	@Override
	public double getWeight()
	{
		return 25;
	}
}
