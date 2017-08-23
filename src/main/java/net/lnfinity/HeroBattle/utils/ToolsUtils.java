package net.lnfinity.HeroBattle.utils;

import net.samagames.tools.GlowEffect;
import org.bukkit.inventory.ItemStack;

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
public class ToolsUtils
{

	/**
	 * Makes the item stack representing a tool available for use.
	 *
	 * @param stack The item stack representing the tool.
	 */
	public static void resetTool(ItemStack stack)
	{
		if (stack == null) return;

		stack.setAmount(1);
		GlowEffect.addGlow(stack);
	}


	/**
	 * Returns true if the tool represented by this stack is available for use.
	 *
	 * @param stack The stack.
	 *
	 * @return True if available.
	 */
	public static boolean isToolAvailable(ItemStack stack)
	{
		return stack != null && stack.getAmount() == 1 && stack.containsEnchantment(GlowEffect.getGlow());
	}
}
