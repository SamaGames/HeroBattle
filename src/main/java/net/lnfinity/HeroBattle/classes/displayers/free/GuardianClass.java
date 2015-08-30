package net.lnfinity.HeroBattle.classes.displayers.free;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
import net.lnfinity.HeroBattle.tools.displayers.FruitTool;
import net.lnfinity.HeroBattle.tools.displayers.RootTool;
import net.lnfinity.HeroBattle.tools.displayers.weapons.ThornyBranchSwordTool;
import net.md_5.bungee.api.ChatColor;

public class GuardianClass extends PlayerClass {

	public GuardianClass(HeroBattle plugin) {
		this(plugin, 0, 0, 0);
	}
	
	public GuardianClass(HeroBattle plugin, int cooldown, int power, int tool) {
		super(plugin, cooldown, power, tool);
		
		addTool(new ThornyBranchSwordTool(p));
		addTool(new FruitTool(p, 25 - cooldown, 10 + power));
		addTool(new RootTool(p, 45 - cooldown, 38 + power, 46 + power));
		
		if(tool >= 1); // TODO 3rd Tool
	}

	@Override
	public String getName() {
		return "Gardien";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemStack(Material.LONG_GRASS, 1, (byte) 2);
	}

	@Override
	public ItemStack getHat() {
		return new ItemStack(Material.LEAVES);
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList("La force de la nature.", "", ChatColor.GRAY + "Classe de type " + ChatColor.GOLD + "Distant", ChatColor.GREEN + "+ " + ChatColor.GRAY + "Résistant, précision, dégâts moyenne portée", ChatColor.RED + "- " + ChatColor.GRAY + "Rapidité d'attaque, corps à corps");
	}

	@Override
	public int getMinDamages() {
		return 2;
	}

	@Override
	public int getMaxDamages() {
		return 5;
	}

	@Override
	public int getMaxResistance() {
		return 180;
	}

	@Override
	public int getLives() {
		return 3;
	}

	@Override
	public PlayerClassType getType() {
		return PlayerClassType.GARDIEN;
	}

}
