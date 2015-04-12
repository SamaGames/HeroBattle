package net.lnfinity.HeroBattle.classes;

import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.EarthquakeTool;
import net.lnfinity.HeroBattle.tools.IceTool;
import net.lnfinity.HeroBattle.tools.InvincibleTool;
import net.lnfinity.HeroBattle.tools.NauseaTool;
import net.lnfinity.HeroBattle.tools.SwordTool;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BruteClass extends PlayerClass {

	public BruteClass(HeroBattle plugin) {
		this(plugin, 0, 0, 0);
	}

	public BruteClass(HeroBattle plugin, int arg1, int arg2, int arg3) {
		super(plugin);

		addTool(new SwordTool(p));
		addTool(new EarthquakeTool(p, 30 - arg1 * 2));
		addTool(new NauseaTool(p, 60 - arg1 * 4, 10 + arg2, 0.25 - arg2 * 0.05));
		addTool(new IceTool(p, 10, 5));
		addTool(new InvincibleTool(p, 10, 5));
	}

	@Override
	public String getName() {
		return "Brute";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemStack(Material.DIAMOND_CHESTPLATE);
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList("Pour le plaisir de faire des dégâts.");
	}

	@Override
	public int getMinDamages() {
		return 1;
	}

	@Override
	public int getMaxDamages() {
		return 8;
	}

	@Override
	public int getLives() {
		return 3;
	}

	@Override
	public ItemStack getHat() {
		ItemStack item = new ItemStack(Material.STAINED_GLASS);
		item.setDurability((short) 14);
		return item;
	}

	@Override
	public int getMaxResistance() {
		return 200;
	}

	@Override
	public PlayerClassType getType() {
		return PlayerClassType.BRUTE;
	}

}
