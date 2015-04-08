package net.lnfinity.HeroBattle.Powerups.powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Powerups.Powerup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class ToastPowerup extends Powerup {
	public ToastPowerup(HeroBattle plugin) {
		super(plugin);
	}

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		Bukkit.getServer().broadcastMessage("TOAST");
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Material.ANVIL);
	}

	@Override
	public String getName() {
		return ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "TOAST";
	}
}
