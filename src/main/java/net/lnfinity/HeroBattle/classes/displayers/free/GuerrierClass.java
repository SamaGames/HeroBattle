package net.lnfinity.HeroBattle.classes.displayers.free;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
import net.lnfinity.HeroBattle.tools.displayers.ArrowsTool;
import net.lnfinity.HeroBattle.tools.displayers.NotYetAvaibleTool;
import net.lnfinity.HeroBattle.tools.displayers.PowerTool;
import net.lnfinity.HeroBattle.tools.displayers.SpeedTool;
import net.lnfinity.HeroBattle.tools.displayers.weapons.DaggerSwordTool;
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
public class GuerrierClass extends PlayerClass
{

	public GuerrierClass(HeroBattle plugin)
	{
		this(plugin, 0, 0, 0);
	}

	public GuerrierClass(HeroBattle plugin, int arg1, int arg2, int arg3)
	{
		super(plugin, arg1, arg2, arg3);

		addTool(new DaggerSwordTool(p, arg1));
		addTool(new SpeedTool(p, 30 - arg1 * 2, (int) Math.floor(arg2 * 0.4), 8 + arg2));
		addTool(new PowerTool(p, 60 - arg1 * 4, 10 + arg2));
		if (arg3 >= 1)
			addTool(new ArrowsTool(p, 30 - arg1 * 2, 2, 5 + arg2, 16 + arg2 * 2, (int) (12 + 1.5 * arg2), 24 + 2 * arg2));
		if (arg3 >= 2) addTool(new NotYetAvaibleTool(p));
	}

	@Override
	public String getName()
	{
		return "Guerrier";
	}

	@Override
	public ItemStack getIcon()
	{
		return new ItemStack(Material.DIAMOND_SWORD);
	}

	@Override
	public ItemStack getHat()
	{
		ItemStack item = new ItemStack(Material.STAINED_GLASS);
		item.setDurability((short) 8);
		return item;
	}

	@Override
	public List<String> getDescription()
	{
		return Arrays.asList("Il n'a pas froid aux yeux.", "", ChatColor.GRAY + "Classe de type " + ChatColor.GOLD + "Corps à corps", ChatColor.GREEN + "+ " + ChatColor.GRAY + "Très résistant, dégâts ciblés, précision", ChatColor.RED + "- " + ChatColor.GRAY + "Puissance d'attaque, agilité");
	}

	@Override
	public int getMinDamages()
	{
		return 3;
	}

	@Override
	public int getMaxDamages()
	{
		return 6;
	}

	@Override
	public int getMaxResistance()
	{
		return 250;
	}

	@Override
	public int getLives()
	{
		return 3;
	}

	@Override
	public PlayerClassType getType()
	{
		return PlayerClassType.GUERRIER;
	}

}
