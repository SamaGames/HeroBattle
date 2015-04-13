package net.lnfinity.HeroBattle.tools.displayers;

import java.util.List;
import java.util.Random;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import net.samagames.utils.GlowEffect;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HealingTool extends PlayerTool {

	private final int COOLDOWN;
	private final int POWER;
	private final double PROBABILITY;

	private Random random = null;

	public HealingTool(HeroBattle plugin, int cooldown, int power, double probability) {
		super(plugin);
		COOLDOWN = cooldown;
		POWER = power;
		PROBABILITY = probability;

		random = new Random();
	}

	@Override
	public String getToolID() {
		return "tool.healing";
	}

	@Override
	public String getName() {
		return ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Soin";
	}

	@Override
	public List<String> getDescription() {
		return Utils.getToolDescription(ChatColor.GRAY + "Vous soigne de " + ChatColor.GOLD + POWER + " " + ChatColor.GRAY + "pourcents. Attention, vous avez " + ChatColor.RED + (int) (PROBABILITY * 100) + ChatColor.GRAY + "% " + " de chance d'échouer ce qui vous ralentira ! Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.MUSHROOM_SOUP);
		GlowEffect.addGlow(item);
		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (ToolsUtils.isToolAvailable(tool)) {
			new ItemCooldown(p, player, this, COOLDOWN);
			if (random.nextDouble() >= PROBABILITY) {
				GamePlayer gamePlayer = p.getGamePlayer(player);
				int newPercentage = gamePlayer.getPercentage() - POWER;
				if (newPercentage < 0) {
					newPercentage = 0;
				}
				gamePlayer.setPercentage(newPercentage);
				player.playSound(player.getLocation(), Sound.FIZZ, 1, 1);
				p.getScoreboardManager().refresh();
			} else {
				player.sendMessage(ChatColor.RED + "Vous échouez votre capacité.");
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 15 * 20, 0));
			}
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {

	}

}
