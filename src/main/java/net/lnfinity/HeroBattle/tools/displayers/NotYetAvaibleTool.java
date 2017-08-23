package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.md_5.bungee.api.ChatColor;
import net.samagames.tools.Titles;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

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
public class NotYetAvaibleTool extends PlayerTool
{

	public NotYetAvaibleTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.notyet";
	}

	@Override
	public String getName()
	{
		return ChatColor.RED + "" + ChatColor.BOLD + "?¿?";
	}

	@Override
	public List<String> getDescription()
	{
		return Arrays.asList(ChatColor.GRAY + "Cet objet n'est pas encore disponible !", ChatColor.GRAY + "Son arrivée est imminente.");
	}

	@Override
	public ItemStack getItem()
	{
		return new ItemStack(Material.BARRIER);
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		Titles.sendTitle(player, 10, 40, 10, ChatColor.RED + "Nope", ChatColor.RED + "Pas encore disponible");
	}
}
