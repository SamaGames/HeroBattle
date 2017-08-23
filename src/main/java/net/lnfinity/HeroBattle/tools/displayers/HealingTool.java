package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;

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
public class HealingTool extends PlayerTool
{

	private final int COOLDOWN;
	private final int POWER;
	private final double PROBABILITY;

	private Random random = null;

	public HealingTool(HeroBattle plugin, int cooldown, int power, double probability)
	{
		super(plugin);
		COOLDOWN = cooldown;
		POWER = power;
		PROBABILITY = probability;

		random = new Random();
	}

	@Override
	public String getToolID()
	{
		return "tool.healing";
	}

	@Override
	public String getName()
	{
		return ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Soin";
	}

	@Override
	public List<String> getDescription()
	{
		return Utils.getToolDescription(ChatColor.GRAY + "Vous soigne de " + ChatColor.GOLD + POWER + " " + ChatColor.GRAY + "pourcents. Attention, vous avez " + ChatColor.RED + (int) (PROBABILITY * 100) + ChatColor.GRAY + "% " + " de chance d'échouer ce qui vous ralentira ! Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.MUSHROOM_SOUP);
		ToolsUtils.resetTool(item);

		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (ToolsUtils.isToolAvailable(tool))
		{
			new ItemCooldown(p, player, this, COOLDOWN);

			if (random.nextDouble() >= PROBABILITY)
			{
				HeroBattlePlayer heroBattlePlayer = p.getGamePlayer(player);

				int newPercentage = Math.max(heroBattlePlayer.getPercentage() - POWER, 0);

				heroBattlePlayer.setPercentage(newPercentage, null);
				player.playSound(player.getLocation(), Sound.FIZZ, 1, 1);
			}

			else
			{
				player.sendMessage(ChatColor.RED + "Vous échouez votre capacité.");
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 15 * 20, 0));
			}
		}

		else
		{
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}
}
