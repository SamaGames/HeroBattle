package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.powerups.PositivePowerup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class TripleJumpPowerup implements PositivePowerup
{
	private final HeroBattle p;

	public TripleJumpPowerup(final HeroBattle plugin)
	{
		p = plugin;
	}

	@Override
	public void onPickup(final Player player, final ItemStack pickupItem)
	{
		p.getGamePlayer(player).setMaxJumps(3, 15);
		player.sendMessage(ChatColor.GREEN + "Vous pouvez désormais faire des triple sauts !");
	}

	@Override
	public ItemStack getItem()
	{
		return new ItemStack(Material.FEATHER);
	}

	@Override
	public String getName()
	{
		return ChatColor.RED + "" + ChatColor.BOLD + "TRIPLE SAUT";
	}

	@Override
	public double getWeight()
	{
		return 10;
	}
}
