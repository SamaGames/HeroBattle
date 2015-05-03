package net.lnfinity.HeroBattle.classes.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
import net.lnfinity.HeroBattle.tools.displayers.IceTool;
import net.lnfinity.HeroBattle.tools.displayers.InvincibleTool;
import net.lnfinity.HeroBattle.tools.displayers.SwordVariant5Tool;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CryogenieClass extends PlayerClass {
	
	public CryogenieClass(HeroBattle plugin) {
		this(plugin, 0, 0, 0);
	}

	public CryogenieClass(HeroBattle plugin, int arg1, int arg2, int arg3) {
		super(plugin, arg1, arg2, arg3);

		addTool(new SwordVariant5Tool(p));
		addTool(new InvincibleTool(p, 60 - arg1 * 2, 8 + arg2));
		addTool(new IceTool(p, 30 - arg1 * 2, 6 + arg2));
	}

	@Override
	public String getName() {
		return "Cryogénie";
	}

	@Override
	public ItemStack getIcon() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		item.setDurability((short) 7);
		return item;
	}

	@Override
	public ItemStack getHat() {
		ItemStack item = new ItemStack(Material.STAINED_GLASS);
		item.setDurability((short) 0);
		return item;
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList("Maître des contrées du froid", "", ChatColor.GRAY + "Classe de type " + ChatColor.GOLD + "Mêlée", ChatColor.GREEN + "+ " + ChatColor.GRAY + "Très résistant, défensif", ChatColor.RED + "- " + ChatColor.GRAY + "Combats distants");
	}

	@Override
	public int getMinDamages() {
		return 5;
	}

	@Override
	public int getMaxDamages() {
		return 7;
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
		return PlayerClassType.CRYOGENIE;
	}

}
