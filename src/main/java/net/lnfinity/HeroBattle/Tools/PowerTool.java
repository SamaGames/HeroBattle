package net.lnfinity.HeroBattle.Tools;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Utils.GlowEffect;
import net.lnfinity.HeroBattle.Utils.ItemCouldown;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class PowerTool extends PlayerTool {

	private final int COOLDOWN = 60; // seconds
	private final int EFFECT_DURATION = 10; // seconds

	public PowerTool(HeroBattle plugin) {
		super(plugin);
	}

	@Override
	public String getToolID() {
		return "tool.power";
	}

	@Override
	public String getName() {
		return ChatColor.DARK_RED + "Poudre de puissance";
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList(
				ChatColor.GRAY + "Clic droit pour activer l'effet.",
				ChatColor.DARK_GRAY + "Ne peut être utilisé que toutes les " + COOLDOWN + " secondes."
		);
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
			new ItemCouldown(p, player.getUniqueId(), player.getInventory().getHeldItemSlot(), COOLDOWN);

			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, EFFECT_DURATION * 20, 9));
			p.getGamePlayer(player).setDoubleDamages(true);

			final UUID playerID = player.getUniqueId();

			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					p.getGamePlayer(playerID).setDoubleDamages(false);
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
