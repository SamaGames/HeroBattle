package net.lnfinity.HeroBattle.tools.displayers.weapons;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.tools.*;
import net.md_5.bungee.api.ChatColor;
import net.samagames.tools.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.*;

import java.util.*;


public class MagicWandSwordTool extends SwordTool implements Weapon
{
	public MagicWandSwordTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.sword.magicWand";
	}

	@Override
	public String getName()
	{
		return ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Baguette Magique";
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);

		GlowEffect.addGlow(item);
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
				ChatColor.GRAY + "Vous avez " + ChatColor.GOLD + (5 + upgrade) + ChatColor.GRAY + "% de chance gagner un effet", ChatColor.GRAY + "de vitesse de " + ChatColor.GOLD + "2 " + ChatColor.GRAY + "secondes à chaque coup porté"
		);
	}

	@Override
	public void onPlayerHit(Player sender, Player victim)
	{
		double n = 0.05 + upgrade * 0.01;
		if (random.nextDouble() <= n)
		{
			sender.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2 * 20, 0));
		}

	}
}
