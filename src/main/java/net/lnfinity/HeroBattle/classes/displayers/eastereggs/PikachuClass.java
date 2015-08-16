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

package net.lnfinity.HeroBattle.classes.displayers.eastereggs;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.classes.*;
import net.lnfinity.HeroBattle.tools.displayers.*;
import net.lnfinity.HeroBattle.tools.displayers.weapons.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

import java.util.*;


public class PikachuClass extends PlayerClass implements EasterEggClass
{

	public PikachuClass()
	{
		super(HeroBattle.getInstance(), 0, 0, 0);

		addTool(new PikachuTailSwordTool());
		addTool(new PikachuShockBlastTool());
		addTool(new PikachuElectricField());
		addTool(new PikachuThunderWaveTool());
	}

	@Override
	public String getName()
	{
		return "Pikachu";
	}

	@Override
	public ItemStack getIcon()
	{
		return new ItemStack(Material.AIR);
	}

	@Override
	public ItemStack getHat()
	{
		ItemStack item = new ItemStack(Material.STAINED_GLASS);
		item.setDurability((short) 4);
		return item;
	}

	@Override
	public List<String> getDescription()
	{
		return Collections.singletonList("Chuuuuuu");
	}

	@Override
	public int getMinDamages()
	{
		return 3;
	}

	@Override
	public int getMaxDamages()
	{
		return 5;
	}

	@Override
	public int getMaxResistance()
	{
		return 200;
	}

	@Override
	public int getLives()
	{
		return 3;
	}

	@Override
	public PlayerClassType getType()
	{
		return PlayerClassType.PIKACHU;
	}
}
