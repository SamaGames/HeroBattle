package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.powerups.PositivePowerup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Plugin UltraHardcore Reloaded (UHPlugin)
 * Copyright (C) 2013 azenet
 * Copyright (C) 2014-2015 Amaury Carrade
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see [http://www.gnu.org/licenses/].
 */

public class NightVisionPowerup implements PositivePowerup {

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {

		player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 30, 0));
		player.sendMessage(ChatColor.GREEN + "Cadeau ! Un peu de claireté pour une trentaine de secondes.");

	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Material.REDSTONE_LAMP_OFF);
	}

	@Override
	public String getName() {
		return ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "VISION NOCTURNE";
	}

	@Override
	public double getWeight() {
		return 15;
	}
}
