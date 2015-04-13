package net.lnfinity.HeroBattle.classes.displayers;

import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
import net.lnfinity.HeroBattle.tools.displayers.AnguilleTool;
import net.lnfinity.HeroBattle.tools.displayers.PotatoTool;
import net.lnfinity.HeroBattle.tools.displayers.SwordVariant8Tool;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaiteClass extends PlayerClass {

	public MaiteClass(HeroBattle plugin) {
		super(plugin);
		
		addTool(new SwordVariant8Tool(plugin));
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
		return 4;
	}

	@Override
	public int getMaxResistance() {
		return 150;
	}

	@Override
	public int getLives() {
		return 3;
	}

	@Override
	public PlayerClassType getType() {
		return null;
	}

}
