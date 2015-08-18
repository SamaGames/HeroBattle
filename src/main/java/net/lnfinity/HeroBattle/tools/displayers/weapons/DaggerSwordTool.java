package net.lnfinity.HeroBattle.tools.displayers.weapons;

import net.lnfinity.HeroBattle.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;


public class DaggerSwordTool extends SwordTool
{

	public DaggerSwordTool(HeroBattle plugin, int arg1)
	{
		super(plugin);
		upgrade = arg1;
	}

	@Override
	public String getToolID()
	{
		return "tool.sword.dagger";
	}

	@Override
	public String getName()
	{
		return ChatColor.RED + "" + ChatColor.BOLD + "Poignard";
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.DIAMOND_SWORD, 1);

		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);

		return item;
	}

}
