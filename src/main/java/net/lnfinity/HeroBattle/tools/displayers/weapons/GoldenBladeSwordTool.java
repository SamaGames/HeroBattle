package net.lnfinity.HeroBattle.tools.displayers.weapons;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.Weapon;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
			p.getServer().getScheduler().runTaskLaterAsynchronously(p, () -> p.getGame().getFiresInProgress().remove(victim.getUniqueId()), duration);
		}
	}

}
