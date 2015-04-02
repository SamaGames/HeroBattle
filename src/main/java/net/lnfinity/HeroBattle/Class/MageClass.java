package net.lnfinity.HeroBattle.Class;

import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Tools.SmokeTool;
import net.lnfinity.HeroBattle.Tools.SwordVariant2Tool;
import net.lnfinity.HeroBattle.Tools.ThunderTool;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MageClass extends PlayerClass {

	public MageClass(HeroBattle plugin) {
		this(plugin, 0, 0, 0);
	}

	public MageClass(HeroBattle plugin, int arg1, int arg2, int arg3) {
		super(plugin);

		addTool(new SwordVariant2Tool(p));
		addTool(new SmokeTool(p, 30 - arg1 * 2, 8 + arg2));
		addTool(new ThunderTool(p, 120 - arg1 * 4));
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

	@Override
	public PlayerClassType getType() {
		return PlayerClassType.MAGE;
	}

}
