package net.lnfinity.HeroBattle.powerups.powerups;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.powerups.PositivePowerup;

public class HealPowerup implements PositivePowerup {
	
	private HeroBattle p;
	
	public HealPowerup(HeroBattle plugin) {
		p = plugin;
	}

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		GamePlayer gamePlayer = p.getGamePlayer(player);
		if(gamePlayer.getLives() == gamePlayer.getPlayerClass().getLives()) {
			player.sendMessage(ChatColor.GREEN + "Vous êtes déjà en pleine forme !");
		} else {
			player.sendMessage(ChatColor.GREEN + "Vous gagnez " + ChatColor.DARK_GREEN + "1 " + ChatColor.GREEN + "vie !");
			gamePlayer.setLives(gamePlayer.getLives() + 1);
			player.setHealth(player.getHealth() + 2);
			p.getScoreboardManager().update(player);
		}
	}

	@Override
	public ItemStack getItem() {
		Potion potion = new Potion(PotionType.INSTANT_HEAL);
		return potion.toItemStack(1);
	}

	@Override
	public String getName() {
		return ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "1 VIE";
	}

	@Override
	public double getWeight() {
		return 5;
	}

}
