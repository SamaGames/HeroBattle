package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.Weapon;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class SwordVariant6Tool extends SwordTool implements Weapon {

	public SwordVariant6Tool(HeroBattle plugin) {
		super(plugin);
	}
	
	@Override
	public String getToolID() {
		return "tool.sword.variant6";
	}

	@Override
	public String getName() {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "Lame dorée";
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.GOLD_SWORD, 1);

		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);

		return item;
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList(
				ChatColor.GRAY + "Frappez les joueurs pour les repousser.",
				ChatColor.GRAY + "Cliquez droit pour faire un double saut.",
				"",
				ChatColor.AQUA + "Effet spécial",
				ChatColor.GRAY + "Vous avez " + ChatColor.GOLD + (4 + upgrade) + ChatColor.GRAY + "% de chance de brûler votre cible", ChatColor.GRAY + "pendant " + ChatColor.GOLD + "2 " + ChatColor.GRAY + "secondes à chaque coup porté"
		);
	}

	@Override
	public void onPlayerHit(final Player sender, final Player victim) {
		double n = 0.04 + upgrade * 0.01;
		if(random.nextDouble() <= n) {

			int duration = sender.getFireTicks() + 2 * 20;

			victim.setFireTicks(duration);

			p.getGame().getFiresInProgress().put(victim.getUniqueId(), sender.getUniqueId());
			p.getServer().getScheduler().runTaskLaterAsynchronously(p, new Runnable() {
				@Override
				public void run() {
					p.getGame().getFiresInProgress().remove(victim.getUniqueId());
				}
			}, duration);
		}
	}

}
