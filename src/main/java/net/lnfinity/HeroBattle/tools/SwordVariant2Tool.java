package net.lnfinity.HeroBattle.tools;

import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;
import net.samagames.utils.GlowEffect;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SwordVariant2Tool extends SwordTool implements Weapon {

	public SwordVariant2Tool(HeroBattle plugin) {
		super(plugin);
	}
	
	@Override
	public String getToolID() {
		return "tool.sword.variant2";
	}

	@Override
	public String getName() {
		return ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Baguette Magique";
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);

		GlowEffect.addGlow(item);
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
				ChatColor.GRAY + "Vous avez " + ChatColor.GOLD + ((int) 5 + upgrade) + ChatColor.GRAY + "% de chance gagner un effet", ChatColor.GRAY + "de vitesse de " + ChatColor.GOLD + "2 " + ChatColor.GRAY + "secondes à chaque coup porté"
		);
	}

	@Override
	public void onPlayerHit(Player sender, Player victim) {
		double n = 0.05 + upgrade * 0.01;
		if(random.nextDouble() <= n) {
			sender.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2 * 20, 0));
		}
		
	}

}
