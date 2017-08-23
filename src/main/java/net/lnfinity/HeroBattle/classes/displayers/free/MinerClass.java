package net.lnfinity.HeroBattle.classes.displayers.free;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
import net.lnfinity.HeroBattle.tools.displayers.CrackTool;
import net.lnfinity.HeroBattle.tools.displayers.MinerSpecialTool;
import net.lnfinity.HeroBattle.tools.displayers.TNTTool;
import net.lnfinity.HeroBattle.tools.displayers.weapons.DrillSwordTool;
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
public class MinerClass extends PlayerClass
{

	public MinerClass(HeroBattle plugin)
	{
		super(plugin, 0, 0, 0);
	}

	public MinerClass(HeroBattle plugin, int cooldown, int power, int tool)
	{
		super(plugin, cooldown, power, tool);

		addTool(new DrillSwordTool(p));
		addTool(new TNTTool(p, 25 - cooldown, (int) (4 - power * 0.5)));
		addTool(new CrackTool(p, 55 - cooldown * 2, 25 + power * 2));
		if (tool >= 1) addTool(new MinerSpecialTool(p, 30 - cooldown, (int) (5 + 0.5 * power)));
	}

	@Override
	public String getName()
	{
		return "Mineur";
	}

	@Override
	public ItemStack getIcon()
	{
		return new ItemStack(Material.DIAMOND_PICKAXE);
	}

	@Override
	public ItemStack getHat()
	{
		return new ItemStack(Material.STONE);
	}

	@Override
	public List<String> getDescription()
	{
		return Arrays.asList("Souvent peu redouté au corps à corps,", "il peut s'avérérer très puissant !", "", ChatColor.GRAY + "Classe de type " + ChatColor.GOLD + "Equilibrée", ChatColor.GREEN + "+ " + ChatColor.GRAY + "Corps à corps, distance", ChatColor.RED + "- " + ChatColor.GRAY + "Peu résistant, peu agile");
	}

	@Override
	public int getMinDamages()
	{
		return 4;
	}

	@Override
	public int getMaxDamages()
	{
		return 8;
	}

	@Override
	public int getMaxResistance()
	{
		return 160;
	}

	@Override
	public int getLives()
	{
		return 3;
	}

	@Override
	public PlayerClassType getType()
	{
		return PlayerClassType.MINEUR;
	}

}
