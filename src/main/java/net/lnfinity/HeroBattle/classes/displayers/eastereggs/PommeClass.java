package net.lnfinity.HeroBattle.classes.displayers.eastereggs;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.EasterEggClass;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
import net.lnfinity.HeroBattle.tools.displayers.PommEhxploseTool;
import net.lnfinity.HeroBattle.tools.displayers.PommeBoostTool;
import net.lnfinity.HeroBattle.tools.displayers.SaintePommeTool;
import net.lnfinity.HeroBattle.tools.displayers.weapons.AppleWhipSwordTool;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
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
public class PommeClass extends PlayerClass implements EasterEggClass
{

	public PommeClass()
	{
		this(HeroBattle.get(), 0, 0, 0);
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
