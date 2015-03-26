package net.lnfinity.HeroBattle.Class;

import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Tools.Tool;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ArcherClass extends PlayerClass {

	public ArcherClass(HeroBattle plugin) {
		super(plugin);

		addTool(p.getToolsManager().getTool(Tool.SWORD_VARIANT3));
		addTool(p.getToolsManager().getTool(Tool.SPEED));
		addTool(p.getToolsManager().getTool(Tool.ARROWS));
	}

	@Override
	public String getName() {
		return "Archer";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemStack(Material.BOW);
	}

	@Override
	public ItemStack getHat() {
		ItemStack item = new ItemStack(Material.STAINED_GLASS);
		item.setDurability((short) 12);
		return item;
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList("La maitrise d'un arc et de ses flèches,", "n'est pas donnée à tout le monde.");
	}

	@Override
	public int getMinDamages() {
		return 2;
	}

	@Override
	public int getMaxDamages() {
		return 4;
	}

	@Override
	public int getMaxResistance() {
		return 175;
	}

	@Override
	public int getLives() {
		return 4;
	}

}
