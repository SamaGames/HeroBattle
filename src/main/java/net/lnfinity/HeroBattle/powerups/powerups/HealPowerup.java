package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import net.lnfinity.HeroBattle.powerups.PositivePowerup;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;


public class HealPowerup implements PositivePowerup
{
	private final HeroBattle p;

	public HealPowerup(final HeroBattle plugin)
	{
		p = plugin;
	}

	@Override
	public void onPickup(final Player player, final ItemStack pickupItem)
	{
		final HeroBattlePlayer heroBattlePlayer = p.getGamePlayer(player);

		player.sendMessage(ChatColor.GREEN + "Vous gagnez " + ChatColor.DARK_GREEN + "une " + ChatColor.GREEN + "vie !");
		heroBattlePlayer.gainLife();
		p.getScoreboardManager().refresh();
	}

	@Override
	public ItemStack getItem()
	{
		return new Potion(PotionType.INSTANT_HEAL).toItemStack(1);
	}

	@Override
	public String getName()
	{
		return ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "1 VIE";
	}

	@Override
	public double getWeight()
	{
		return 5;
	}
}
