package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.samagames.utils.GlowEffect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
		return Utils.getToolDescription(ChatColor.GRAY + "Enflamme les joueurs trop proches de vous pendant " + ChatColor.GOLD + DURATION + " " + ChatColor.GRAY + "secondes, en tant que vengeance pour nous avoir martyrisé ! Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.RAW_FISH);
		GlowEffect.addGlow(item);
		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (ToolsUtils.isToolAvailable(tool)) {
			new ItemCooldown(p, player, this, COOLDOWN);
			player.getWorld().playSound(player.getLocation(), Sound.CAT_HISS, 1, 1);

			final List<UUID> victims = new ArrayList<>();
			for (Entity entity : player.getNearbyEntities(5, 5, 5)) {
				if (entity instanceof Player) {
					entity.setFireTicks(20 * DURATION);
					victims.add(entity.getUniqueId());
				}
			}

			for(UUID victim : victims) {
				p.getGame().getFiresInProgress().put(victim, player.getUniqueId());
			}

			p.getServer().getScheduler().runTaskLaterAsynchronously(p, new Runnable() {
				@Override
				public void run() {
					for(UUID victim : victims) {
						p.getGame().getPoisonsInProgress().remove(victim);
					}
				}
			}, 20 * DURATION);

		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {

	}

}
