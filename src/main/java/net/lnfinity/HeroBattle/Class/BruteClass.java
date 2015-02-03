package net.lnfinity.HeroBattle.Class;

import net.lnfinity.HeroBattle.Utils.GlowEffect;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class BruteClass extends PlayerClass {

	public BruteClass() {
		
		ItemStack item = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.RED + "Épée repoussante");
		meta.setLore(Arrays.asList(ChatColor.GRAY + "Frappez les joueurs pour les repousser", ChatColor.GRAY
				+ "Clic droit pour faire un double saut"));
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);
		items[0] = item;
		
		item = new ItemStack(Material.SUGAR, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BLUE + "Boost vitesse");
		meta.setLore(Arrays.asList(ChatColor.GRAY + "Clic droit pour utiliser"));
		item.setItemMeta(meta);
		GlowEffect.addGlow(item);
		items[1] = item;
		
		item = new ItemStack(Material.BLAZE_POWDER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.DARK_RED + "Poudre de puissance");
		meta.setLore(Arrays.asList(ChatColor.GRAY + "Clic droit pour utiliser"));
		item.setItemMeta(meta);
		GlowEffect.addGlow(item);
		items[2] = item;
	}

	@Override
	public String getName() {
		return "Brute";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemStack(Material.DIAMOND_CHESTPLATE);
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList("Pour le plaisir de faire des dégâts.");
	}

	@Override
	public int getMinDamages() {
		return 1;
	}

	@Override
	public int getMaxDamages() {
		return 8;
	}

	@Override
	public int getLives() {
		return 3;
	}

}
