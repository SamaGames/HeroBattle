package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.powerups.NegativePowerup;
import net.lnfinity.HeroBattle.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class PercentageIncreasedPowerup implements NegativePowerup {

	HeroBattle p;

	public PercentageIncreasedPowerup(HeroBattle plugin) {
		p = plugin;
	}

	@Override
	public void onPickup(final Player player, ItemStack pickupItem) {

		GamePlayer gPlayer = p.getGamePlayer(player);

		int percentageIncrease = Utils.randomNumber(5, 30);

		gPlayer.setPercentage(gPlayer.getPercentage() + percentageIncrease);
		player.sendMessage(ChatColor.RED + "Votre pourcentage augmente de " + ChatColor.DARK_RED + percentageIncrease + ChatColor.RED + " points !");

		p.getServer().getScheduler().runTaskTimer(p, new Runnable() {
			float pitch = 1f;

			@Override
			public void run() {
				player.playSound(player.getLocation(), Sound.CLICK, 1, pitch);
				pitch += 0.1f;
			}
		}, 1l, 2l);
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Material.FEATHER);
	}

	@Override
	public String getName() {
		return ChatColor.AQUA + "" + ChatColor.BOLD + "POURCENTAGE AUGMENTÉ";
	}

	@Override
	public double getWeight() {
		return 15;
	}
}
