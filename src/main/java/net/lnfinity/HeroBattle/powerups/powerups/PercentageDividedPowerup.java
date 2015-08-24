package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import net.lnfinity.HeroBattle.powerups.PositivePowerup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;


public class PercentageDividedPowerup implements PositivePowerup
{
	private final HeroBattle p;

	public PercentageDividedPowerup(final HeroBattle plugin)
	{
		p = plugin;
	}

	@Override
	public void onPickup(final Player player, final ItemStack pickupItem)
	{
		final HeroBattlePlayer gPlayer = p.getGamePlayer(player);
		gPlayer.setPercentage(gPlayer.getPercentage() / 2, null);

		player.sendMessage(ChatColor.GREEN + "Votre pourcentage a été divisé par deux !");
	}

	@Override
	public ItemStack getItem()
	{
		return new Potion(PotionType.INSTANT_HEAL).toItemStack(1);
	}

	@Override
	public String getName()
	{
		return ChatColor.AQUA + "" + ChatColor.BOLD + "POURCENTAGE COUPÉ EN DEUX";
	}

	@Override
	public double getWeight()
	{
		return 10;
	}
}
