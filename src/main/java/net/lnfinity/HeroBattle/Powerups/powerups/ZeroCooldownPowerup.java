package net.lnfinity.HeroBattle.Powerups.powerups;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.lnfinity.HeroBattle.Powerups.PositivePowerup;
import net.md_5.bungee.api.ChatColor;
import net.samagames.utils.GlowEffect;

public class ZeroCooldownPowerup implements PositivePowerup {

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		for(int i = 1; i < 9; i++) {
			if(player.getInventory().getItem(i) != null) {
				player.getInventory().getItem(i).setAmount(1);
				GlowEffect.addGlow(player.getInventory().getItem(i));
			}
		}
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.CHEST);
		GlowEffect.addGlow(item);
		return new ItemStack(item);
	}

	@Override
	public String getName() {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "CAPACITÉS RAFRAICHIES";
	}

	@Override
	public double getWeight() {
		return 10;
	}

}
