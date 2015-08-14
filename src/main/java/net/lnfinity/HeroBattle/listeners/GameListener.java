package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.displayers.free.BruteClass;
import net.lnfinity.HeroBattle.classes.displayers.free.MinerClass;
import net.lnfinity.HeroBattle.game.DeathType;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.tools.Weapon;
import net.lnfinity.HeroBattle.utils.ParticleEffect;
import net.lnfinity.HeroBattle.utils.TripleParameters;
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
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class GameListener implements Listener {

	private HeroBattle plugin;

	private Random random = new Random();

	private Map<UUID,BukkitTask> tpSoundsTasks = new HashMap<>();


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
			
			if(gp == null) return;

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

				TripleParameters params = plugin.getGame().getParameters(e.getEntity().getUniqueId());
				gp.setPercentage(gp.getPercentage() + 25 + (int) (Math.random() * ((50 - 25) + 25)), nearest == null ? null : plugin.getGamePlayer(nearest));
			}
			
			// ### Tools that use fire ###
			else if (e.getCause() == DamageCause.FIRE_TICK || e.getCause() == DamageCause.FIRE) {
				gp.basicDamage(2, plugin.getGamePlayer(plugin.getGame().getFiresInProgress().get(gp.getPlayerUniqueID())));
			}
			
			// ### Tools that use poison ###
			else if (e.getCause() == DamageCause.POISON) {
				gp.basicDamage(3, plugin.getGamePlayer(plugin.getGame().getPoisonsInProgress().get(gp.getPlayerUniqueID())));
			}

			// ### Tools that use wither effects ###
			else if (e.getCause() == DamageCause.WITHER) {
				gp.basicDamage(4, null);
			}
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(final EntityDamageByEntityEvent e) {
		// A condenser
		if (e.getEntity() instanceof Player && plugin.getGame().getStatus() == Status.InGame) {
			
			final Player player = (Player) e.getEntity();
			final GamePlayer gamePlayer = plugin.getGamePlayer(player);
			if(gamePlayer == null) return;
			if(gamePlayer.getRemainingRespawnInvincibility() != 0) {
				e.setCancelled(true);
				return;
			}

			if (e.getDamager() instanceof Player) {
				// v- old
				// Devrait *enfin* fonctionner !
				
				final Player damager = (Player) e.getDamager();
				final GamePlayer gameDamager = plugin.getGamePlayer(damager);
				
				// Prevents a bug
				if(gameDamager.getPlayerClass() == null) gameDamager.setPlayerClass(new BruteClass(plugin));
				

				if (damager.getItemInHand() != null && damager.getItemInHand().getType() != Material.AIR && damager.getItemInHand().hasItemMeta()
						&& damager.getItemInHand().getItemMeta().hasDisplayName()) {

					if(gameDamager.getPlayerClass().getTool(player.getInventory().getHeldItemSlot()) instanceof Weapon) {
							((Weapon) gameDamager.getPlayerClass().getTool(player.getInventory().getHeldItemSlot())).onPlayerHit(damager, player);
					}
						
				}
				
				int min = gameDamager.getPlayerClass().getMinDamages();
				int max = gameDamager.getPlayerClass().getMaxDamages();
				int damages;
				
				// Double damage for weapon only
				if (gameDamager.getRemainingDoubleDamages() != 0) {
					damages = 2 * (min + random.nextInt(max - min + 1));
				} else {
					damages = min + random.nextInt(max - min + 1);
				}
				
				// Hitting with something else than the main weapon
				if (damager.getInventory().getHeldItemSlot() != 0) {
					damages = 1;
				}

				gamePlayer.damage(damages, gameDamager, e.getDamager().getLocation());
				
				// Cool effects
				for(int i = 0; i < Math.min(gamePlayer.getPercentage() / 20, 5); i++)
					e.getEntity().getWorld().playEffect(e.getEntity().getLocation().add(0.5 - Math.random(), Math.random() + 0.5, 0.5 - Math.random()), Effect.SMALL_SMOKE, 0);
				
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					@Override
					public void run() {
						for(int i = 0; i < Math.min(gamePlayer.getPercentage() / 30, 5); i++)
							e.getEntity().getWorld().playEffect(e.getEntity().getLocation().add(0.5 - Math.random(), Math.random() + 0.5, 0.5 - Math.random()), Effect.SMOKE, 4);	
					}
				}, 1L);
				
				if(gamePlayer.getPercentage() > 150 && random.nextInt(4) == 0)
					e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ZOMBIE_WOODBREAK, 0.5F, 1);
					
				
			// ### ArrowsTool ###
			} else if (e.getDamager() instanceof Arrow) {
				Arrow arrow = (Arrow) e.getDamager();
				TripleParameters params = plugin.getGame().getParameters(arrow.getUniqueId());

				if (arrow.getShooter().equals(e.getEntity()) || !(arrow.getShooter() instanceof Player)) {
					e.setCancelled(true);
					return;
				}

				GamePlayer damagerGPlayer = plugin.getGamePlayer(((Player) arrow.getShooter()));

				if (arrow.getCustomName() != null && arrow.getCustomName().equals(" ")) {
					arrow.setFireTicks(0);
					arrow.getWorld().playEffect(arrow.getLocation(), Effect.EXPLOSION_HUGE, 1);
					arrow.getWorld().playSound(arrow.getLocation(), Sound.EXPLODE, 1L, 1L);
					
					for(GamePlayer potential : plugin.getGamePlayers().values()) {
						if(!potential.equals(damagerGPlayer) && potential.getPlayer().getLocation().distanceSquared(e.getDamager().getLocation()) < 16) {
							potential.damage(params.getMinDamages(), params.getMaxDamages(), damagerGPlayer, e.getDamager().getLocation());
						}
					}
				} else {
					gamePlayer.damage(params.getMinDamages(), params.getMaxDamages(), damagerGPlayer, e.getDamager().getLocation());
				}
				
			}
		}

		else if(e.getEntityType() == EntityType.ARMOR_STAND || e.getEntityType() == EntityType.DROPPED_ITEM) {
			e.setCancelled(true); // Avoid the lightning bolts from destroying the powerups (among other things).
		}
	}

	@EventHandler
	public void onentityExplode(ExplosionPrimeEvent e) {
		Entity entity = e.getEntity();
		
		// ### FireballTool ###
		if (entity instanceof Fireball) {
		e.setRadius(0);

		GamePlayer damager = plugin.getGamePlayer(plugin.getGame().getFireballsLaunched().get(entity.getUniqueId()));

		for(GamePlayer gamePlayer : plugin.getGamePlayers().values()) {
			Player player = plugin.getServer().getPlayer(gamePlayer.getPlayerUniqueID());
			if(player != null) {
				if(player.getLocation().distanceSquared(e.getEntity().getLocation()) <= 16) {
					
					TripleParameters params = plugin.getGame().getParameters(e.getEntity().getUniqueId());
					
					gamePlayer.damage(params.getMinDamages(), params.getMaxDamages(), damager, e.getEntity().getLocation());
					
				}
			}
		}

		for(int i = 0; i <= Utils.randomNumber(10, 15); i++) {
			e.getEntity().getWorld().playEffect(e.getEntity().getLocation().clone().add(1 - Utils.randomNumber(0, 2), 1 - Utils.randomNumber(0, 2), 1 - Utils.randomNumber(0, 2)), Effect.FLAME, 0);
		}
		
		
		// ### TNTTool ###
		} else if(entity instanceof TNTPrimed) {
			e.setRadius(0);
			
			GamePlayer damager = plugin.getGamePlayer(plugin.getGame().getFireballsLaunched().get(entity.getUniqueId()));

			for(GamePlayer gamePlayer : plugin.getGamePlayers().values()) {
				Player player = plugin.getServer().getPlayer(gamePlayer.getPlayerUniqueID());
				if(player != null && !(gamePlayer.getPlayerClass() instanceof MinerClass)) {
					if(player.getLocation().distanceSquared(e.getEntity().getLocation()) <= 16) {
						
						TripleParameters params = plugin.getGame().getParameters(entity.getUniqueId());
						gamePlayer.damage(params.getMinDamages(), params.getMaxDamages(), damager, e.getEntity().getLocation());
						
					}
				}
			}
		}
	}


	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {

		final Player p = e.getPlayer();
		final GamePlayer gamePlayer = plugin.getGamePlayer(p);

		if(gamePlayer == null || gamePlayer.getPlayerClass() == null) return;


		if(p.getGameMode() != GameMode.CREATIVE) e.setCancelled(true);


		if (e.hasItem() && e.getItem().getType() != Material.AIR && e.getItem().hasItemMeta()
				&& e.getItem().getItemMeta().hasDisplayName()) {

			ItemStack item = e.getItem();

			if (item != null && plugin.getGame().getStatus() == Status.InGame) {

				PlayerTool tool = gamePlayer.getPlayerClass().getTool(p.getInventory().getHeldItemSlot());
				if(tool == null) return;

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
		// Should never happen ?
		e.setDeathMessage(null);
		e.getDrops().clear();
		e.setDroppedExp(0);
		plugin.getGame().onPlayerDeath(e.getEntity().getUniqueId(), DeathType.QUIT);
	}


	@EventHandler
	public void onPlayerEntersATeleportationPortal(EntityPortalEnterEvent ev) {
		if(plugin.getGame().getTeleportationPortalsDestinations().size() != 0
				&& ev.getEntityType() == EntityType.PLAYER
				&& ev.getLocation().getBlock().getType() == Material.ENDER_PORTAL) {

			ev.getEntity().teleport(plugin.getGame().getTeleportationPortalsDestinations().get(random.nextInt(plugin.getGame().getTeleportationPortalsDestinations().size())));


			if(plugin.getArenaConfig().getBoolean("map.marioTeleportationSound")) {
				if(!tpSoundsTasks.containsKey(ev.getEntity().getUniqueId())) {

					final Player player = ((Player) ev.getEntity());

					tpSoundsTasks.put(ev.getEntity().getUniqueId(), plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {

						private int runs = 0;

						@Override
						public void run() {
							player.playSound(player.getLocation(), Sound.BURP, 3, 6);

							runs++;
							if(runs >= 3) {
								tpSoundsTasks.get(player.getUniqueId()).cancel();
								tpSoundsTasks.remove(player.getUniqueId());
							}
						}
					}, 1l, 6l));
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
