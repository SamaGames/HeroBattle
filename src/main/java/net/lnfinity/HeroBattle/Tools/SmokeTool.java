package net.lnfinity.HeroBattle.Tools;

import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Utils.ItemCooldown;
import net.samagames.utils.GlowEffect;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SmokeTool extends PlayerTool {

	private final int COOLDOWN = 30;

	public SmokeTool(HeroBattle p) {
		super(p);
	}

	@Override
	public String getToolID() {
		return "tool.smoke";
	}

	@Override
	public String getName() {
		return ChatColor.DARK_GRAY + "Bombe fumigène";
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList(ChatColor.GRAY + "Allume une bombe fumigène à vos pieds et vous fait disparaître.", "",
				ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Clic droit pour activer l'effet.", ChatColor.DARK_GRAY
						+ "" + ChatColor.ITALIC + "Ne peut être utilisé que toutes les " + COOLDOWN + " secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.FIREWORK_CHARGE);
		GlowEffect.addGlow(item);
		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (tool.containsEnchantment(GlowEffect.getGlow())) {
			new ItemCooldown(p, player, this, COOLDOWN);
			for (int i = 0; i <= 100; i++) {
				Location loc = player.getLocation().clone();
				loc.setX(loc.getX() + 5 - ((int) (Math.random() * ((10 - 0) + 0))));
				loc.setY(loc.getY() + 5 - ((int) (Math.random() * ((10 - 0) + 0))));
				loc.setZ(loc.getZ() + 5 - ((int) (Math.random() * ((10 - 0) + 0))));
				player.getWorld().playEffect(loc, Effect.EXPLOSION_HUGE, 10, 10);
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5 * 20, 0, false, false));
			player.getWorld().playSound(player.getLocation(), Sound.BAT_LOOP, 1, 2);
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {

	}

}
