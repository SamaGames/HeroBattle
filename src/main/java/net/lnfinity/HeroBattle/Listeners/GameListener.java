package net.lnfinity.HeroBattle.Listeners;

import java.util.Map;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Game.DeathType;
import net.lnfinity.HeroBattle.Game.GamePlayer;
import net.lnfinity.HeroBattle.Powerups.Powerup;
import net.lnfinity.HeroBattle.Tasks.EarthquakeTask;
import net.lnfinity.HeroBattle.Tools.PlayerTool;
import net.lnfinity.HeroBattle.Tools.Weapon;
import net.lnfinity.HeroBattle.Utils.Utils;
import net.samagames.gameapi.json.Status;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
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
				gp.setPercentage(gp.getPercentage() + 25 + (int) (Math.random() * ((50 - 25) + 25)));
			} else if (e.getCause() == DamageCause.FIRE_TICK) {
				gp.setPercentage(gp.getPercentage() + 2);
			} else if (e.getCause() == DamageCause.POISON) {
				gp.setPercentage(gp.getPercentage() + 3);
			} else if (e.getCause() == DamageCause.WITHER) {
				gp.setPercentage(gp.getPercentage() + 4);
			}
			p.setLevel(0);
			p.setTotalExperience(0);
			p.setLevel(gp.getPercentage());

			plugin.getScoreboardManager().update(p);

			plugin.getGame().updatePlayerArmor(p);
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		// A condenser
		if (e.getEntity() instanceof Player && plugin.getGame().getStatus() == Status.InGame) {
			final Player player = (Player) e.getEntity();
			final GamePlayer gamePlayer = plugin.getGamePlayer(player);
			if (e.getDamager() instanceof Player) {
				// Devrait *enfin* fonctionner !
				final float reducer = 20.0F;
				final Player damager = (Player) e.getDamager();
				final GamePlayer gameDamager = plugin.getGamePlayer(damager);
				
				if (damager.getItemInHand() != null && damager.getItemInHand().getType() != Material.AIR && damager.getItemInHand().hasItemMeta()
						&& damager.getItemInHand().getItemMeta().hasDisplayName()) {
					System.out.println("ok");
					if(gameDamager.getPlayerClass().getTool(player.getInventory().getHeldItemSlot()) instanceof Weapon) {
							((Weapon) gameDamager.getPlayerClass().getTool(player.getInventory().getHeldItemSlot())).onPlayerHit(damager, player);
					}
						
				}
				
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
				if (damager.getInventory().getHeldItemSlot() != 0) {
					damages = gamePlayer.getPercentage() + 1;
				}
				if (damages >= gamePlayer.getPlayerClass().getMaxResistance()) {
					damages = gamePlayer.getPlayerClass().getMaxResistance();

					gamePlayer.setPercentage(damages);
					gamePlayer.setLastDamager(damager.getUniqueId());

					player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_LARGE, 10);

					// Très important ! Sinon le joueur conserve sa vélocité
					player.setVelocity(player.getVelocity().zero());

					plugin.getGame().onPlayerDeath(player.getUniqueId(), DeathType.KO);

					player.setLevel(0);
				} else {
					gamePlayer.setPercentage(damages);
					gamePlayer.setLastDamager(damager.getUniqueId());
					player.setLevel(damages);
				}
				plugin.getScoreboardManager().update(player);
				
			} else if (e.getDamager() instanceof Arrow) {
				Arrow arrow = (Arrow) e.getDamager();
				int damages;
				if (arrow.getShooter().equals(e.getEntity())) {
					e.setCancelled(true);
					return;
				}
				if (arrow.getCustomName() != null && arrow.getCustomName().equals(" ")) {
					arrow.getWorld().playEffect(arrow.getLocation(), Effect.EXPLOSION_HUGE, 1);
					arrow.getWorld().playSound(arrow.getLocation(), Sound.EXPLODE, 1L, 1L);
					damages = 20 + (int) (Math.random() * ((40 - 20) + 20));
					
					// TODO Damage this entities
					for(Entity entity : player.getNearbyEntities(3, 3, 3)) {
						if(entity instanceof Player) {
							((Player) entity).damage(0);
						}
					}
				} else {
					damages = 8 + (int) (Math.random() * ((20 - 8) + 8));
				}
				if (damages >= gamePlayer.getPlayerClass().getMaxResistance()) {
					damages = gamePlayer.getPlayerClass().getMaxResistance();

					gamePlayer.setPercentage(damages);
					gamePlayer.setLastDamager(((Player) arrow.getShooter()).getUniqueId());

					player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_LARGE, 10);
					
					// Très important ! Sinon le joueur conserve sa vélocité
					player.setVelocity(player.getVelocity().zero());

					plugin.getGame().onPlayerDeath(player.getUniqueId(), DeathType.KO);

					player.setLevel(0);
				} else {
					gamePlayer.setPercentage(damages + gamePlayer.getPercentage());
					gamePlayer.setLastDamager(((Player) arrow.getShooter()).getUniqueId());
					player.setLevel(damages);
				}
				plugin.getScoreboardManager().update(player);
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		e.setCancelled(true);
		final Player p = e.getPlayer();
		final GamePlayer gamePlayer = plugin.getGamePlayer(p);

		if (e.hasItem() && e.getItem().getType() != Material.AIR && e.getItem().hasItemMeta()
				&& e.getItem().getItemMeta().hasDisplayName()) {

			ItemStack item = e.getItem();

			if (item != null && plugin.getGame().getStatus() == Status.InGame) {
				if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					gamePlayer.getPlayerClass().getTool(p.getInventory().getHeldItemSlot()).onRightClick(p, item, e);
				} else if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
					gamePlayer.getPlayerClass().getTool(p.getInventory().getHeldItemSlot()).onLeftClick(p, item, e);
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

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerToggleFly(PlayerToggleFlightEvent e) {
		if (plugin.getTutorialDisplayer().isWatchingTutorial(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}
	}
}
