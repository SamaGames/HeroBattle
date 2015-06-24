package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.powerups.PositivePowerup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TripleJumpPowerup implements PositivePowerup {

	private HeroBattle p;
	
	public TripleJumpPowerup(HeroBattle plugin) {
		p = plugin;
	}
	
	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		final GamePlayer gamePlayer = p.getGamePlayer(player);

        if(gamePlayer != null) {
            player.sendMessage(ChatColor.GREEN + "Vous pouvez désormais faire des triple sauts !");
            gamePlayer.setMaxJumps(3, 15);
        }
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
