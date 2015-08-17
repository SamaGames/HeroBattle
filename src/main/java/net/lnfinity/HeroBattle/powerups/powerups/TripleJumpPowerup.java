package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.powerups.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

public class TripleJumpPowerup implements PositivePowerup {

	private HeroBattle p;
	
	public TripleJumpPowerup(HeroBattle plugin) {
		p = plugin;
	}
	
	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		final HeroBattlePlayer heroBattlePlayer = p.getGamePlayer(player);

		if (heroBattlePlayer != null)
		{
			player.sendMessage(ChatColor.GREEN + "Vous pouvez désormais faire des triple sauts !");
			heroBattlePlayer.setMaxJumps(3, 15);
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
