package net.lnfinity.HeroBattle.tools.displayers.weapons;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.tools.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.*;

import java.util.*;


public class ColdScepterSwordTool extends SwordTool implements Weapon
{

	public ColdScepterSwordTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.sword.coldScepter";
	}

	@Override
	public String getName()
	{
		return ChatColor.WHITE + "" + ChatColor.BOLD + "Sceptre du froid";
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.IRON_HOE, 1);

		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);

		return item;
	}

	@Override
	public List<String> getDescription()
	{
		return Arrays.asList(
				ChatColor.GRAY + "Frappez les joueurs pour les repousser.",
				ChatColor.GRAY + "Cliquez droit pour faire un double saut.",
				"",
				ChatColor.AQUA + "Effet spécial:",
				ChatColor.GRAY + "Vous avez " + ChatColor.GOLD + (10 + upgrade * 2) + ChatColor.GRAY + "% de chance de geler votre cible", ChatColor.GRAY + "pendant " + ChatColor.GOLD + "2 " + ChatColor.GRAY + "secondes à chaque coup porté"
		);
	}

	@Override
	public void onPlayerHit(Player sender, Player victim)
	{
		double n = 0.1 + upgrade * 0.02;
		if (random.nextDouble() <= n)
		{
			victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 1));
		}
	}
}
