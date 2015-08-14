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


public class DrillSwordTool extends SwordTool implements Weapon
{

	private final Double NIGHT_VISION_PROBABILITY = 0.01;
	private final Integer NIGHT_VISION_DURATION = 10;


	public DrillSwordTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.sword.drill";
	}

	@Override
	public String getName()
	{
		return ChatColor.GRAY + "" + ChatColor.BOLD + "Foreuse";
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE, 1);

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
				ChatColor.AQUA + "Effet spécial",
				ChatColor.GRAY + "Vous avez " + ChatColor.GOLD + ((int) (NIGHT_VISION_PROBABILITY * 100)) + ChatColor.GRAY + "% de chance d'obtenir de la vision nocturne ", ChatColor.GRAY + "pendant " + ChatColor.GOLD + NIGHT_VISION_DURATION + ChatColor.GRAY + " secondes à chaque coup porté"
		);
	}

	@Override
	public void onPlayerHit(final Player sender, final Player victim)
	{
		if (random.nextDouble() <= NIGHT_VISION_PROBABILITY)
		{
			victim.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, NIGHT_VISION_DURATION * 20, 0, true, false));
		}
	}
}
