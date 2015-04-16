package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.powerups.PositivePowerup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;


public class PercentageDividedPowerup implements PositivePowerup {

	private HeroBattle p;

	public PercentageDividedPowerup(HeroBattle plugin) {
		p = plugin;
	}

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		GamePlayer gPlayer = p.getGamePlayer(player);
		gPlayer.setPercentage(gPlayer.getPercentage() / 2, null);
		p.getScoreboardManager().update(gPlayer);

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
