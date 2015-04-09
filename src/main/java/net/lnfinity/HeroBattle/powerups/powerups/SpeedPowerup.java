package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.powerups.PositivePowerup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class SpeedPowerup implements PositivePowerup {

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 0));
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Material.SUGAR);
	}

	@Override
	public String getName() {
		return ChatColor.DARK_PURPLE + "VITESSE" + ChatColor.GREEN + " DIX SECONDES";
	}

	@Override
	public double getWeight() {
		return 25;
	}
}
