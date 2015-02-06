package net.lnfinity.HeroBattle.Utils;

import net.lnfinity.HeroBattle.HeroBattle;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

// FIXME Use a reference to the tool class instead of the slot ID, with a cache tool<->slot.
public class ItemCouldown {

	private HeroBattle p;
	private int seconds;
	private int task;
	private int slotId;
	private UUID playerId;
	private OfflinePlayer player;

	public ItemCouldown(HeroBattle plugin, UUID id, int slot, int time) {
		p = plugin;

		playerId = id;
		seconds = time;
		slotId = slot;

		player = p.getServer().getOfflinePlayer(playerId);

		if(player.isOnline()) {
			Player onlinePlayer = ((Player) player);

			if (onlinePlayer.getInventory().getItem(slotId) != null
					&& onlinePlayer.getInventory().getItem(slotId).getType() != Material.AIR) {

				onlinePlayer.getInventory().getItem(slotId).removeEnchantment(GlowEffect.getGlow());

			}

			onlinePlayer.getInventory().getItem(slotId).setAmount(seconds);
			onlinePlayer.updateInventory();
		}

		task = p.getServer().getScheduler().runTaskTimer(p, new Runnable() {
			public void run() {
				seconds--;

				if(player == null || !player.isOnline()) {
					return;
				}

				Player onlinePlayer = ((Player) player);

				if (seconds == 0) {
					p.getServer().getScheduler().cancelTask(task);
					if (onlinePlayer.getInventory().getItem(slotId) != null
							&& onlinePlayer.getInventory().getItem(slotId).getType() != Material.AIR) {
						GlowEffect.addGlow(onlinePlayer.getInventory().getItem(slotId));
					}
				} else {
					onlinePlayer.getInventory().getItem(slotId).setAmount(seconds);
					onlinePlayer.updateInventory();
				}
			}
		}, 20L, 20L).getTaskId();
	}
}
