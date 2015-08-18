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


public class AppleWhipSwordTool extends WroughtIronSwordTool implements Weapon
{

	public AppleWhipSwordTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.sword.appleWhip";
	}

	@Override
	public String getName()
	{
		return ChatColor.RED + "" + ChatColor.BOLD + "POMME QUI FOUETTE";
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.CARROT_STICK, 1);

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
				ChatColor.GRAY + "Vous avez " + ChatColor.GOLD + 2 + ChatColor.GRAY + "% de chance d'asperger votre cible ", ChatColor.GRAY + "pendant " + ChatColor.GOLD + "3 " + ChatColor.GRAY + "secondes à chaque coup porté"
		);
	}

	@Override
	public void onPlayerHit(final Player sender, final Player victim)
	{
		double n = 0.02;

		if (random.nextDouble() <= n)
		{
			victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 0, true, false));
		}
	}
}
