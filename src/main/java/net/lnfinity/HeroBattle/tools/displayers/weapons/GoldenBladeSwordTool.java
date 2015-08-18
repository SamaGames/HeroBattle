package net.lnfinity.HeroBattle.tools.displayers.weapons;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.tools.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;


public class GoldenBladeSwordTool extends SwordTool implements Weapon
{

	public GoldenBladeSwordTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.sword.goldenBlade";
	}

	@Override
	public String getName()
	{
		return ChatColor.GOLD + "" + ChatColor.BOLD + "Lame dorée";
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.GOLD_SWORD, 1);

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
				ChatColor.GRAY + "Vous avez " + ChatColor.GOLD + (4 + upgrade) + ChatColor.GRAY + "% de chance de brûler votre cible", ChatColor.GRAY + "pendant " + ChatColor.GOLD + "4 " + ChatColor.GRAY + "secondes à chaque coup porté"
		);
	}

	@Override
	public void onPlayerHit(final Player sender, final Player victim)
	{
		double n = 0.04 + upgrade * 0.01;
		if (random.nextDouble() <= n)
		{

			int duration = sender.getFireTicks() + 4 * 20;

			victim.setFireTicks(duration);

			p.getGame().getFiresInProgress().put(victim.getUniqueId(), sender.getUniqueId());
			p.getServer().getScheduler().runTaskLaterAsynchronously(p, new Runnable()
			{
				@Override
				public void run()
				{
					p.getGame().getFiresInProgress().remove(victim.getUniqueId());
				}
			}, duration);
		}
	}

}
