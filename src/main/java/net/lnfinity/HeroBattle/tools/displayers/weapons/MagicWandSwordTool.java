package net.lnfinity.HeroBattle.tools.displayers.weapons;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.Weapon;
import net.md_5.bungee.api.ChatColor;
import net.samagames.tools.GlowEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

/*
 * This file is part of HeroBattle.
 *
 * HeroBattle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HeroBattle is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HeroBattle.  If not, see <http://www.gnu.org/licenses/>.
 */
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
