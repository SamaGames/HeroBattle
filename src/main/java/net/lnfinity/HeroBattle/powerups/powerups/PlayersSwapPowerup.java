package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.powerups.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

import java.util.*;


public class PlayersSwapPowerup implements NegativePowerup {

	private HeroBattle p;
	private Random random = new Random();

	public PlayersSwapPowerup(HeroBattle plugin) {
		p = plugin;
	}

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {
		List<HeroBattlePlayer> otherPlayers = new ArrayList<>();
		for (HeroBattlePlayer gPlayer : p.getGamePlayers().values())
		{
			if (!gPlayer.isSpectator() && !gPlayer.getPlayerUniqueID().equals(player.getUniqueId()))
			{
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
