package net.lnfinity.HeroBattle.Tools;

import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Utils.GlowEffect;
import net.lnfinity.HeroBattle.Utils.ItemCouldown;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InkTool extends PlayerTool {

	private final int COOLDOWN = 60; // seconds
	private final int EFFECT_DURATION = 10; // seconds

	public InkTool(HeroBattle plugin) {
		super(plugin);
	}

	@Override
	public String getToolID() {
		return "tool.ink";
	}

	@Override
	public String getName() {
		return ChatColor.GRAY + "Jet d'encre";
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList(ChatColor.GRAY + "Clic droit pour activer l'effet", ChatColor.DARK_GRAY
				+ "Ne peut être utilisé que toutes les" + COOLDOWN + "secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.INK_SACK, 1);
		GlowEffect.addGlow(item);
		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (tool.containsEnchantment(GlowEffect.getGlow())) {
			player.playSound(player.getLocation(), Sound.SPLASH, 1, 1);
			new ItemCouldown(p, player.getUniqueId(), player.getInventory().getHeldItemSlot(), COOLDOWN);
			for (Entity e : player.getNearbyEntities(10, 10, 10)) {
				if (e instanceof Player) {
					Player pl = (Player) e;
					pl.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, EFFECT_DURATION * 20, 0));
					pl.playSound(pl.getLocation(), Sound.SPLASH, 1, 1);
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
