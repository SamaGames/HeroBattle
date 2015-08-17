package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.powerups.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

public class PowerPowerup implements PositivePowerup {

	private HeroBattle p;
	
	public PowerPowerup(HeroBattle plugin) {
		p = plugin;
	}
	
	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		final HeroBattlePlayer heroBattlePlayer = p.getGamePlayer(player);
		player.sendMessage(ChatColor.GREEN + "Vous " + ChatColor.DARK_GREEN + "doublez votre puissance" + ChatColor.GREEN + " pour 24 secondes !");
		heroBattlePlayer.addRemainingDoubleDamages(24);
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
