package net.lnfinity.HeroBattle.tools.displayers.weapons;

import net.lnfinity.HeroBattle.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;


public class Mac34SwordTool extends SwordTool
{
	public Mac34SwordTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.sword.mac34";
	}

	@Override
	public String getName()
	{
		return ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "MAC-34";
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.DIAMOND_HOE, 1);

		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);

		return item;
	}
}
