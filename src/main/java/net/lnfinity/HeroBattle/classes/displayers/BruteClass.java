package net.lnfinity.HeroBattle.classes.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
import net.lnfinity.HeroBattle.tools.displayers.EarthquakeTool;
import net.lnfinity.HeroBattle.tools.displayers.NauseaTool;
import net.lnfinity.HeroBattle.tools.displayers.SwordTool;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class BruteClass extends PlayerClass {

	public BruteClass(HeroBattle plugin) {
		this(plugin, 0, 0, 0);
	}

	public BruteClass(HeroBattle plugin, int arg1, int arg2, int arg3) {
		super(plugin, arg1, arg2, arg3);

		addTool(new SwordTool(p));
		addTool(new EarthquakeTool(p, 30 - arg1 * 2));
		addTool(new NauseaTool(p, 60 - arg1 * 4, 10 + arg2, 0.25 - arg2 * 0.05));
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
		return Arrays.asList("Pour le plaisir de faire des dégâts.", "", ChatColor.GRAY + "Classe de type " + ChatColor.GOLD + "Mêlée", ChatColor.GREEN + "+ " + ChatColor.GRAY + "Dégâts de masse, résistant", ChatColor.RED + "- " + ChatColor.GRAY + "Précision, agilité");
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
