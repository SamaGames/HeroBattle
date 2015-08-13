package net.lnfinity.HeroBattle.classes.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.*;
import net.lnfinity.HeroBattle.tools.displayers.TripleJumpTool;
import net.lnfinity.HeroBattle.tools.displayers.Cannon20mmTool;
import net.lnfinity.HeroBattle.tools.displayers.weapons.SwordVariant9Tool;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;


public class DewoitineClass extends EasterEggPlayerClass {

	public DewoitineClass(HeroBattle plugin) {
		super(plugin, 0, 0, 0);
	}

	public DewoitineClass(HeroBattle plugin, int cooldown, int power, int tool) {
		super(plugin, cooldown, power, tool);

		addTool(new SwordVariant9Tool(plugin));
		addTool(new Cannon20mmTool(plugin, 12, 1));
		addTool(new TripleJumpTool(plugin, 20, 4));
	}

	@Override
	public String getName() {
		return "Dewoitine";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemStack(Material.FEATHER);
	}

	@Override
	public ItemStack getHat() {
		return new ItemStack(Material.SLIME_BLOCK);
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList("Dewoitine");
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
		return 200;
	}

	@Override
	public int getLives() {
		return 3;
	}

	@Override
	public PlayerClassType getType() {
		return PlayerClassType.DEWOITINE;
	}
}
