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
