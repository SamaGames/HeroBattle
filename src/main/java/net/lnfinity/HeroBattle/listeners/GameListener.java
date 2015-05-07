package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.DeathType;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.tasks.displayers.EarthquakeTask;
import net.lnfinity.HeroBattle.tools.Weapon;
import net.lnfinity.HeroBattle.utils.Utils;
import net.samagames.gameapi.json.Status;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;

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

		if(e.getCause() == DamageCause.FALL) {
			e.setCancelled(true);
			return;
		}

		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			GamePlayer gp = plugin.getGamePlayer(p);

			e.setDamage(0);

			// ### ThunderTool ###
			if (e.getCause() == DamageCause.LIGHTNING) {
				UUID nearest = null;
				Double distanceS = Double.MAX_VALUE;
				Location locRef = p.getLocation();
				for(Map.Entry<UUID,Location> entry : plugin.getGame().getLastLightningBolts().entrySet()) {
					double distanceLoc = entry.getValue().distanceSquared(locRef);
					if(distanceLoc < distanceS) {
						distanceS = distanceLoc;
						nearest = entry.getKey();
					}
				}

				gp.setPercentage(gp.getPercentage() + 25 + (int) (Math.random() * ((50 - 25) + 25)), nearest == null ? null : plugin.getGamePlayer(nearest));
			}
			
			// ### Tools that use fire ###
			else if (e.getCause() == DamageCause.FIRE_TICK || e.getCause() == DamageCause.FIRE) {
				gp.setPercentage(gp.getPercentage() + 2, plugin.getGamePlayer(plugin.getGame().getFiresInProgress().get(gp.getPlayerUniqueID())));
			}
			
			// ### Tools that use poison ###
			else if (e.getCause() == DamageCause.POISON) {
				gp.setPercentage(gp.getPercentage() + 3, plugin.getGamePlayer(plugin.getGame().getPoisonsInProgress().get(gp.getPlayerUniqueID())));
			}

			// ### Tools that use wither effects ###
			else if (e.getCause() == DamageCause.WITHER) {
				gp.setPercentage(gp.getPercentage() + 4, null);
			}
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		// A condenser
		if (e.getEntity() instanceof Player && plugin.getGame().getStatus() == Status.InGame) {
			final Player player = (Player) e.getEntity();
			final GamePlayer gamePlayer = plugin.getGamePlayer(player);
			if(gamePlayer == null) return;
			if(gamePlayer.isRespawning()) {
				e.setCancelled(true);
				return;
			}
			if (e.getDamager() instanceof Player) {
				// Devrait *enfin* fonctionner !
				final float reducer = 15.0F;
				final Player damager = (Player) e.getDamager();
				final GamePlayer gameDamager = plugin.getGamePlayer(damager);
				
				if (damager.getItemInHand() != null && damager.getItemInHand().getType() != Material.AIR && damager.getItemInHand().hasItemMeta()
						&& damager.getItemInHand().getItemMeta().hasDisplayName()) {

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

				gamePlayer.setPercentage(damages, gameDamager);
				gamePlayer.setLastDamager(damager.getUniqueId());
				
			// ### ArrowsTool ###
			} else if (e.getDamager() instanceof Arrow) {
				Arrow arrow = (Arrow) e.getDamager();
				int damages;
				if (arrow.getShooter().equals(e.getEntity()) || !(arrow.getShooter() instanceof Player)) {
					e.setCancelled(true);
					return;
				}

				GamePlayer damagerGPlayer = plugin.getGamePlayer(((Player) arrow.getShooter()));

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

				gamePlayer.setPercentage(damages + gamePlayer.getPercentage(), damagerGPlayer);
				gamePlayer.setLastDamager(((Player) arrow.getShooter()).getUniqueId());
			}
		}

		else if(e.getEntityType() == EntityType.ARMOR_STAND || e.getEntityType() == EntityType.DROPPED_ITEM) {
			e.setCancelled(true); // Avoid the lightning bolts from destroying the powerups.
		}
	}

	@EventHandler
	public void onentityExplode(ExplosionPrimeEvent e) {
		Entity entity = e.getEntity();
		
		// ### FireballTool ###
		if (!(entity instanceof Fireball)) return;
		e.setRadius(0);

		GamePlayer damager = plugin.getGamePlayer(plugin.getGame().getFireballsLaunched().get(entity.getUniqueId()));

		for(GamePlayer gamePlayer : plugin.getGamePlayers().values()) {
			Player player = plugin.getServer().getPlayer(gamePlayer.getPlayerUniqueID());
			if(player != null) {
				if(player.getLocation().distanceSquared(e.getEntity().getLocation()) <= 16) {
					player.damage(0);
					gamePlayer.setPercentage(gamePlayer.getPercentage() + Utils.randomNumber(16, 25), damager);
					player.setLevel(gamePlayer.getPercentage());
					plugin.getScoreboardManager().update(player);
				}
			}
		}

		for(int i = 0; i <= Utils.randomNumber(10, 15); i++) {
			e.getEntity().getWorld().playEffect(e.getEntity().getLocation().clone().add(1 - Utils.randomNumber(0, 2), 1 - Utils.randomNumber(0, 2), 1 - Utils.randomNumber(0, 2)), Effect.FLAME, 0);
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
		// Should never happen ?
		e.setDeathMessage(null);
		e.getDrops().clear();
		e.setDroppedExp(0);
		plugin.getGame().onPlayerDeath(e.getEntity().getUniqueId(), DeathType.QUIT);
	}


	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDoubleJump(PlayerToggleFlightEvent e) {
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);

			final GamePlayer gPlayer = plugin.getGamePlayer(e.getPlayer());
			if (gPlayer != null && gPlayer.isPlaying() && plugin.getGame().getStatus() != Status.Starting
					&& plugin.getGame().getStatus() != Status.Available) {

				gPlayer.doubleJump();
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
