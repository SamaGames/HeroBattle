package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.DeathType;
import net.lnfinity.HeroBattle.game.Game;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.tasks.displayers.EarthquakeTask;
import net.md_5.bungee.api.ChatColor;
import net.samagames.gameapi.json.Status;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SystemListener implements Listener {

	private HeroBattle plugin;

	// Longest variable name ever
	private Map<UUID,Integer> checksIfTheUserIsReallyOnTheGroundAndSoWeCanResetTheLastDamager = new ConcurrentHashMap<>();
	private final int CHECKS_NEEDED_TO_RESET_THE_LAST_DAMAGER = 10;


	public SystemListener(HeroBattle p) {
		plugin = p;
	}

	@EventHandler
	public boolean onFoodLevelChange(FoodLevelChangeEvent event) {
		event.setFoodLevel(100);
		return true;
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		if (e.getPlayer().getGameMode() == GameMode.ADVENTURE)
			e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (e.getTo().getBlockY() <= plugin.getGame().getBottomHeight()
				&& e.getPlayer().getGameMode() == GameMode.ADVENTURE && plugin.getGamePlayer(e.getPlayer()).isPlaying()) {

			if (plugin.getGame().getStatus() == Status.InGame) {
				plugin.getGame().onPlayerDeath(e.getPlayer().getUniqueId(), DeathType.FALL);
			} else {
				plugin.getGame().teleportHub(e.getPlayer().getUniqueId());
			}
		} else if (e.getTo().getBlockY() <= plugin.getGame().getBottomHeight()
				&& e.getPlayer().getGameMode() == GameMode.ADVENTURE) {

			plugin.getGame().teleportHub(e.getPlayer().getUniqueId());
		}

		if(((Entity) e.getPlayer()).isOnGround()) {
			GamePlayer gamePlayer = plugin.getGamePlayer(e.getPlayer());
			if(gamePlayer != null && plugin.getGame().getStatus() == Status.InGame) {
				gamePlayer.setJumps(gamePlayer.getMaxJumps());
				gamePlayer.setJumpLocked(false);

				e.getPlayer().setAllowFlight(true);

				gamePlayer.playTask(new EarthquakeTask(plugin, e.getPlayer()));

				// Reset of the last damager
				Integer checksForThisUser = checksIfTheUserIsReallyOnTheGroundAndSoWeCanResetTheLastDamager.get(e.getPlayer().getUniqueId());
				if(checksForThisUser == null) checksForThisUser = 0;

				if(checksForThisUser >= CHECKS_NEEDED_TO_RESET_THE_LAST_DAMAGER) {
					gamePlayer.setLastDamager(null);
					checksIfTheUserIsReallyOnTheGroundAndSoWeCanResetTheLastDamager.put(e.getPlayer().getUniqueId(), 0);
				}
				else {
					checksIfTheUserIsReallyOnTheGroundAndSoWeCanResetTheLastDamager.put(e.getPlayer().getUniqueId(), checksForThisUser + 1);
				}
			}
		}
		else {
			checksIfTheUserIsReallyOnTheGroundAndSoWeCanResetTheLastDamager.put(e.getPlayer().getUniqueId(), 0);
		}

		// Some times the double jump management of the fly cancels the fly for the spectators
		if(e.getPlayer().getGameMode() == GameMode.SPECTATOR && !e.getPlayer().getAllowFlight()) {
			e.getPlayer().setAllowFlight(true);
			e.getPlayer().setFlying(true);
		}


		Material blockType = e.getPlayer().getLocation().getBlock().getType();

		// In-water check
		if(blockType == Material.WATER || blockType == Material.STATIONARY_WATER) {
			if(plugin.getArenaConfig().getBoolean("map.toxicWater", false)) {
				plugin.getGame().onPlayerDeath(e.getPlayer().getUniqueId(), DeathType.WATER);
			}
		}
	}

	@EventHandler
	public void onEntityRegainHealthEvent(EntityRegainHealthEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onInventoryMoveItemEvent(InventoryMoveItemEvent e) {
		if (!(e.getSource().getHolder() instanceof Player && ((HumanEntity) e.getSource().getHolder()).getGameMode() == GameMode.CREATIVE)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		if (e.toWeatherState()) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		if (e.getEntityType() != EntityType.ARMOR_STAND && e.getEntityType() != EntityType.DROPPED_ITEM) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onAchievementAwarded(PlayerAchievementAwardedEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent ev) {
		if(ev.getPlayer().getGameMode() != GameMode.SPECTATOR) {
			ev.setCancelled(true);
			ev.getPlayer().setSneaking(false);
		}
	}

	@EventHandler
	public void onGameModeChange(PlayerGameModeChangeEvent ev) {
		if(ev.getNewGameMode() == GameMode.ADVENTURE) {
			ev.getPlayer().setFlySpeed(0);
		}
		else {
			ev.getPlayer().setFlySpeed(0.1f);
		}
	}


	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerChat(PlayerChatEvent e) {
		GamePlayer gamePlayer = plugin.getGamePlayer(e.getPlayer().getUniqueId());
		e.setFormat(ChatColor.DARK_GREEN + "" + gamePlayer.getElo() + ChatColor.GREEN + " ▏ " + ChatColor.RESET + e.getFormat());
	}
}
