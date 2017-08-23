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
