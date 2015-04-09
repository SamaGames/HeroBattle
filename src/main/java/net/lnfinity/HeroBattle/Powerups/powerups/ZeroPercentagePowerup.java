package net.lnfinity.HeroBattle.Powerups.powerups;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Game.GamePlayer;
import net.lnfinity.HeroBattle.Powerups.PositivePowerup;

public class ZeroPercentagePowerup implements PositivePowerup {
	
	private HeroBattle p;
	
	public ZeroPercentagePowerup(HeroBattle plugin) {
		p = plugin;
	}

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		GamePlayer gamePlayer = p.getGamePlayer(player);
		player.sendMessage(ChatColor.GREEN + "Votre pourcentage a été remis à " + ChatColor.DARK_GREEN + "0 " + ChatColor.GREEN + "!");
		gamePlayer.setPercentage(0);
		player.setLevel(0);
		p.getScoreboardManager().update(player);
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Material.NETHER_STAR);
	}

	@Override
	public String getName() {
		return ChatColor.AQUA + "" + ChatColor.BOLD + "POURCENTAGE À ZÉRO";
	}

	@Override
	public double getWeight() {
		return 5;
	}

}
