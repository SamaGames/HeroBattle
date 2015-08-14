package net.lnfinity.HeroBattle.classes.displayers.free;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
import net.lnfinity.HeroBattle.tools.displayers.NotYetAvaibleTool;
import net.lnfinity.HeroBattle.tools.displayers.SmokeTool;
import net.lnfinity.HeroBattle.tools.displayers.SpeedTool;
import net.lnfinity.HeroBattle.tools.displayers.weapons.MagicWandSwordTool;
import net.lnfinity.HeroBattle.tools.displayers.ThunderTool;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class MageClass extends PlayerClass {

	public MageClass(HeroBattle plugin) {
		this(plugin, 0, 0, 0);
	}

	public MageClass(HeroBattle plugin, int arg1, int arg2, int arg3) {
		super(plugin, arg1, arg2, arg3);

		addTool(new MagicWandSwordTool(p));
		addTool(new SmokeTool(p, 30 - arg1 * 2, 8 + arg2));
		addTool(new ThunderTool(p, 60 - arg1 * 4, 25 + 2 * arg2, 50 + 2 * arg2));
		if(arg3 >= 1) addTool(new SpeedTool(p, 30 - arg1, (int) Math.floor(1 + arg2), 4 + arg2));
		if(arg3 >= 2) addTool(new NotYetAvaibleTool(p));
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
		return Arrays.asList("La magie, il maitrise.", "", ChatColor.GRAY + "Classe de type " + ChatColor.GOLD + "Magie", ChatColor.GREEN + "+ " + ChatColor.GRAY + "Très puissant, agile", ChatColor.RED + "- " + ChatColor.GRAY + "Faible résistance");
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
		return 200;
	}

	@Override
	public int getLives() {
		return 2;
	}

	@Override
	public PlayerClassType getType() {
		return PlayerClassType.MAGE;
	}

}
