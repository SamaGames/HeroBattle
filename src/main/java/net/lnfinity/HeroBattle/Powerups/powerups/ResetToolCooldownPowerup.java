package net.lnfinity.HeroBattle.Powerups.powerups;

import net.lnfinity.HeroBattle.Game.GamePlayer;
import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Powerups.PositivePowerup;
import net.lnfinity.HeroBattle.Utils.ToolsUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * This powerup resets the cooldown of a random tool with at least 5 seconds left of the player.
 */
public class ResetToolCooldownPowerup implements PositivePowerup {

	private HeroBattle p;

	public ResetToolCooldownPowerup(HeroBattle plugin) {
		p = plugin;
	}

	@Override
	public void onPickup(Player player, ItemStack pickupItem) {

		GamePlayer gPlayer = p.getGamePlayer(player);

		// 1: we try to find a tool with a delay higher than 5 seconds.
		List<Integer> slotsToTest = new ArrayList<>();
		for(int i = 0; i < gPlayer.getPlayerClass().getTools().size(); i++) {
			slotsToTest.add(i);
		}

		ItemStack toolToReset = null;
		Random rand = new Random();

		do {
			if(slotsToTest.size() == 0) break;

			int stackSlot = slotsToTest.get(rand.nextInt(slotsToTest.size()));
			ItemStack stack = player.getInventory().getItem(stackSlot);
			slotsToTest.remove(stackSlot);

			if(!ToolsUtils.isToolAvailable(stack) && stack.getAmount() >= 6) {
				toolToReset = stack;
			}

		} while(toolToReset == null);


		if(toolToReset == null) {
			player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "Vous n'avez aucun outil qui vaille le coup d'être réinitialisé...");
			return;
		}


		ToolsUtils.resetTool(toolToReset);
		player.sendMessage(ChatColor.GREEN + "L'outil " + toolToReset.getItemMeta().getDisplayName() + ChatColor.GREEN + " est à nouveau utilisable ! " + ChatColor.DARK_GREEN + "*pouf*");

		player.updateInventory();
	}

	@Override
	public ItemStack getItem() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public double getWeight() {
		return 0;
	}
}
