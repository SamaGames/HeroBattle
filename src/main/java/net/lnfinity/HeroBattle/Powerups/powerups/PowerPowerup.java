package net.lnfinity.HeroBattle.Powerups.powerups;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Game.GamePlayer;
import net.lnfinity.HeroBattle.Powerups.PositivePowerup;

public class PowerPowerup implements PositivePowerup {

	private HeroBattle p;
	
	public PowerPowerup(HeroBattle plugin) {
		p = plugin;
	}
	
	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		final GamePlayer gamePlayer = p.getGamePlayer(player);
		player.sendMessage(ChatColor.GREEN + "Vous activez " + ChatColor.DARK_GREEN + "doubles dommages " + ChatColor.GREEN + "!");
		gamePlayer.setDoubleDamages(true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 15 * 20, 0));
		p.getServer().getScheduler().runTaskLater(p, new Runnable() {
			@Override
			public void run() {
				gamePlayer.setDoubleDamages(false);
			}
		}, 15 * 20L);
		
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
		return 35;
	}

}
