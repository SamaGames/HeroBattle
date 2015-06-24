package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.powerups.PositivePowerup;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PowerPowerup implements PositivePowerup {

	private HeroBattle p;
	
	public PowerPowerup(HeroBattle plugin) {
		p = plugin;
	}
	
	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		final GamePlayer gamePlayer = p.getGamePlayer(player);
		player.sendMessage(ChatColor.GREEN + "Vous " + ChatColor.DARK_GREEN + "doublez votre puissance" + ChatColor.GREEN + " pour 24 secondes !");
		gamePlayer.addRemainingDoubleDamages(24);
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Material.BLAZE_POWDER);
	}

	@Override
	public String getName() {
		return ChatColor.DARK_RED + "" + ChatColor.BOLD + "FORCE";
	}

	@Override
	public double getWeight() {
		return 25;
	}

}
