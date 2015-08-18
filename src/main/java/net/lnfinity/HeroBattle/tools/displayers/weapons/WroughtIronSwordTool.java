package net.lnfinity.HeroBattle.tools.displayers.weapons;

import net.lnfinity.HeroBattle.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;


public class WroughtIronSwordTool extends SwordTool
{

	public WroughtIronSwordTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.sword.wroughtIron";
	}

	@Override
	public String getName()
	{
		return ChatColor.RED + "" + ChatColor.BOLD + "Épée en fer forgé";
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.IRON_SWORD, 1);

		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);

		return item;
	}
}
