package net.lnfinity.HeroBattle.tools.displayers.weapons;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.Weapon;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class SwordVariant10Tool extends SwordTool implements Weapon {

	public SwordVariant10Tool(HeroBattle plugin) {
		super(plugin);
	}

	@Override
	public String getToolID() {
		return "tool.sword.variant10";
	}

	@Override
	public String getName() {
		return ChatColor.RED + "" + ChatColor.BOLD + "POMME QUI FOUETTE";
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.CARROT_STICK, 1);

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
				ChatColor.GRAY + "Vous avez " + ChatColor.GOLD + 2 + ChatColor.GRAY + "% de chance d'asperger votre cible ", ChatColor.GRAY + "pendant " + ChatColor.GOLD + "3 " + ChatColor.GRAY + "secondes à chaque coup porté"
		);
	}

	@Override
	public void onPlayerHit(final Player sender, final Player victim) {
		double n = 0.02;

		if(random.nextDouble() <= n) {
			victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3*20, 0, true, false));
		}
	}

}
