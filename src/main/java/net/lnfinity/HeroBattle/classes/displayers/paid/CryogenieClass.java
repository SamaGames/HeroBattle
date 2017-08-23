package net.lnfinity.HeroBattle.classes.displayers.paid;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
import net.lnfinity.HeroBattle.tools.displayers.IceTool;
import net.lnfinity.HeroBattle.tools.displayers.InvincibleTool;
import net.lnfinity.HeroBattle.tools.displayers.NotYetAvaibleTool;
import net.lnfinity.HeroBattle.tools.displayers.RemoveFireTool;
import net.lnfinity.HeroBattle.tools.displayers.weapons.ColdScepterSwordTool;
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
public class CryogenieClass extends PlayerClass
{

	public CryogenieClass(HeroBattle plugin)
	{
		this(plugin, 0, 0, 0);
	}

	public CryogenieClass(HeroBattle plugin, int arg1, int arg2, int arg3)
	{
		super(plugin, arg1, arg2, arg3);

		addTool(new ColdScepterSwordTool(p));
		addTool(new InvincibleTool(p, 60 - arg1 * 2, 8 + arg2));
		addTool(new IceTool(p, 30 - arg1 * 2, 6 + arg2));
		if (arg3 >= 1) addTool(new RemoveFireTool(p, 25 - arg1));
		if (arg3 >= 2) addTool(new NotYetAvaibleTool(p));
	}

	@Override
	public String getName()
	{
		return "Cryogénie";
	}

	@Override
	public ItemStack getIcon()
	{
		ItemStack item = new ItemStack(Material.INK_SACK);
		item.setDurability((short) 7);
		return item;
	}

	@Override
	public ItemStack getHat()
	{
		ItemStack item = new ItemStack(Material.STAINED_GLASS);
		item.setDurability((short) 0);
		return item;
	}

	@Override
	public List<String> getDescription()
	{
		return Arrays.asList("Maître des contrées du froid", "", ChatColor.GRAY + "Classe de type " + ChatColor.GOLD + "Mêlée", ChatColor.GREEN + "+ " + ChatColor.GRAY + "Très résistant, défensif", ChatColor.RED + "- " + ChatColor.GRAY + "Combats distants");
	}

	@Override
	public int getMinDamages()
	{
		return 5;
	}

	@Override
	public int getMaxDamages()
	{
		return 7;
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
		return PlayerClassType.CRYOGENIE;
	}

}
