package net.lnfinity.HeroBattle.Class;

import org.bukkit.inventory.ItemStack;

public abstract class PlayerClass {
	
	protected ItemStack items[] = new ItemStack[9];

	public abstract String getName();
	
	public abstract int getMinDamages();
	
	public abstract int getMaxDamages();
	
	public abstract int getHearths();

	public ItemStack getItem(int i) {
		return items[i];
	}
	
}
