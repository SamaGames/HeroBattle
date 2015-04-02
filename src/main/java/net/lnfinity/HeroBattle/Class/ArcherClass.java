package net.lnfinity.HeroBattle.Class;

import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Tools.ArrowsTool;
import net.lnfinity.HeroBattle.Tools.SpeedTool;
import net.lnfinity.HeroBattle.Tools.SwordVariant3Tool;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ArcherClass extends PlayerClass {

	public ArcherClass(HeroBattle plugin) {
		this(plugin, 0, 0, 0);
	}

	public ArcherClass(HeroBattle plugin, int arg1, int arg2, int arg3) {
		super(plugin);

		addTool(new SwordVariant3Tool(p));
		addTool(new SpeedTool(p, 20 - arg1 * 2, 5 + arg2));
		addTool(new ArrowsTool(p, 45 - 3 * arg1, 3 + arg2));
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

	@Override
	public PlayerClassType getType() {
		return PlayerClassType.ARCHER;
	}

}
