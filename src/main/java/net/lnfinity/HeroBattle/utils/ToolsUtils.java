package net.lnfinity.HeroBattle.utils;

import net.samagames.utils.GlowEffect;
import org.bukkit.inventory.ItemStack;


public class ToolsUtils {

	/**
	 * Makes the item stack representing a tool available for use.
	 *
	 * @param stack The item stack representing the tool.
	 */
	public static void resetTool(ItemStack stack) {
		if(stack == null) return;

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
	public static boolean isToolAvailable(ItemStack stack) {
		return stack != null && stack.getAmount() == 1 && stack.containsEnchantment(GlowEffect.getGlow());
	}
}
