package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.powerups.*;
import net.md_5.bungee.api.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;


public class PercentageDividedPowerup implements PositivePowerup {

	private HeroBattle p;

	public PercentageDividedPowerup(HeroBattle plugin) {
		p = plugin;
	}

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		HeroBattlePlayer gPlayer = p.getGamePlayer(player);
		gPlayer.setPercentage(gPlayer.getPercentage() / 2, null);

		player.sendMessage(ChatColor.GREEN + "Votre pourcentage a été divisé par deux !");
	}

	@Override
	public ItemStack getItem() {
		Potion potion = new Potion(PotionType.INSTANT_HEAL);
		return potion.toItemStack(1);
	}

	@Override
	public String getName() {
		return ChatColor.AQUA + "" + ChatColor.BOLD + "POURCENTAGE COUPÉ EN DEUX";
	}

	@Override
	public double getWeight() {
		return 10;
	}
}
