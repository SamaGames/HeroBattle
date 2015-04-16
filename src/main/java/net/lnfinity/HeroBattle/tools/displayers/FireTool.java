package net.lnfinity.HeroBattle.tools.displayers;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import net.samagames.utils.GlowEffect;

public class FireTool extends PlayerTool {
	
	private final int COOLDOWN;
	private final int DURATION;
	private int taskId;
	
	public FireTool(HeroBattle plugin, int cooldown, int duration) {
		super(plugin);
		
		COOLDOWN = cooldown;
		DURATION = duration;
	}

	@Override
	public String getToolID() {
		return "tool.fire";
	}

	@Override
	public String getName() {
		return ChatColor.DARK_RED + "" + ChatColor.BOLD + "Feu follet";
	}

	@Override
	public List<String> getDescription() {
		return Utils.getToolDescription(ChatColor.GRAY + "Vous protège d'une couche de magma qui absorbe tous les dégâts et double votre puissance d'attaque pendant " + ChatColor.GOLD + DURATION + " " + ChatColor.GRAY + "secondes. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.FLINT_AND_STEEL);
		GlowEffect.addGlow(item);
		return item;
	}

	@Override
	public void onRightClick(final Player player, ItemStack tool, PlayerInteractEvent event) {
		if (ToolsUtils.isToolAvailable(tool)) {
			new ItemCooldown(p, player, this, COOLDOWN);
			player.playSound(player.getLocation(), Sound.BLAZE_BREATH, 1.5F, 0.75F);
			final Location location = player.getLocation();
			taskId = p.getServer().getScheduler().runTaskTimer(p, new Runnable() {
				private double i = 0;
				@Override
				public void run() {
					player.getWorld().playEffect(new Location(location.getWorld(), location.getX() + Math.sin(i * Math.PI), location.getY() + i, location.getZ() + Math.cos(i * Math.PI)), Effect.LAVADRIP, 0);
					player.getWorld().playEffect(new Location(location.getWorld(), location.getX() + Math.sin(i * Math.PI + Math.PI), location.getY() + i, location.getZ() + Math.cos(i * Math.PI + Math.PI)), Effect.LAVADRIP, 0);
					i+=0.05;
					if(i >= 2.5) {
						p.getServer().getScheduler().cancelTask(taskId);
					}
				}
			}, 0L, 1L).getTaskId();
			
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, DURATION * 20, 0));
			final GamePlayer gamePlayer = p.getGamePlayer(player);
			gamePlayer.setDoubleDamages(true);
			gamePlayer.setInvulnerable(true);
			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					gamePlayer.setDoubleDamages(false);
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
