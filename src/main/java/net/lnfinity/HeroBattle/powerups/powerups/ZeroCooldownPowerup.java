package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.powerups.PositivePowerup;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.md_5.bungee.api.ChatColor;
import net.samagames.tools.GlowEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class ZeroCooldownPowerup implements PositivePowerup
{

	@Override
	public void onPickup(final Player player, final ItemStack pickupItem)
	{
		player.sendMessage(ChatColor.GREEN + "Toutes vos capacités ont été rafraichies !");

		for (int i = 1; i < 9; i++)
		{
			final ItemStack stack = player.getInventory().getItem(i);
			if (stack != null && stack.getType() != Material.AIR)
			{
				ToolsUtils.resetTool(stack);
			}
		}
	}

	@Override
	public ItemStack getItem()
	{
		final ItemStack item = new ItemStack(Material.CHEST);
		GlowEffect.addGlow(item);

		return item;
	}

	@Override
	public String getName()
	{
		return ChatColor.GOLD + "" + ChatColor.BOLD + "CAPACITÉS RAFRAICHIES";
	}

	@Override
	public double getWeight()
	{
		return 10;
	}
}
