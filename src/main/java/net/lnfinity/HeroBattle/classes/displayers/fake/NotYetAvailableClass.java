package net.lnfinity.HeroBattle.classes.displayers.fake;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
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
public class NotYetAvailableClass extends PlayerClass
{

	public NotYetAvailableClass(HeroBattle plugin)
	{
		super(plugin, 0, 0, 0);
	}

	@Override
	public String getName()
	{
		return "? ¿ ?";
	}

	@Override
	public ItemStack getIcon()
	{
		return new ItemStack(Material.BARRIER);
	}

	@Override
	public List<String> getDescription()
	{
		return Collections.singletonList("Bientôt disponible !");
	}

	@Override
	public int getMinDamages()
	{
		return 0;
	}

	@Override
	public int getMaxDamages()
	{
		return 0;
	}

	@Override
	public int getLives()
	{
		return 0;
	}

	@Override
	public ItemStack getHat()
	{
		return new ItemStack(Material.BARRIER);
	}

	@Override
	public int getMaxResistance()
	{
		return 0;
	}

	@Override
	public PlayerClassType getType()
	{
		return null;
	}
}
