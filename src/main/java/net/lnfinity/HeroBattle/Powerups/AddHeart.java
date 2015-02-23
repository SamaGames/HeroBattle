package net.lnfinity.HeroBattle.Powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Game.GamePlayer;
import net.lnfinity.HeroBattle.Utils.Utils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class AddHeart extends Powerup {

	public AddHeart(HeroBattle plugin, Location loc) {
		super(plugin, loc);
	}

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		player.sendMessage(ChatColor.GREEN + "Quelle chance, vous gagnez " + ChatColor.DARK_GREEN + "1" + ChatColor.GREEN + " vie !");
		GamePlayer HBplayer = p.getGamePlayer(player);
		if(HBplayer.getLives() == HBplayer.getPlayerClass().getLives()) {
			player.sendMessage(ChatColor.GREEN + "Vous êtes déjà en pleine forme !");
		} else {
			HBplayer.setLives(HBplayer.getLives() + 1);
			player.setHealth(player.getHealth() + 2);
		}
		p.getPowerupManager().removeSpawnedPowerup(Utils.roundLocation(this.location));
		Location loc2 = location.clone();
		loc2.setY(loc2.getY() + 2);
		location.getWorld().playEffect(Utils.blockLocation(location), Effect.EXPLOSION_HUGE, 10, 10);
	}

	@Override
	public ItemStack getItem() {
		return new Potion(PotionType.INSTANT_HEAL).toItemStack(1);
	}

}
