package net.lnfinity.HeroBattle.tools.displayers.weapons;

import net.lnfinity.HeroBattle.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;


public class WisdomStickSwordTool extends SwordTool
{

	public WisdomStickSwordTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.sword.wisdomStick";
	}

	@Override
	public String getName()
	{
		return ChatColor.WHITE + "" + ChatColor.BOLD + "Bâton de la sagesse";
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.STICK, 1);

		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);

		return item;
	}
}
