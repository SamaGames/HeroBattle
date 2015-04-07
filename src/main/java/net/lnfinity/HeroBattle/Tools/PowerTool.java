package net.lnfinity.HeroBattle.Tools;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Utils.ItemCooldown;
import net.lnfinity.HeroBattle.Utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.samagames.utils.GlowEffect;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class PowerTool extends PlayerTool {

	private final int COOLDOWN; // seconds
	private final int EFFECT_DURATION; // seconds

	public PowerTool(HeroBattle plugin, int cooldown, int duration) {
		super(plugin);
		COOLDOWN = cooldown;
		EFFECT_DURATION = duration;
	}

	@Override
	public String getToolID() {
		return "tool.power";
	}

	@Override
	public String getName() {
		return ChatColor.DARK_RED + "" + ChatColor.BOLD + "Poudre de puissance";
	}

	@Override
	public List<String> getDescription() {
		return Utils.getToolDescription(ChatColor.GRAY + "Vous ajoute un effet de force qui permet de multiplier les dommages de corps à corps par " + ChatColor.GOLD + "2 " + ChatColor.GRAY + "pendant " + ChatColor.GOLD + EFFECT_DURATION + " " + ChatColor.GRAY + "secondes. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.BLAZE_POWDER, 1);
		GlowEffect.addGlow(item);

		return item;
	}

	@Override
	public void onRightClick(final Player player, ItemStack tool, PlayerInteractEvent event) {
		if(tool.containsEnchantment(GlowEffect.getGlow())) {
			new ItemCooldown(p, player, this, COOLDOWN);

			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, EFFECT_DURATION * 20, 9));
			p.getGamePlayer(player).setDoubleDamages(true);

			final UUID playerID = player.getUniqueId();
			
			final int taskId = p.getServer().getScheduler().runTaskTimer(p, new Runnable() {
				@Override
				public void run() {
					player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
					player.getWorld().playEffect(player.getLocation(), Effect.LAVA_POP, 1);
				}
			}, 0, 5L).getTaskId();
			
			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					p.getGamePlayer(playerID).setDoubleDamages(false);
					p.getServer().getScheduler().cancelTask(taskId);
				}
			}, EFFECT_DURATION * 20L);
			
		}
		else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {

	}
}
