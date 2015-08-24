package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import net.lnfinity.HeroBattle.powerups.NegativePowerup;
import net.lnfinity.HeroBattle.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;


public class PercentageIncreasedPowerup implements NegativePowerup
{
	private final HeroBattle p;

	public PercentageIncreasedPowerup(final HeroBattle plugin)
	{
		p = plugin;
	}

	@Override
	public void onPickup(final Player player, final ItemStack pickupItem)
	{
		final HeroBattlePlayer gPlayer = p.getGamePlayer(player);

		if (gPlayer.getRemainingReducingIncomingDamages() != 0)
		{
			player.sendMessage(ChatColor.GREEN + "Votre invulnérabilité vous sauve..." + ChatColor.RED + " pour cette fois. :>");
			return;
		}


		final int percentageIncrease = Utils.randomNumber(5, 30);

		gPlayer.setPercentage(gPlayer.getPercentage() + percentageIncrease, null);
		player.sendMessage(ChatColor.RED + "Votre pourcentage augmente de " + ChatColor.DARK_RED + percentageIncrease + ChatColor.RED + " points !");

		new BukkitRunnable()
		{
			float pitch = 0.4f;
			int counter = 0;

			@Override
			public void run()
			{
				player.playSound(player.getLocation(), Sound.CLICK, 1, pitch);
				pitch += 0.1f;
				counter++;

				if (counter >= percentageIncrease) cancel();
			}
		}.runTaskTimer(p, 1l, 2l);
	}

	@Override
	public ItemStack getItem()
	{
		return new ItemStack(Material.FEATHER);
	}

	@Override
	public String getName()
	{
		return ChatColor.AQUA + "" + ChatColor.BOLD + "POURCENTAGE AUGMENTÉ";
	}

	@Override
	public double getWeight()
	{
		return 15;
	}
}
