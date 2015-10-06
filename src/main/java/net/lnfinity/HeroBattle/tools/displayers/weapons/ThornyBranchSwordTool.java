package net.lnfinity.HeroBattle.tools.displayers.weapons;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class ThornyBranchSwordTool extends SwordTool
{

	public ThornyBranchSwordTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.sword.branch";
	}

	@Override
	public String getName()
	{
		return ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Branche épineuse";
	}

	@Override
	public ItemStack getItem()
	{
		return new ItemStack(Material.DEAD_BUSH);
	}

}
