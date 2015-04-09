package net.lnfinity.HeroBattle.Powerups.powerups;

import net.lnfinity.HeroBattle.Game.GamePlayer;
import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Powerups.PositivePowerup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class PercentageDividedPowerup implements PositivePowerup {

	private HeroBattle p;

	public PercentageDividedPowerup(HeroBattle plugin) {
		p = plugin;
	}

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		GamePlayer gPlayer = p.getGamePlayer(player);
		gPlayer.setPercentage(gPlayer.getPercentage() / 2);
		p.getScoreboardManager().update(gPlayer);

		player.sendMessage(ChatColor.GREEN + "Votre pourcentage a été divisé par deux !");
	}

	@Override
	public ItemStack getItem() {
		ItemStack icon = new ItemStack(Material.POTION);
		PotionMeta meta = ((PotionMeta) icon.getItemMeta());

		meta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1, 1), true);

		icon.setItemMeta(meta);

		return icon;
	}

	@Override
	public String getName() {
		return ChatColor.GREEN + "" + ChatColor.BOLD + "KNOCKBACK : "
				+ ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "-50 %";
	}

	@Override
	public double getWeight() {
		return 1;
	}
}
