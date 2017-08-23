package net.lnfinity.HeroBattle.classes.displayers.eastereggs;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.EasterEggClass;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
import net.lnfinity.HeroBattle.tools.displayers.Cannon20mmTool;
import net.lnfinity.HeroBattle.tools.displayers.TripleJumpTool;
import net.lnfinity.HeroBattle.tools.displayers.weapons.Mac34SwordTool;
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
public class DewoitineClass extends PlayerClass implements EasterEggClass
{

	public DewoitineClass(HeroBattle plugin)
	{
		super(plugin, 0, 0, 0);
	}

	public DewoitineClass(HeroBattle plugin, int cooldown, int power, int tool)
	{
		super(plugin, cooldown, power, tool);

		addTool(new Mac34SwordTool(plugin));
		addTool(new Cannon20mmTool(plugin, 12, 1, 28, 34));
		addTool(new TripleJumpTool(plugin, 20, 4));
	}

	@Override
	public String getName()
	{
		return "Dewoitine";
	}

	@Override
	public ItemStack getIcon()
	{
		return new ItemStack(Material.FEATHER);
	}

	@Override
	public ItemStack getHat()
	{
		return new ItemStack(Material.SLIME_BLOCK);
	}

	@Override
	public List<String> getDescription()
	{
		return Collections.singletonList("Dewoitine");
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
		return PlayerClassType.DEWOITINE;
	}
}
