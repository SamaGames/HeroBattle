package net.lnfinity.HeroBattle.Class;

import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Tools.Tool;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GuerrierClass extends PlayerClass {

	public GuerrierClass(HeroBattle plugin) {

		super(plugin);
		
		addTool(p.getToolsManager().getTool(Tool.SWORD_VARIANT1));
		addTool(p.getToolsManager().getTool(Tool.SPEED));
		addTool(p.getToolsManager().getTool(Tool.POWER));
	}

	@Override
	public String getName() {
		return "Guerrier";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemStack(Material.DIAMOND_SWORD);
	}

	@Override
	public ItemStack getHat() {
		ItemStack item = new ItemStack(Material.STAINED_GLASS);
		item.setDurability((short) 8);
		return item;
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList("Il n'a pas froid aux yeux.");
	}

	@Override
	public int getMinDamages() {
		return 3;
	}

	@Override
	public int getMaxDamages() {
		return 6;
	}

	@Override
	public int getMaxResistance() {
		return 250;
	}

	@Override
	public int getLives() {
		return 4;
	}

}
