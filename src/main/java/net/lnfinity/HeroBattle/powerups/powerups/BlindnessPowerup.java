package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.powerups.NegativePowerup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
public class BlindnessPowerup implements NegativePowerup
{

	@Override
	public void onPickup(final Player player, final ItemStack pickupItem)
	{

		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 8, 1, true));
		player.sendMessage(ChatColor.RED + "Ça va être tout noir !");

	}

	@Override
	public ItemStack getItem()
	{
		return new ItemStack(Material.INK_SACK);
	}

	@Override
	public String getName()
	{
		return ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "CÉCITÉ";
	}

	@Override
	public double getWeight()
	{
		return 15;
	}
}
