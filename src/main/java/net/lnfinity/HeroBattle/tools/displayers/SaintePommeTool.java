/**
 * Plugin BelovedBlocks
 * Copyright (C) 2014-2015 Amaury Carrade & Florian Cassayre
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

package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.tools.*;
import net.lnfinity.HeroBattle.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

import java.util.*;


public class SaintePommeTool extends PlayerTool
{

	private final Integer COOLDOWN = 45;
	private final Double FAILURE_PROBABILITY = 0.15;

	private final Random random = new Random();


	public SaintePommeTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.holyApple";
	}

	@Override
	public String getName()
	{
		return ChatColor.GOLD + "" + ChatColor.BOLD + "Sainte POMME";
	}

	@Override
	public List<String> getDescription()
	{
		return Arrays.asList(
				ChatColor.GRAY + "Diminue le pourcentage actuel de quelques pourcents.",
				ChatColor.GRAY + "Mais cet outil ne marche pas à tous les coups..."
		);
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack holy = new ItemStack(Material.GOLDEN_APPLE);
		holy.setDurability((short) 1);

		ToolsUtils.resetTool(holy);
		return holy;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if(!ToolsUtils.isToolAvailable(tool))
		{
			player.sendMessage(ChatColor.RED + "Vous etes trop fatigué pour réutiliser ceci maintenant.");
			return;
		}

		HeroBattlePlayer gPlayer = HeroBattle.get().getGamePlayer(player);
		if (gPlayer == null || gPlayer.isSpectator()) return;


		if(random.nextDouble() < FAILURE_PROBABILITY)
		{
			player.sendMessage(ChatColor.RED + "Meh, ce n'est pas la sainte pomme...");
		}
		else
		{
			gPlayer.setPercentage(gPlayer.getPercentage() - 65);
			player.sendMessage(ChatColor.RED + "POMMEH PUISSANT");
		}

		new ItemCooldown(HeroBattle.get(), player, this, COOLDOWN);
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		onRightClick(player, tool, event);
	}
}
