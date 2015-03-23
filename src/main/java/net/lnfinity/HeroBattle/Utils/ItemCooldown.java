package net.lnfinity.HeroBattle.Utils;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Tools.PlayerTool;
import net.samagames.utils.GlowEffect;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class ItemCooldown {

	private HeroBattle p;
	private int seconds;
	private int task;
	private int slotId;
	private OfflinePlayer player;

	/**
	 * Launches a cooldown on the specified tool for the specified player.
	 *
	 * The countdown ends when the ItemStack's amount reaches 1.
	 *
	 * @param plugin The HB plugin.
	 * @param thePlayer The player.
	 * @param tool The tool.
	 * @param time The cooldown time, in seconds.
	 */
	public ItemCooldown(HeroBattle plugin, OfflinePlayer thePlayer, PlayerTool tool, int time) {
		p = plugin;
		seconds = time;

		this.player = thePlayer;

		if(player.isOnline()) {
			Player onlinePlayer = ((Player) player);
			slotId = tool.getInventoryItemSlot(onlinePlayer);

			onlinePlayer.getInventory().getItem(slotId).removeEnchantment(GlowEffect.getGlow());
			onlinePlayer.getInventory().getItem(slotId).setAmount(seconds);

			onlinePlayer.updateInventory();
		}

		task = p.getServer().getScheduler().runTaskTimer(p, new Runnable() {
			public void run() {
				seconds--;

				if (player == null || !player.isOnline()) {
					return;
				}

				Player onlinePlayer = ((Player) player);

				if (seconds == 0) {
					onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.NOTE_PIANO, (float) 1, (float) 1.5);
					p.getServer().getScheduler().cancelTask(task);

					if (onlinePlayer.getInventory().getItem(slotId) != null
							&& onlinePlayer.getInventory().getItem(slotId).getType() != Material.AIR) {
						GlowEffect.addGlow(onlinePlayer.getInventory().getItem(slotId));
					}

				} else {
					// When the game is finished, the inventory is cleaned.
					if (onlinePlayer.getInventory().getItem(slotId) != null) {
						onlinePlayer.getInventory().getItem(slotId).setAmount(seconds);
						onlinePlayer.updateInventory();
					}
				}
			}
		}, 20L, 20L).getTaskId();
	}
}
