package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.powerups.NegativePowerup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class NauseaPowerup implements NegativePowerup {

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {

		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 8, 1, true));
		player.sendMessage(ChatColor.RED + "Blurp... :c");

	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Material.POISONOUS_POTATO);
	}

	@Override
	public String getName() {
		return ChatColor.YELLOW + "" + ChatColor.BOLD + "NAUSÉE";
	}

	@Override
	public double getWeight() {
		return 15;
	}
}
