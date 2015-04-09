package net.lnfinity.HeroBattle.Powerups.powerups;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Game.GamePlayer;
import net.lnfinity.HeroBattle.Powerups.PositivePowerup;
import net.md_5.bungee.api.ChatColor;

public class TripleJumpPowerup implements PositivePowerup {

	private HeroBattle p;
	
	public TripleJumpPowerup(HeroBattle plugin) {
		p = plugin;
	}
	
	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		final GamePlayer gamePlayer = p.getGamePlayer(player);
		player.sendMessage(ChatColor.GREEN + "Vous pouvez désormais faire des triple sauts !");
		gamePlayer.setMaxJumps(3);
		
		p.getServer().getScheduler().runTaskLater(p, new Runnable() {
			@Override
			public void run() {
				gamePlayer.setMaxJumps(2);
			}
		}, 15 * 20L);
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Material.FEATHER);
	}

	@Override
	public String getName() {
		return ChatColor.RED + "" + ChatColor.BOLD + "TRIPLE SAUT";
	}

	@Override
	public double getWeight() {
		return 10;
	}

}
