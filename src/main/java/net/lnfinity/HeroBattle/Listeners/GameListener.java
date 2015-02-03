package net.lnfinity.HeroBattle.Listeners;

import net.lnfinity.HeroBattle.Game.GamePlayer;
import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Utils.ItemCouldown;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;

public class GameListener implements Listener {

	private HeroBattle plugin;

	public GameListener(HeroBattle p) {
		plugin = p;
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e) {
		if (plugin.getGame().isWaiting()) {
			e.setCancelled(true);
		}
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getCause() == DamageCause.FALL) {
				e.setCancelled(true);
				plugin.getGamePlayer(p).setDoubleJump(2);
			} else {
				e.setDamage(0);
			}
		}
		if (e.getCause() == DamageCause.ENTITY_ATTACK) {

		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player
				&& !plugin.getGame().isWaiting()) {
			Player player = (Player) e.getEntity();
			int min = plugin.getGamePlayer(player).getPlayerClass().getMinDamages();
			int max = plugin.getGamePlayer(player).getPlayerClass().getMaxDamages();
			if (plugin.getGamePlayer((Player) e.getDamager()).hasDoubleDamages()) {
				plugin.getGamePlayer(player).setPercentage(
						plugin.getGamePlayer(player).getPercentage() + 2
								* (min + (int) (Math.random() * ((max - min) + min))));
			} else {
				plugin.getGamePlayer(player).setPercentage(
						plugin.getGamePlayer(player).getPercentage() + min
								+ (int) (Math.random() * ((max - min) + min)));
			}

			player.setLevel(plugin.getGamePlayer(player).getPercentage());

			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard board = manager.getNewScoreboard();

			Objective objective = board.registerNewObjective("percentage", "dummy");
			objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
			objective.setDisplayName("%");

			for (Player online : Bukkit.getOnlinePlayers()) {
				Score score = objective.getScore(online);
				score.setScore(plugin.getGamePlayer(online).getPercentage());
			}

			for (Player online : Bukkit.getOnlinePlayers()) {
				online.setScoreboard(board);
			}

			double pw = 10;
			double xa = e.getDamager().getLocation().getX();
			double ya = e.getDamager().getLocation().getZ();
			double xb = e.getEntity().getLocation().getX();
			double yb = e.getEntity().getLocation().getZ();
			double m = (ya - yb) / (xa - xb);
			double p = ya - m * xa;
			double alpha = Math.atan(m * xa + p);
			double xc1 = xb + pw * Math.cos(alpha);
			double xc2 = xb - pw * Math.cos(alpha);
			double yc;
			double xc;
			if (Math.abs(xa - xc1) > Math.abs(xa - xc2)) {
				yc = m * xc1 + p;
				xc = xc1;
			} else {
				yc = m * xc2 + p;
				xc = xc2;
			}
			double a = xc - e.getEntity().getLocation().getX();
			double b = yc - e.getEntity().getLocation().getZ();
			e.getEntity().setVelocity(
					new Vector(a * plugin.getGamePlayer(player).getPercentage(), e.getEntity().getVelocity().getY(), b
							* plugin.getGamePlayer(player).getPercentage()));
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		GamePlayer hbp = plugin.getGamePlayer(p);
		if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
				&& e.getPlayer().getItemInHand().getType() != Material.AIR) {
			if (e.getItem().getType() == Material.IRON_SWORD) {
				if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
					hbp.setDoubleJump(2);
				}
				if (hbp.getDoubleJump() > 0) {
					hbp.setDoubleJump(hbp.getDoubleJump() - 1);
					p.setVelocity(p.getVelocity().setY(1.5));
				}
			} else if (e.getItem().getType() == Material.SUGAR) {
				if (e.getItem().getEnchantments().size() >= 1) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
					new ItemCouldown(plugin, p.getUniqueId(), 1, 20);
				} else {
					e.getPlayer().sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
				}
			} else if (e.getItem().getType() == Material.BLAZE_POWDER) {
				if (e.getItem().getEnchantments().size() >= 1) {
					new ItemCouldown(plugin, p.getUniqueId(), 2, 60);
					p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 9));
					plugin.getGamePlayer(p).setDoubleDamages(true);
					plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
						@Override
						public void run() {
							plugin.getGamePlayer(p).setDoubleDamages(false);
						}
					}, 200L);
				} else {
					e.getPlayer().sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
				}
			}
		}
	}
}
