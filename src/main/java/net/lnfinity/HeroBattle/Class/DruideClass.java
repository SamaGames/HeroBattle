package net.lnfinity.HeroBattle.Class;

import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Tools.HealingTool;
import net.lnfinity.HeroBattle.Tools.InkTool;
import net.lnfinity.HeroBattle.Tools.SwordVariant4Tool;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DruideClass extends PlayerClass {

	public DruideClass(HeroBattle plugin) {
		this(plugin, 0, 0, 0);
	}
	
	public DruideClass(HeroBattle plugin, int arg1, int arg2, int arg3) {
		super(plugin);
		
		addTool(new SwordVariant4Tool(plugin));
		addTool(new HealingTool(plugin, 45 - arg1 * 2, 50 + arg2 * 4, 0.4 + arg2 * 0.5));
		addTool(new InkTool(plugin, 60 - arg1 * 2, 10 + arg2, 0.25 - arg2 * 0.05));
	}

	@Override
	public String getName() {
		return "Druide";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemStack(Material.GOLDEN_APPLE);
	}

	@Override
	public ItemStack getHat() {
		ItemStack item = new ItemStack(Material.STAINED_GLASS);
		item.setDurability((short) 13);
		return item;
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList("La capacité à pouvoir se soigner.");
	}

	@Override
	public int getMinDamages() {
		return 4;
	}

	@Override
	public int getMaxDamages() {
		return 6;
	}

	@Override
	public int getMaxResistance() {
		return 200;
	}

	@Override
	public int getLives() {
		return 4;
	}

	@Override
	public PlayerClassType getType() {
		return PlayerClassType.DRUIDE;
	}

}
