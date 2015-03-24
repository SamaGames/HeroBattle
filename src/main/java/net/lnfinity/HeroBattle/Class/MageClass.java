package net.lnfinity.HeroBattle.Class;

import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Tools.Tool;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MageClass extends PlayerClass {

	public MageClass(HeroBattle plugin) {

		super(plugin);
		
		addTool(p.getToolsManager().getTool(Tool.SWORD_VARIANT2));
		addTool(p.getToolsManager().getTool(Tool.SMOKE));
		addTool(p.getToolsManager().getTool(Tool.THUNDER));
	}

	@Override
	public String getName() {
		return "Mage";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemStack(Material.POTION);
	}

	@Override
	public ItemStack getHat() {
		ItemStack item = new ItemStack(Material.STAINED_GLASS);
		item.setDurability((short) 10);
		return item;
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList("La magie, il maitrise.");
	}

	@Override
	public int getMinDamages() {
		return 2;
	}

	@Override
	public int getMaxDamages() {
		return 6;
	}

	@Override
	public int getMaxResistance() {
		return 175;
	}

	@Override
	public int getLives() {
		return 3;
	}

}
