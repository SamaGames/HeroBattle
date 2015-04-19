package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.DeathType;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.tasks.displayers.EarthquakeTask;
import net.md_5.bungee.api.ChatColor;
import net.samagames.gameapi.json.Status;
import org.bukkit.GameMode;
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

public class SystemListener implements Listener {

	private HeroBattle plugin;

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
				e.getPlayer().setAllowFlight(true);

				gamePlayer.playTask(new EarthquakeTask(plugin, e.getPlayer()));
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
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerChat(PlayerChatEvent e) {
		GamePlayer gamePlayer = plugin.getGamePlayer(e.getPlayer().getUniqueId());
		e.setFormat(ChatColor.DARK_GREEN + "" + gamePlayer.getElo() + ChatColor.GREEN + " ▏ " + ChatColor.RESET + e.getFormat());
	}
}
