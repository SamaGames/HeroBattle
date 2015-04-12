package net.lnfinity.HeroBattle.tools;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import net.samagames.utils.GlowEffect;

public class InvincibleTool extends PlayerTool {

	private final int COOLDOWN; // seconds
	private final int DURATION; // seconds

	public InvincibleTool(HeroBattle plugin, int cooldown, int duration) {
		super(plugin);
		COOLDOWN = cooldown;
		DURATION = duration;
	}

	@Override
	public String getToolID() {
		return "tool.invinsible";
	}

	@Override
	public String getName() {
		return ChatColor.BLUE + "" + ChatColor.BOLD + "Givre";
	}

	@Override
	public List<String> getDescription() {
		return Utils.getToolDescription(ChatColor.GRAY + "Créé une couche de givre qui absorbe tous les dégâts pendant " + ChatColor.GOLD + DURATION + " " + ChatColor.GRAY + "secondes. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.SNOW_BALL);
		GlowEffect.addGlow(item);
		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (ToolsUtils.isToolAvailable(tool)) {
			new ItemCooldown(p, player, this, COOLDOWN);
			player.playSound(player.getLocation(), Sound.BLAZE_DEATH, 1F, 1.5F);
			for(double i = 0; i <= 2; i+=0.05) {
					player.getWorld().playEffect(new Location(player.getWorld(), player.getLocation().getX() + Math.sin(i * Math.PI), player.getLocation().getY() + 1.5, player.getLocation().getZ() + Math.cos(i * Math.PI)), Effect.WATERDRIP, 0);
					player.getWorld().playEffect(new Location(player.getWorld(), player.getLocation().getX() + Math.sin(i * Math.PI), player.getLocation().getY() + 1, player.getLocation().getZ() + Math.cos(i * Math.PI)), Effect.WATERDRIP, 0);
					player.getWorld().playEffect(new Location(player.getWorld(), player.getLocation().getX() + Math.sin(i * Math.PI), player.getLocation().getY() + 0.5, player.getLocation().getZ() + Math.cos(i * Math.PI)), Effect.WATERDRIP, 0);
			}
			
			final GamePlayer gamePlayer = p.getGamePlayer(player);
			gamePlayer.setInvulnerable(true);
			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					gamePlayer.setInvulnerable(false);
				}
			}, DURATION * 20L);
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {

	}

}
