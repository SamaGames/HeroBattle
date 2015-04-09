package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;


public class PowerupsListener implements Listener {

	private HeroBattle p;

	public PowerupsListener(HeroBattle plugin) {
		p = plugin;
	}

	@EventHandler
	public void onPowerupPickup(PlayerPickupItemEvent ev) {

		ev.setCancelled(true);
		p.getPowerupManager().onPowerupPickup(ev.getItem(), ev.getPlayer());

	}

}
