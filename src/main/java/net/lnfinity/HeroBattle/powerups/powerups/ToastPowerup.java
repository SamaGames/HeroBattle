package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.powerups.NegativePowerup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;


public class ToastPowerup implements NegativePowerup {

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {

		player.setVelocity(player.getVelocity().add(new Vector(0, (new Random()).nextDouble() + 3, 0)));
		player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.8f, 1);

		player.sendMessage(ChatColor.RED + "Wooosh !");

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
