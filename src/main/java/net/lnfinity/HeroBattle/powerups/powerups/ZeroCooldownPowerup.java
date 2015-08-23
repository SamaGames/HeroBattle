package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.powerups.PositivePowerup;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ZeroCooldownPowerup implements PositivePowerup {

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		player.sendMessage(ChatColor.GREEN + "Toutes vos capacités ont été rafraichies !");
		for(int i = 1; i < 9; i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if(stack != null) {
				ToolsUtils.resetTool(stack);
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
