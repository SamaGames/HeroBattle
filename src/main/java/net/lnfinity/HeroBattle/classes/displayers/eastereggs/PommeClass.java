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

package net.lnfinity.HeroBattle.classes.displayers.eastereggs;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.*;
import net.lnfinity.HeroBattle.tools.displayers.PommEhxploseTool;
import net.lnfinity.HeroBattle.tools.displayers.PommeBoostTool;
import net.lnfinity.HeroBattle.tools.displayers.SaintePommeTool;
import net.lnfinity.HeroBattle.tools.displayers.weapons.AppleWhipSwordTool;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;


public class PommeClass extends PlayerClass implements EasterEggClass
{

	public PommeClass()
	{
		this(HeroBattle.getInstance(), 0, 0, 0);
	}

	public PommeClass(HeroBattle plugin, int cooldown, int power, int tool)
	{
		super(plugin, cooldown, power, tool);

		addTool(new AppleWhipSwordTool(plugin));
		addTool(new PommEhxploseTool(plugin));
		addTool(new PommeBoostTool(plugin));
		addTool(new SaintePommeTool(plugin));
	}

	@Override
	public String getName()
	{
		return "POOOMME";
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
		item.setDurability((short) 14);
		return item;
	}

	@Override
	public List<String> getDescription()
	{
		return Collections.singletonList("POMME");
	}

	@Override
	public int getMinDamages()
	{
		return 3;
	}

	@Override
	public int getMaxDamages()
	{
		return 7;
	}

	@Override
	public int getMaxResistance()
	{
		return 180;
	}

	@Override
	public int getLives()
	{
		return 3;
	}

	@Override
	public PlayerClassType getType()
	{
		return PlayerClassType.POMME;
	}
}
