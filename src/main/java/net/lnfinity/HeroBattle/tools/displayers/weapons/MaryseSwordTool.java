package net.lnfinity.HeroBattle.tools.displayers.weapons;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class MaryseSwordTool extends SwordTool
{

	public MaryseSwordTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.sword.maryse";
	}

	@Override
	public String getName()
	{
		return ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Maryse";
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.WOOD_SPADE, 1);

		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);

		return item;
	}
}
