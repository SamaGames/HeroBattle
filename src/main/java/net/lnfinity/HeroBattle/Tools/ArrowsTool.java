package net.lnfinity.HeroBattle.Tools;

import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Utils.ItemCooldown;
import net.lnfinity.HeroBattle.Utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.samagames.utils.GlowEffect;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ArrowsTool extends PlayerTool {

	private final int COOLDOWN; // seconds
	private final int ARROWS_TO_FIRE;
	private int taskId;

	public ArrowsTool(HeroBattle plugin, int cooldown, int count) {
		super(plugin);
		COOLDOWN = cooldown;
		ARROWS_TO_FIRE = count;
	}

	@Override
	public String getToolID() {
		return "tool.arrows";
	}

	@Override
	public String getName() {
		return ChatColor.GRAY + "" + ChatColor.BOLD + "Arc";
	}

	@Override
	public List<String> getDescription() {
		return Utils.getToolDescription(ChatColor.GRAY + "Clic gauche pour lancer une flèche explosive causant " + ChatColor.RED + "20 " + ChatColor.GRAY + "à " + ChatColor.RED + "40 " + ChatColor.GRAY + "dégâts au joueur touché. Clic droit pour lancer une pluie de " + ChatColor.GOLD + ARROWS_TO_FIRE + " " + ChatColor.GRAY + "flèches occasionant chacune " + ChatColor.RED + "8 " + ChatColor.GRAY + "à " + ChatColor.RED + "20 " + ChatColor.GRAY + "dégâts. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.BOW);
		GlowEffect.addGlow(item);
		return item;
	}

	@Override
	public void onRightClick(final Player player, ItemStack tool, PlayerInteractEvent event) {
		if (tool.containsEnchantment(GlowEffect.getGlow())) {
			taskId = p.getServer().getScheduler().runTaskTimer(p, new Runnable() {
				int loop = 1;

				@Override
				public void run() {
					player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 1L, 1L);

					double pitch = ((player.getLocation().getPitch() + 90) * Math.PI) / 180;
					double yaw = ((player.getLocation().getYaw() + 90) * Math.PI) / 180;
					double x = Math.sin(pitch) * Math.cos(yaw);
					double y = Math.sin(pitch) * Math.sin(yaw);
					double z = Math.cos(pitch);

					Vector vector = new Vector(x, z, y);
					vector.multiply(2);

					Location loc = player.getLocation().clone();
					loc.setY(loc.getY() + 1);

					Arrow arrow = player.getWorld().spawn(loc, Arrow.class);
					arrow.setShooter(player);
					arrow.setVelocity(vector);
					if (loop >= ARROWS_TO_FIRE) {
						p.getServer().getScheduler().cancelTask(taskId);
					}
					loop++;
				}
			}, 0L, 5L).getTaskId();
			new ItemCooldown(p, player, this, COOLDOWN);
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}

	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (tool.containsEnchantment(GlowEffect.getGlow())) {
			new ItemCooldown(p, player, this, COOLDOWN);
			
			player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 1L, 1L);

			double pitch = ((player.getLocation().getPitch() + 90) * Math.PI) / 180;
			double yaw = ((player.getLocation().getYaw() + 90) * Math.PI) / 180;
			double x = Math.sin(pitch) * Math.cos(yaw);
			double y = Math.sin(pitch) * Math.sin(yaw);
			double z = Math.cos(pitch);

			Vector vector = new Vector(x, z, y);
			vector.multiply(2);

			Location loc = player.getLocation().clone();
			loc.setY(loc.getY() + 1);

			Arrow arrow = player.getWorld().spawn(loc, Arrow.class);
			arrow.setShooter(player);
			arrow.setVelocity(vector);
			arrow.setCustomName(" ");
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}
}
