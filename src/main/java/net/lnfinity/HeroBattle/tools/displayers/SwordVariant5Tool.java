package net.lnfinity.HeroBattle.tools.displayers;

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

public class SwordVariant5Tool extends SwordTool implements Weapon {

	public SwordVariant5Tool(HeroBattle plugin) {
		super(plugin);
	}
	
	@Override
	public String getToolID() {
		return "tool.sword.variant5";
	}

	@Override
	public String getName() {
		return ChatColor.WHITE + "" + ChatColor.BOLD + "Sceptre du froid";
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.IRON_HOE, 1);

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
				ChatColor.AQUA + "Effet spécial:",
				ChatColor.GRAY + "Vous avez " + ChatColor.GOLD + ((int) 10 + upgrade) + ChatColor.GRAY + "% de chance de geler votre cible", ChatColor.GRAY + "pendant " + ChatColor.GOLD + "2 " + ChatColor.GRAY + "secondes à chaque coup porté"
		);
	}

	@Override
	public void onPlayerHit(Player sender, Player victim) {
		double n = 0.1 + upgrade * 0.02;
		if(random.nextDouble() <= n) {
			victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 1));
		}
		
	}

}
