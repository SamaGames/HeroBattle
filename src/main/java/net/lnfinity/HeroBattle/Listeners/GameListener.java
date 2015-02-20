package net.lnfinity.HeroBattle.Listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Tasks.EarthquakeTask;
import net.lnfinity.HeroBattle.Tools.PlayerTool;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
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
				plugin.getGamePlayer(p).playTask(new EarthquakeTask(plugin, p));

				plugin.getGamePlayer(p).setDoubleJump(2);
			} else {
				e.setDamage(0);
			}
			if(e.getCause() == DamageCause.LIGHTNING) {
				plugin.getGamePlayer(p).setPercentage(
						plugin.getGamePlayer(p).getPercentage() + 20
						+ (int) (Math.random() * ((50 - 20) + 20)));
			}
			p.setExp(0);
			p.setTotalExperience(0);
			p.setLevel(plugin.getGamePlayer(p).getPercentage());

			plugin.getScoreboardManager().update(p);
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player && !plugin.getGame().isWaiting()) {
			final Player player = (Player) e.getEntity();
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
			plugin.getScoreboardManager().update(player);

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

			plugin.getGamePlayer(player).setLastDamager(e.getDamager().getUniqueId());
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		final Player p = e.getPlayer();

		if (e.hasItem() && e.getItem().getType() != Material.AIR && e.getItem().hasItemMeta()
				&& e.getItem().getItemMeta().hasDisplayName()) {

			ItemStack item = e.getItem();
			String toolName = item.getItemMeta().getDisplayName();

			PlayerTool tool = plugin.getToolsManager().getToolByName(toolName);

			if (tool != null) {
				if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					tool.onRightClick(p, item, e);
				} else if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
					tool.onLeftClick(p, item, e);
				}
			}
		}
	}
}
