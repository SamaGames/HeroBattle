package net.lnfinity.HeroBattle.tools.displayers;

import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import net.samagames.utils.GlowEffect;

public class RemoveFireTool extends PlayerTool {

	private final int COOLDOWN;
	private Random random = new Random();
	
	public RemoveFireTool(HeroBattle plugin, int cooldown) {
		super(plugin);
		this.COOLDOWN = cooldown;
	}

	@Override
	public String getToolID() {
		return "tool.removefire";
	}

	@Override
	public String getName() {
		return ChatColor.AQUA + "" + ChatColor.BOLD + "Eau";
	}

	@Override
	public List<String> getDescription() {
		return  Utils.getToolDescription(ChatColor.GRAY + "Vous enlève tous les effets de feux. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.WATER_BUCKET);
		GlowEffect.addGlow(item);
		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (ToolsUtils.isToolAvailable(tool)) {
			new ItemCooldown(p, player, this, COOLDOWN);
			if(player.getFireTicks() > 0) {
				player.setFireTicks(0);
				player.playSound(player.getLocation(), Sound.DIG_SNOW, 1, 2);
				player.sendMessage(ChatColor.GREEN + "Vous n'êtes plus en feu !");
				for(int x = 0; x < 25; x++) {
					player.getWorld().playEffect(player.getLocation().add(random.nextDouble() - 0.5, 0, random.nextDouble() - 0.5), Effect.WATERDRIP, 0);
				}
			} else {
				player.sendMessage(ChatColor.RED + "Vous n'êtes pas en feu !");
			}
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {
	}

}
