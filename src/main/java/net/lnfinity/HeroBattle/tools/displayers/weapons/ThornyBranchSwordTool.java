package net.lnfinity.HeroBattle.tools.displayers.weapons;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
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
public class ThornyBranchSwordTool extends SwordTool
{

	public ThornyBranchSwordTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.sword.branch";
	}

	@Override
	public String getName()
	{
		return ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Branche épineuse";
	}

	@Override
	public ItemStack getItem()
	{
		return new ItemStack(Material.DEAD_BUSH);
	}

}
