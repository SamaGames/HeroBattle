package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.tools.*;
import net.lnfinity.HeroBattle.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;

import java.util.*;

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
		ToolsUtils.resetTool(item);

		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (ToolsUtils.isToolAvailable(tool)) {
			new ItemCooldown(p, player, this, COOLDOWN);

			if (random.nextDouble() >= PROBABILITY) {
				HeroBattlePlayer heroBattlePlayer = p.getGamePlayer(player);

				int newPercentage = Math.max(heroBattlePlayer.getPercentage() - POWER, 0);

				heroBattlePlayer.setPercentage(newPercentage, null);
				player.playSound(player.getLocation(), Sound.FIZZ, 1, 1);
			}

			else {
				player.sendMessage(ChatColor.RED + "Vous échouez votre capacité.");
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 15 * 20, 0));
			}
		}

		else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}
}
