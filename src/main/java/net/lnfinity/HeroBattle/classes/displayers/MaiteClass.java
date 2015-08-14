package net.lnfinity.HeroBattle.classes.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.*;
import net.lnfinity.HeroBattle.tools.displayers.AnguilleTool;
import net.lnfinity.HeroBattle.tools.displayers.PotatoTool;
import net.lnfinity.HeroBattle.tools.displayers.weapons.MaryseSwordTool;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class MaiteClass extends EasterEggPlayerClass {

	public MaiteClass(HeroBattle plugin) {
		super(plugin, 0, 0, 0);
		
		addTool(new MaryseSwordTool(plugin));
		addTool(new AnguilleTool(plugin));
		addTool(new PotatoTool(plugin));
	}

	@Override
	public String getName() {
		return "Maïté";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemStack(Material.POISONOUS_POTATO);
	}

	@Override
	public ItemStack getHat() {
		return new ItemStack(Material.JACK_O_LANTERN);
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList("Hmmmm... Fameux hein ?");
	}

	@Override
	public int getMinDamages() {
		return 2;
	}

	@Override
	public int getMaxDamages() {
		return 8;
	}

	@Override
	public int getMaxResistance() {
		return 200;
	}

	@Override
	public int getLives() {
		return 3;
	}

	@Override
	public PlayerClassType getType() {
		return PlayerClassType.MAITE;
	}

}
