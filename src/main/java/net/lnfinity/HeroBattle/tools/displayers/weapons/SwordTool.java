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
import net.md_5.bungee.api.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

import java.util.*;


public abstract class SwordTool extends PlayerTool
{
	protected int upgrade = 0;
	protected Random random = new Random();


	public SwordTool(HeroBattle plugin)
	{
		super(plugin);
	}


	@Override
	public List<String> getDescription()
	{
		return Arrays.asList(
				ChatColor.GRAY + "Frappez les joueurs pour les repousser.",
				ChatColor.GRAY + "Cliquez droit pour faire un double saut."
		);
	}


	@Override
	public void onRightClick(final Player player, ItemStack tool, PlayerInteractEvent event)
	{
		p.getGamePlayer(player).doubleJump();

	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{

	}
}
