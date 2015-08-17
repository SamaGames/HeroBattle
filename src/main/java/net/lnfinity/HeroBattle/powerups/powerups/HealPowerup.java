package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.powerups.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;

public class HealPowerup implements PositivePowerup {
	
	private HeroBattle p;
	
	public HealPowerup(HeroBattle plugin) {
		p = plugin;
	}

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		HeroBattlePlayer heroBattlePlayer = p.getGamePlayer(player);

		player.sendMessage(ChatColor.GREEN + "Vous gagnez " + ChatColor.DARK_GREEN + "une " + ChatColor.GREEN + "vie !");
		heroBattlePlayer.gainLife();
		p.getScoreboardManager().refresh();
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
