package net.lnfinity.HeroBattle.Listeners;

import java.util.Map;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Game.DeathType;
import net.lnfinity.HeroBattle.Game.GamePlayer;
import net.lnfinity.HeroBattle.Powerups.Powerup;
import net.lnfinity.HeroBattle.Tasks.EarthquakeTask;
import net.lnfinity.HeroBattle.Tools.PlayerTool;
import net.lnfinity.HeroBattle.Utils.Utils;
import net.samagames.gameapi.json.Status;

import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

public class GameListener implements Listener {

	private HeroBattle plugin;

	public GameListener(HeroBattle p) {
		plugin = p;
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e) {
		if (plugin.getGame().getStatus() != Status.InGame) {
			e.setCancelled(true);
			return;
		}

		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			GamePlayer gp = plugin.getGamePlayer(p);

			if (e.getCause() == DamageCause.FALL) {
				e.setCancelled(true);
				gp.playTask(new EarthquakeTask(plugin, p));

				// The double-jump is reset
				gp.setDoubleJump(2);

				// The player is on the ground, so the previous hitter is no
				// longer
				// the one who will punch it out of the map.
				// ...only if the player is still inside the map of course.
				if (p.getLocation().getY() > plugin.getGame().getBottomHeight()) {
					gp.setLastDamager(null);
				}
			} else {
				e.setDamage(0);
			}
			if (e.getCause() == DamageCause.LIGHTNING) {
				gp.setPercentage(gp.getPercentage() + 20 + (int) (Math.random() * ((50 - 20) + 20)));
			}
			p.setExp(0);
			p.setTotalExperience(0);
			p.setLevel(gp.getPercentage());

			plugin.getScoreboardManager().update(p);

			plugin.getGame().updatePlayerArmor(p);
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player
				&& plugin.getGame().getStatus() == Status.InGame) {
			// Devrait *enfin* fonctionner !
			final float reducer = 25.0F;
			final Player player = (Player) e.getEntity();
			final GamePlayer gamePlayer = plugin.getGamePlayer(player);
			final Player damager = (Player) e.getDamager();
			final GamePlayer gameDamager = plugin.getGamePlayer(damager);
			Vector v = player.getVelocity().add(
					player.getLocation().toVector().subtract(damager.getLocation().toVector()).normalize()
							.multiply(gamePlayer.getPercentage() / reducer));
			v.setY(1);
			e.getEntity().setVelocity(v);

			int min = gameDamager.getPlayerClass().getMinDamages();
			int max = gameDamager.getPlayerClass().getMaxDamages();
			int damages;
			if (gameDamager.hasDoubleDamages()) {
				damages = gamePlayer.getPercentage() + 2 * (min + (int) (Math.random() * ((max - min) + min)));
			} else {
				damages = gamePlayer.getPercentage() + min + (int) (Math.random() * ((max - min) + min));
			}
			if (damages >= gamePlayer.getPlayerClass().getMaxResistance()) {
				damages = gamePlayer.getPlayerClass().getMaxResistance();
				
				gamePlayer.setPercentage(damages);
				gamePlayer.setLastDamager(damager.getUniqueId());

				player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_LARGE, 10);
				plugin.getGame().onPlayerDeath(player.getUniqueId(), DeathType.KO);
			} else {
				gamePlayer.setPercentage(damages);
				gamePlayer.setLastDamager(damager.getUniqueId());
			}
			player.setLevel(damages);
			plugin.getScoreboardManager().update(player);
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

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		// Schould never happen ?
		e.setDeathMessage(null);
		e.getDrops().clear();
		e.setDroppedExp(0);
		plugin.getGame().onPlayerDeath(e.getEntity().getUniqueId(), DeathType.QUIT);
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		if (e.getPlayer().getGameMode() == GameMode.ADVENTURE) {
			e.setCancelled(true);
			for (Map.Entry<Location, Powerup> entry : plugin.getPowerupManager().getExistingPowerups().entrySet()) {

				if (Utils.roundLocation(e.getItem().getLocation()).equals(entry.getKey())) {
					plugin.getPowerupManager().getItem(entry.getKey())
							.onPickup(e.getPlayer(), e.getItem().getItemStack());
					e.getItem().remove();
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDoubleJump(PlayerToggleFlightEvent e) {
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);

			final GamePlayer gPlayer = plugin.getGamePlayer(e.getPlayer());
			if (gPlayer != null && gPlayer.isPlaying() && plugin.getGame().getStatus() != Status.Starting
					&& plugin.getGame().getStatus() != Status.Available) {

				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					@Override
					public void run() {
						gPlayer.doubleJump();
					}
				}, 3l);
			}
		}
	}
}
