package net.lnfinity.HeroBattle.Listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Game.DeathType;
import net.samagames.gameapi.json.Status;

import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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
			}
		} else if (e.getTo().getBlockY() <= plugin.getGame().getBottomHeight()
				&& e.getPlayer().getGameMode() == GameMode.ADVENTURE) {

			plugin.getGame().teleportHub(e.getPlayer().getUniqueId());
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
		if (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) {
			e.setCancelled(true);
		}
	}
}
