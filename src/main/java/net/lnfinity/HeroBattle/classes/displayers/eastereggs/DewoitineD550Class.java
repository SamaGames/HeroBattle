package net.lnfinity.HeroBattle.classes.displayers.eastereggs;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
import net.lnfinity.HeroBattle.tools.displayers.Cannon20mmTool;
import net.lnfinity.HeroBattle.tools.displayers.TripleJumpTool;
import net.lnfinity.HeroBattle.tools.displayers.weapons.Mac34SwordTool;

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
public class DewoitineD550Class extends DewoitineClass
{
	public DewoitineD550Class(HeroBattle plugin, int cooldown, int power, int tool)
	{
		super(plugin);

		addTool(new Mac34SwordTool(plugin));
		addTool(new Cannon20mmTool(plugin, 2, 1, 10, 14));
		addTool(new TripleJumpTool(plugin, 2, 25));
	}

	@Override
	public String getName()
	{
		return "Dewoitine";
	}

	@Override
	public int getMinDamages()
	{
		return 10;
	}

	@Override
	public int getMaxDamages()
	{
		return 12;
	}

	@Override
	public int getMaxResistance()
	{
		return 250;
	}

	@Override
	public int getLives()
	{
		return 4;
	}

	@Override
	public PlayerClassType getType()
	{
		return PlayerClassType.DEWOITINE_D550;
	}
}


