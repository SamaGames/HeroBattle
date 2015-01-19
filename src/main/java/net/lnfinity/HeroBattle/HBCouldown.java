package net.lnfinity.HeroBattle;

import java.util.UUID;

import org.bukkit.Material;

public class HBCouldown {

	private HeroBattle p;
	private int seconds;
	private int task;
	private int slotId;
	private UUID playerId;
	
	public HBCouldown(HeroBattle plugin, UUID id, int slot, int time) {
		p = plugin;
		
		playerId = id;
		seconds = time;
		slotId = slot;
		
		if(p.getServer().getPlayer(playerId).getInventory().getItem(slotId) != null && p.getServer().getPlayer(playerId).getInventory().getItem(slotId).getType() != Material.AIR) {
			p.getServer().getPlayer(playerId).getInventory().getItem(slotId).removeEnchantment(GlowEffect.getGlow());
		}

		p.getServer().getPlayer(playerId).getInventory().getItem(slotId).setAmount(seconds);
		p.getServer().getPlayer(playerId).updateInventory();
		
		task = p.getServer().getScheduler().runTaskTimer(p, new Runnable() {
			public void run() {
				seconds--;
				if(seconds == 0) {
					p.getServer().getScheduler().cancelTask(task);
					if(p.getServer().getPlayer(playerId).getInventory().getItem(slotId) != null && p.getServer().getPlayer(playerId).getInventory().getItem(slotId).getType() != Material.AIR) {
						GlowEffect.addGlow(p.getServer().getPlayer(playerId).getInventory().getItem(slotId));
					}
				} else {
				p.getServer().getPlayer(playerId).getInventory().getItem(slotId).setAmount(seconds);
				p.getServer().getPlayer(playerId).updateInventory();
				}
			}
		}, 20L, 20L).getTaskId();
	}
}