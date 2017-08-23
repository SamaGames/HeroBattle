package net.lnfinity.HeroBattle.classes.displayers.eastereggs;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.EasterEggClass;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
import net.lnfinity.HeroBattle.tools.displayers.PikachuElectricField;
import net.lnfinity.HeroBattle.tools.displayers.PikachuShockBlastTool;
import net.lnfinity.HeroBattle.tools.displayers.PikachuThunderWaveTool;
import net.lnfinity.HeroBattle.tools.displayers.weapons.PikachuTailSwordTool;
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
public class PikachuClass extends PlayerClass implements EasterEggClass
{

	public PikachuClass()
	{
		super(HeroBattle.get(), 0, 0, 0);

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
