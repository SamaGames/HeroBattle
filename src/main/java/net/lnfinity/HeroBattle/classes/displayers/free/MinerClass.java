package net.lnfinity.HeroBattle.classes.displayers.free;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
import net.lnfinity.HeroBattle.tools.displayers.CrackTool;
import net.lnfinity.HeroBattle.tools.displayers.MinerSpecialTool;
import net.lnfinity.HeroBattle.tools.displayers.TNTTool;
import net.lnfinity.HeroBattle.tools.displayers.weapons.DrillSwordTool;
import net.md_5.bungee.api.ChatColor;

public class MinerClass extends PlayerClass {

	public MinerClass(HeroBattle plugin) {
		super(plugin, 0, 0, 0);
	}
	
	public MinerClass(HeroBattle plugin, int cooldown, int power, int tool) {
		super(plugin, cooldown, power, tool);
		
		addTool(new DrillSwordTool(p));
		addTool(new TNTTool(p, 25 - cooldown, (int) (6 - power * 0.5)));
		addTool(new CrackTool(p, 55 - cooldown * 2, 25 + power * 2));
		if(tool >= 1) addTool(new MinerSpecialTool(p, 30 - cooldown, (int) (5 + 0.5 * power)));
	}

	@Override
	public String getName() {
		return "Mineur";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemStack(Material.DIAMOND_PICKAXE);
	}

	@Override
	public ItemStack getHat() {
		return new ItemStack(Material.STONE);
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList("Souvent peu redouté au corps à corps,", "il peut s'avérérer très puissant !", "", ChatColor.GRAY + "Classe de type " + ChatColor.GOLD + "Equilibrée", ChatColor.GREEN + "+ " + ChatColor.GRAY + "Corps à corps, distance", ChatColor.RED + "- " + ChatColor.GRAY + "Peu résistant, peu agile");
	}

	@Override
	public int getMinDamages() {
		return 4;
	}

	@Override
	public int getMaxDamages() {
		return 8;
	}

	@Override
	public int getMaxResistance() {
		return 160;
	}

	@Override
	public int getLives() {
		return 3;
	}

	@Override
	public PlayerClassType getType() {
		return PlayerClassType.MINEUR;
	}

}
