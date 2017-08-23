package net.lnfinity.HeroBattle.tools.displayers.weapons;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
public class DaggerSwordTool extends SwordTool
{

	public DaggerSwordTool(HeroBattle plugin, int arg1)
	{
		super(plugin);
		upgrade = arg1;
	}

	@Override
	public String getToolID()
	{
		return "tool.sword.dagger";
	}

	@Override
	public String getName()
	{
		return ChatColor.RED + "" + ChatColor.BOLD + "Poignard";
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.DIAMOND_SWORD, 1);

		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);

		return item;
	}

}
