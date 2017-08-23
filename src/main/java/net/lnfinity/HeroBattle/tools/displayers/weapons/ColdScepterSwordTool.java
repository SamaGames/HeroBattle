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
