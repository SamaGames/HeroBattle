package net.lnfinity.HeroBattle.Class;

import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Tools.PowerTool;
import net.lnfinity.HeroBattle.Tools.SpeedTool;
import net.lnfinity.HeroBattle.Tools.SwordVariant1Tool;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GuerrierClass extends PlayerClass {

	public GuerrierClass(HeroBattle plugin) {
		this(plugin, 0, 0, 0);
	}

	public GuerrierClass(HeroBattle plugin, int arg1, int arg2, int arg3) {
		super(plugin);

		addTool(new SwordVariant1Tool(p, arg1));
		addTool(new SpeedTool(p, 30 -  arg1 * 2, 8 + arg2));
		addTool(new PowerTool(p, 60 - arg2 * 4, 10 + arg2));
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

	@Override
	public PlayerClassType getType() {
		return PlayerClassType.GUERRIER;
	}

}
