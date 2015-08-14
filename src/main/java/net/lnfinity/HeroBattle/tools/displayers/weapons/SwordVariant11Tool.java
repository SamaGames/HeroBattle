package net.lnfinity.HeroBattle.tools.displayers.weapons;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.Weapon;
import net.md_5.bungee.api.ChatColor;

public class SwordVariant11Tool extends SwordTool implements Weapon {

	public SwordVariant11Tool(HeroBattle plugin) {
		super(plugin);
	}

	@Override
	public String getToolID() {
		return "tool.sword.variant11";
	}

	@Override
	public String getName() {
		return ChatColor.GRAY + "" + ChatColor.BOLD + "Foreuse";
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE, 1);

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
				ChatColor.GRAY + "Vous avez " + ChatColor.GOLD + 1 + ChatColor.GRAY + "% de chance d'obtenir de la vision nocturne ", ChatColor.GRAY + "pendant " + ChatColor.GOLD + "10 " + ChatColor.GRAY + "secondes à chaque coup porté"
		);
	}

	@Override
	public void onPlayerHit(final Player sender, final Player victim) {
		double n = 0.01;

		if(random.nextDouble() <= n) {
			victim.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 10 * 20, 0, true, false));
		}
	}

}
