/*
 * Copyright (C) 2015 Amaury Carrade
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.lnfinity.HeroBattle.tools.displayers.weapons;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.tools.*;
import org.bukkit.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;

import java.util.*;


public class PikachuTailSwordTool extends SwordTool implements Weapon
{

	private final Double SPEED_PROBABILITY = 0.05;
	private final Integer SPEED_DURATION = 2; // seconds


	public PikachuTailSwordTool()
	{
		super(HeroBattle.get());
	}

	@Override
	public String getToolID()
	{
		return "tool.sword.pikachuTail";
	}

	@Override
	public String getName()
	{
		return ChatColor.YELLOW + "" + ChatColor.BOLD + "Pikachu Tail";
	}

	@Override
	public ItemStack getItem()
	{
		return new ItemStack(Material.GOLD_HOE);
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList(
				net.md_5.bungee.api.ChatColor.GRAY + "Frappez les joueurs pour les repousser.",
				net.md_5.bungee.api.ChatColor.GRAY + "Cliquez droit pour faire un double saut.",
				"",
				net.md_5.bungee.api.ChatColor.AQUA + "Effet spécial:",
				net.md_5.bungee.api.ChatColor.GRAY + "Vous avez " + ChatColor.GOLD + SPEED_PROBABILITY * 100 + ChatColor.GRAY + "% de chance gagner un effet", ChatColor.GRAY + "de vitesse de " + ChatColor.GOLD + SPEED_DURATION + ChatColor.GRAY + " secondes à chaque coup porté"
		);
	}

	@Override
	public void onPlayerHit(Player sender, Player victim) {
		if(random.nextDouble() <= SPEED_PROBABILITY) {
			sender.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, SPEED_DURATION * 20, 0));
		}
	}
}
