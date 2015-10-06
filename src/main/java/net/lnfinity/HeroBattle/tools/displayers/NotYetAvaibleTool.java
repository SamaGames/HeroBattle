package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.md_5.bungee.api.ChatColor;
import net.samagames.tools.Titles;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;


public class NotYetAvaibleTool extends PlayerTool
{

	public NotYetAvaibleTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.notyet";
	}

	@Override
	public String getName()
	{
		return ChatColor.RED + "" + ChatColor.BOLD + "?¿?";
	}

	@Override
	public List<String> getDescription()
	{
		return Arrays.asList(ChatColor.GRAY + "Cet objet n'est pas encore disponible !", ChatColor.GRAY + "Son arrivée est imminente.");
	}

	@Override
	public ItemStack getItem()
	{
		return new ItemStack(Material.BARRIER);
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		Titles.sendTitle(player, 10, 40, 10, ChatColor.RED + "Nope", ChatColor.RED + "Pas encore disponible");
	}
}
