package net.lnfinity.HeroBattle.tools.displayers;

import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class FruitTool extends PlayerTool {
	
	private final int COOLDOWN, DURATION;

	public FruitTool(HeroBattle plugin, int cooldown, int duration) {
		super(plugin);
		
		COOLDOWN = cooldown;
		DURATION = duration;
	}

	@Override
	public String getToolID() {
		return "tools.fruit";
	}

	@Override
	public String getName() {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "Fruit du Savoir";
	}

	@Override
	public List<String> getDescription() {
		return Utils.getToolDescription(ChatColor.GRAY + "Le fruit du savoir est une denrée très rare uniquement possédée par les gardiens. Une fois mangé, le fruit doubles les dégâts au corps à corps pendant " + ChatColor.GOLD + DURATION + " " + ChatColor.GRAY + "secondes. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.GOLDEN_CARROT);
		ToolsUtils.resetTool(item);
		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (ToolsUtils.isToolAvailable(tool)) {
			new ItemCooldown(p, player, this, COOLDOWN);
			player.playSound(player.getLocation(), Sound.BURP, 1, 1);
			
			GamePlayer gamePlayer = p.getGamePlayer(player);
			gamePlayer.addRemainingDoubleDamages(DURATION);
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {
	}

}
