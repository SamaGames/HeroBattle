package net.lnfinity.HeroBattle.classes.displayers.paid;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
import net.lnfinity.HeroBattle.tools.displayers.EarthquakeTool;
import net.lnfinity.HeroBattle.tools.displayers.HealingTool;
import net.lnfinity.HeroBattle.tools.displayers.InkTool;
import net.lnfinity.HeroBattle.tools.displayers.NotYetAvaibleTool;
import net.lnfinity.HeroBattle.tools.displayers.weapons.WisdomStickSwordTool;
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
public class DruideClass extends PlayerClass
{

	public DruideClass(HeroBattle plugin)
	{
		this(plugin, 0, 0, 0);
	}

	public DruideClass(HeroBattle plugin, int arg1, int arg2, int arg3)
	{
		super(plugin, arg1, arg2, arg3);

		addTool(new WisdomStickSwordTool(plugin));
		double probability = 0.4 - arg2 * 0.05;
		probability = probability < 0 ? 0 : probability;
		addTool(new HealingTool(plugin, 90 - arg1 * 2, 50 + arg2 * 2, probability));
		addTool(new InkTool(plugin, 60 - arg1 * 2, 6 + arg2, 0.25 - arg2 * 0.05));
		if (arg3 >= 1) addTool(new EarthquakeTool(plugin, 60 - arg1 * 2, 18 + arg2, 30 + arg2 * 2));
		if (arg3 >= 2) addTool(new NotYetAvaibleTool(p));
	}

	@Override
	public String getName()
	{
		return "Druide";
	}

	@Override
	public ItemStack getIcon()
	{
		return new ItemStack(Material.GOLDEN_APPLE);
	}

	@Override
	public ItemStack getHat()
	{
		ItemStack item = new ItemStack(Material.STAINED_GLASS);
		item.setDurability((short) 13);
		return item;
	}

	@Override
	public List<String> getDescription()
	{
		return Arrays.asList("La capacité à pouvoir se soigner.", "", ChatColor.GRAY + "Classe de type " + ChatColor.GOLD + "Magie", ChatColor.GREEN + "+ " + ChatColor.GRAY + "Peut se soigner, résistant", ChatColor.RED + "- " + ChatColor.GRAY + "Vitesse, agilité");
	}

	@Override
	public int getMinDamages()
	{
		return 4;
	}

	@Override
	public int getMaxDamages()
	{
		return 6;
	}

	@Override
	public int getMaxResistance()
	{
		return 150;
	}

	@Override
	public int getLives()
	{
		return 3;
	}

	@Override
	public PlayerClassType getType()
	{
		return PlayerClassType.DRUIDE;
	}

}
