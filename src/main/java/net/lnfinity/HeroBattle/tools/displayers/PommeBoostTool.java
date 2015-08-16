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

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;


public class PommeBoostTool extends PlayerTool
{

	private final Integer COOLDOWN = 26;


	public PommeBoostTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.pommeboost";
	}

	@Override
	public String getName()
	{
		return ChatColor.YELLOW + "" + ChatColor.BOLD + "POMME BOOST";
	}

	@Override
	public List<String> getDescription()
	{
		return Arrays.asList(
				"PLUS de vitesse, MOINS de dégâts, PLUS de POMMES !"
		);
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack boost = new ItemStack(Material.GOLDEN_APPLE);
		ToolsUtils.resetTool(boost);

		return boost;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if(!ToolsUtils.isToolAvailable(tool))
		{
			player.sendMessage(ChatColor.RED + "Vous etes trop fatigué pour réutiliser ceci maintenant.");
			return;
		}

		GamePlayer gPlayer = HeroBattle.get().getGamePlayer(player);
		if(gPlayer == null || !gPlayer.isPlaying()) return;


		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2, true, false));
		gPlayer.addRemainingReducedIncomingDamages(10);


		new ItemCooldown(HeroBattle.get(), player, this, COOLDOWN);
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		onRightClick(player, tool, event);
	}
}
