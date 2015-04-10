package net.lnfinity.HeroBattle.tools;

import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
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

	private final int COOLDOWN;
	private final int EFFECT_DURATION;

	public SmokeTool(HeroBattle p, int cooldown, int duration) {
		super(p);
		COOLDOWN = cooldown;
		EFFECT_DURATION = duration;
	}

	@Override
	public String getToolID() {
		return "tool.smoke";
	}

	@Override
	public String getName() {
		return ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Bombe fumigène";
	}

	@Override
	public List<String> getDescription() {
		return Utils.getToolDescription(ChatColor.GRAY + "Lance une bombe fumigène à vos pieds et vous fait disparaitre pendant " + ChatColor.GOLD + EFFECT_DURATION + " " + ChatColor.GRAY + "secondes. Seul l'object sélectionné est alors visible des autres joueurs. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.FIREWORK_CHARGE);
		GlowEffect.addGlow(item);
		return item;
	}

	@Override
	public void onRightClick(final Player player, ItemStack tool, PlayerInteractEvent event) {
		if (ToolsUtils.isToolAvailable(tool)) {
			new ItemCooldown(p, player, this, COOLDOWN);
			for (int i = 0; i <= 100; i++) {
				Location loc = player.getLocation().clone();
				loc.setX(loc.getX() + 5 - ((int) (Math.random() * ((10 - 0) + 0))));
				loc.setY(loc.getY() + 5 - ((int) (Math.random() * ((10 - 0) + 0))));
				loc.setZ(loc.getZ() + 5 - ((int) (Math.random() * ((10 - 0) + 0))));
				player.getWorld().playEffect(loc, Effect.EXPLOSION_HUGE, 10, 10);
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, EFFECT_DURATION * 20, 0, false, false));
			player.getWorld().playSound(player.getLocation(), Sound.BAT_LOOP, 1, 2);
			p.getGamePlayer(player).setInvisible(true);
			p.getGame().updatePlayerArmor(player);
			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					p.getGamePlayer(player).setInvisible(false);
					p.getGame().updatePlayerArmor(player);
				}
			}, 8 * 20L);
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {

	}

}
