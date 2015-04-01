package net.lnfinity.HeroBattle.Tools;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Utils.ItemCooldown;
import net.md_5.bungee.api.ChatColor;
import net.samagames.utils.GlowEffect;

import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NauseaTool extends PlayerTool {

	private final int COOLDOWN; // seconds
	private final int EFFECT_DURATION; // seconds
	private final double PROBABILITY_SENDER_HIT;

	private Random random = null;

	public NauseaTool(HeroBattle plugin, int cooldown, int duration, double probability) {
		super(plugin);
		COOLDOWN = cooldown;
		EFFECT_DURATION = duration;
		PROBABILITY_SENDER_HIT = probability;

		random = new Random();
	}

	@Override
	public String getToolID() {
		return "tool.nausea";
	}

	@Override
	public String getName() {
		return ChatColor.GREEN + "" + ChatColor.BOLD + "Crachat de vomi";
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList(ChatColor.GRAY + "Rend les joueurs proches malades.",
				ChatColor.GRAY + "Attention, votre crachat peut vous toucher...",
				"",
				ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Clic droit pour activer l'effet.",
				ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Ne peut être utilisé que toutes les " + COOLDOWN + " secondes.");
	}

	@Override
	public ItemStack getItem() {
		Dye dye = new Dye();
		dye.setColor(DyeColor.GREEN);
		ItemStack item = dye.toItemStack();
		item.setAmount(1);
		GlowEffect.addGlow(item);
		return item;
	}

	@Override
	public void onRightClick(final Player player, ItemStack tool, PlayerInteractEvent event) {
		if (tool.containsEnchantment(GlowEffect.getGlow())) {
			player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, (float) 1, (float) 2);
			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					player.playSound(player.getLocation(), Sound.SPLASH, (float) 1, (float) 0.5);
				}
			}, 10L);
			new ItemCooldown(p, player, this, COOLDOWN);

			for (Entity e : player.getNearbyEntities(10, 10, 10)) {
				if (e instanceof Player) {
					Player pl = (Player) e;
					pl.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, EFFECT_DURATION * 20, 0));
					player.playSound(player.getLocation(), Sound.SPLASH, (float) 1, (float) 0.5);
				}
			}

			if(random.nextDouble() < PROBABILITY_SENDER_HIT) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, ((EFFECT_DURATION * 20) / 2) + random.nextInt(EFFECT_DURATION / 2), 0));
			}
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}

	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {

	}

}

