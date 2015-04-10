package net.lnfinity.HeroBattle.utils;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.samagames.utils.GlowEffect;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;


public class ItemCooldown {

	private HeroBattle p;
	private int seconds;
	private final BukkitTask task;
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
				
				if(onlinePlayer.getInventory().getItem(slotId) != null && onlinePlayer.getInventory().getItem(slotId).getAmount() == 1) {
					ToolsUtils.resetTool(onlinePlayer.getInventory().getItem(slotId));
					task.cancel();
					return;
				}

				ItemStack slot = onlinePlayer.getInventory().getItem(slotId);

				if (seconds == 0 || ToolsUtils.isToolAvailable(slot)) {

					onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.NOTE_PIANO, (float) 1, (float) 1.5);
					task.cancel();

					if (slot != null && slot.getType() != Material.AIR) {
						ToolsUtils.resetTool(slot);
					}

				} else {
					// When the game is finished, the inventory is cleaned.
					if (slot != null) {
						slot.setAmount(seconds);
					}
				}

				onlinePlayer.updateInventory();
			}
		}, 20L, 20L);
	}
}
