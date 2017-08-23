package net.lnfinity.HeroBattle.classes.displayers.free;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
import net.lnfinity.HeroBattle.tools.displayers.EarthquakeTool;
import net.lnfinity.HeroBattle.tools.displayers.InkTool;
import net.lnfinity.HeroBattle.tools.displayers.NauseaTool;
import net.lnfinity.HeroBattle.tools.displayers.NotYetAvaibleTool;
import net.lnfinity.HeroBattle.tools.displayers.weapons.WroughtIronSwordTool;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
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
public class BruteClass extends PlayerClass
{

	public BruteClass(HeroBattle plugin)
	{
		this(plugin, 0, 0, 0);
	}

	public BruteClass(HeroBattle plugin, int arg1, int arg2, int arg3)
	{
		super(plugin, arg1, arg2, arg3);

		addTool(new WroughtIronSwordTool(p));
		addTool(new EarthquakeTool(p, 30 - arg1 * 2, 30 + arg2 * 2, 50 + arg2 * 4));
		addTool(new NauseaTool(p, 60 - arg1 * 4, 10 + arg2, 0.25 - arg2 * 0.05));
		if (arg3 >= 1) addTool(new InkTool(p, 90 - arg1 * 4, 5 + arg2, 0.35 - arg2 * 0.05));
		if (arg3 >= 2) addTool(new NotYetAvaibleTool(p));
	}

	@Override
	public String getName()
	{
		return "Brute";
	}

	@Override
	public ItemStack getIcon()
	{
		return new ItemStack(Material.DIAMOND_CHESTPLATE);
	}

	@Override
	public List<String> getDescription()
	{
		return Arrays.asList("Pour le plaisir de faire des dégâts.", "", ChatColor.GRAY + "Classe de type " + ChatColor.GOLD + "Mêlée", ChatColor.GREEN + "+ " + ChatColor.GRAY + "Dégâts de masse, résistant", ChatColor.RED + "- " + ChatColor.GRAY + "Précision, agilité");
	}

	@Override
	public int getMinDamages()
	{
		return 1;
	}

	@Override
	public int getMaxDamages()
	{
		return 8;
	}

	@Override
	public int getLives()
	{
		return 3;
	}

	@Override
	public ItemStack getHat()
	{
		ItemStack item = new ItemStack(Material.STAINED_GLASS);
		item.setDurability((short) 14);
		return item;
	}

	@Override
	public int getMaxResistance()
	{
		return 200;
	}

	@Override
	public PlayerClassType getType()
	{
		return PlayerClassType.BRUTE;
	}

}
