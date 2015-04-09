package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.powerups.NegativePowerup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class ToastPowerup implements NegativePowerup {

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		Bukkit.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "TOAST... JUMP!");
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Material.ANVIL);
	}

	@Override
	public String getName() {
		return ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "TOAST";
	}

	@Override
	public double getWeight() {
		return 1;
	}
}
