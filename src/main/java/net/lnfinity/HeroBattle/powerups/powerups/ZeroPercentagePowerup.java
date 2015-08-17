package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.powerups.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

public class ZeroPercentagePowerup implements PositivePowerup {
	
	private HeroBattle p;
	
	public ZeroPercentagePowerup(HeroBattle plugin) {
		p = plugin;
	}

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		HeroBattlePlayer heroBattlePlayer = p.getGamePlayer(player);
		heroBattlePlayer.setPercentage(0, null);

		player.sendMessage(ChatColor.GREEN + "Votre pourcentage a été remis à " + ChatColor.DARK_GREEN + "0 " + ChatColor.GREEN + "!");
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
