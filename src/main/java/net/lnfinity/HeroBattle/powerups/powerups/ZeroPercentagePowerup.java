package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.powerups.PositivePowerup;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class ZeroPercentagePowerup implements PositivePowerup
{

	private final HeroBattle p;

	public ZeroPercentagePowerup(final HeroBattle plugin)
	{
		p = plugin;
	}

	@Override
	public void onPickup(final Player player, final ItemStack pickupItem)
	{
		p.getGamePlayer(player).setPercentage(0, null);

		player.sendMessage(ChatColor.GREEN + "Votre pourcentage a été remis à " + ChatColor.DARK_GREEN + "0 " + ChatColor.GREEN + "!");
	}

	@Override
	public ItemStack getItem()
	{
		return new ItemStack(Material.NETHER_STAR);
	}

	@Override
	public String getName()
	{
		return ChatColor.AQUA + "" + ChatColor.BOLD + "POURCENTAGE À ZÉRO";
	}

	@Override
	public double getWeight()
	{
		return 5;
	}
}
