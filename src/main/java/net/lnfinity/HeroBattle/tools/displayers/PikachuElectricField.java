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

package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.tools.*;
import net.lnfinity.HeroBattle.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;

import java.util.*;


public class PikachuElectricField extends PlayerTool
{

	private final Integer ELECTRIC_FIELD_DURATION = 10;
	private final Float   ELECTRIC_FIELD_THORNS_EFFICIENCY = 0.7f;

	private final Integer COOLDOWN = 45;

	public PikachuElectricField()
	{
		super(HeroBattle.get());
	}

	@Override
	public String getToolID()
	{
		return "tools.electricField";
	}

	@Override
	public String getName()
	{
		return ChatColor.YELLOW + "" + ChatColor.BOLD + "Electric Field";
	}

	@Override
	public List<String> getDescription()
	{
		return Utils.getToolDescription(ChatColor.GRAY + "Un champ électrique renvoie partiellement les dégâts subits à leurs envoyeurs pendant " + ChatColor.GOLD + ELECTRIC_FIELD_DURATION + ChatColor.GRAY + " secondes.");
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.RAW_FISH);
		item.setDurability((short) 3); // Pufferfish

		ToolsUtils.resetTool(item);
		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (!ToolsUtils.isToolAvailable(tool))
		{
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser cela maintenant. Chuu!");
			return;
		}

		GamePlayer gPlayer = HeroBattle.get().getGamePlayer(player);
		if(gPlayer == null || !gPlayer.isPlaying())
		{
			player.sendMessage(ChatColor.RED + "Euh... nop ? [Pas un joueur actif ou gPlayer == null] [wtf this was displayed, plz report to a dev]");
			return;
		}

		gPlayer.addRemainingThorns(ELECTRIC_FIELD_DURATION, ELECTRIC_FIELD_THORNS_EFFICIENCY);

		player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, ELECTRIC_FIELD_DURATION * 20, 0, false, true));

		new ItemCooldown(HeroBattle.get(), player, this, COOLDOWN);
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		onRightClick(player, tool, event);
	}
}
