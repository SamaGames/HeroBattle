package net.lnfinity.HeroBattle.classes.displayers.eastereggs;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
import net.lnfinity.HeroBattle.tools.displayers.Cannon20mmTool;
import net.lnfinity.HeroBattle.tools.displayers.TripleJumpTool;
import net.lnfinity.HeroBattle.tools.displayers.weapons.Mac34SwordTool;


public class DewoitineD550Class extends DewoitineClass {
	public DewoitineD550Class(HeroBattle plugin, int cooldown, int power, int tool) {
		super(plugin);

		addTool(new Mac34SwordTool(plugin));
		addTool(new Cannon20mmTool(plugin, 2, 1, 10, 14));
		addTool(new TripleJumpTool(plugin, 2, 25));
	}

	@Override
	public String getName() {
		return "Dewoitine";
	}

	@Override
	public int getMinDamages() {
		return 10;
	}

	@Override
	public int getMaxDamages() {
		return 12;
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
		return PlayerClassType.DEWOITINE_D550;
	}
}


