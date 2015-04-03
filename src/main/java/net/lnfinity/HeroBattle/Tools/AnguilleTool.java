package net.lnfinity.HeroBattle.Tools;

import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Utils.ItemCooldown;
import net.md_5.bungee.api.ChatColor;
import net.samagames.utils.GlowEffect;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AnguilleTool extends PlayerTool {

	private final int COOLDOWN;
	private final int DURATION;

	public AnguilleTool(HeroBattle plugin) {
		super(plugin);

		COOLDOWN = 60;
		DURATION = 6;
	}

	@Override
	public String getToolID() {
		return "tool.anguille";
	}

	@Override
	public String getName() {
		return ChatColor.DARK_RED + "" + ChatColor.BOLD + "L'anguille";
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList(ChatColor.GRAY + "La vengeance de l'anguille...", "", ChatColor.DARK_GRAY + ""
				+ ChatColor.ITALIC + "Clic droit pour activer l'effet.", ChatColor.DARK_GRAY + "" + ChatColor.ITALIC
				+ "Ne peut être utilisé que toutes les " + COOLDOWN + " secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.RAW_FISH);
		GlowEffect.addGlow(item);
		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (tool.containsEnchantment(GlowEffect.getGlow())) {
			new ItemCooldown(p, player, this, COOLDOWN);
			player.getWorld().playSound(player.getLocation(), Sound.CAT_HISS, 1, 1);
			for (Entity entity : player.getNearbyEntities(5, 5, 5)) {
				if (entity instanceof Player) {
					entity.setFireTicks(20 * DURATION);
				}
			}
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {

	}

}
