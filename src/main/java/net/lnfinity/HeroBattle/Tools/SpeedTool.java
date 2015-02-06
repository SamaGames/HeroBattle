package net.lnfinity.HeroBattle.Tools;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Utils.GlowEffect;
import net.lnfinity.HeroBattle.Utils.ItemCooldown;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;


public class SpeedTool extends PlayerTool {

	private final int COOLDOWN = 20; // seconds

	public SpeedTool(HeroBattle plugin) {
		super(plugin);
	}

	@Override
	public String getToolID() {
		return "tool.speed";
	}

	@Override
	public String getName() {
		return ChatColor.BLUE + "Boost de vitesse";
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList(
				ChatColor.GRAY + "Clic droit pour activer l'effet.",
				ChatColor.DARK_GRAY + "Ne peut être utilisé que toutes les " + COOLDOWN + " secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.SUGAR, 1);
		GlowEffect.addGlow(item);

		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if(tool.containsEnchantment(GlowEffect.getGlow())) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
			new ItemCooldown(p, player, this, COOLDOWN);
		}
		else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {

	}
}
