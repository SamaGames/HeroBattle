package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.powerups.NegativePowerup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class PlayersSwapPowerup implements NegativePowerup {

	private HeroBattle p;
	private Random random = new Random();

	public PlayersSwapPowerup(HeroBattle plugin) {
		p = plugin;
	}

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		List<GamePlayer> otherPlayers = new ArrayList<>();
		for(GamePlayer gPlayer : p.getGamePlayers().values()) {
			if(gPlayer.isPlaying() && !gPlayer.getPlayerUniqueID().equals(player.getUniqueId())) {
				otherPlayers.add(gPlayer);
			}
		}

		if(otherPlayers.size() == 0) return;

		Player otherPlayer = p.getServer().getPlayer(otherPlayers.get(random.nextInt(otherPlayers.size())).getPlayerUniqueID());
		Location otherPlayerLocation = otherPlayer.getLocation();

		otherPlayer.teleport(player);
		player.teleport(otherPlayerLocation);

		otherPlayer.sendMessage(ChatColor.RED + "Woosh ! Vous avez échangé votre place avec " + ChatColor.DARK_RED + player.getName() + ChatColor.RED + " car il a pris un bonus de swap !");
		player.sendMessage(ChatColor.RED + "Woosh ! Vous avez échangé votre place avec " + ChatColor.DARK_RED + otherPlayer.getName() + ChatColor.RED + " !");

		otherPlayer.playSound(otherPlayer.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
		player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Material.COMMAND);
	}

	@Override
	public String getName() {
		return ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "◄►"
				+ ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + " INVERSION";
	}

	@Override
	public double getWeight() {
		return 15;
	}
}
