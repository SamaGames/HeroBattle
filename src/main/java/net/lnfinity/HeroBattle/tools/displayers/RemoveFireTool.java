package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
public class RemoveFireTool extends PlayerTool
{

	private final int COOLDOWN;
	private Random random = new Random();

	public RemoveFireTool(HeroBattle plugin, int cooldown)
	{
		super(plugin);
		this.COOLDOWN = cooldown;
	}

	@Override
	public String getToolID()
	{
		return "tool.removefire";
	}

	@Override
	public String getName()
	{
		return ChatColor.AQUA + "" + ChatColor.BOLD + "Eau";
	}

	@Override
	public List<String> getDescription()
	{
		return Utils.getToolDescription(ChatColor.GRAY + "Vous enlève tous les effets de feux. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.WATER_BUCKET);
		ToolsUtils.resetTool(item);

		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (ToolsUtils.isToolAvailable(tool))
		{
			new ItemCooldown(p, player, this, COOLDOWN);

			if (player.getFireTicks() > 0)
			{
				player.setFireTicks(0);
				player.playSound(player.getLocation(), Sound.DIG_SNOW, 1, 2);
				player.sendMessage(ChatColor.GREEN + "Vous n'êtes plus en feu !");

				for (int x = 0; x < 25; x++)
				{
					player.getWorld().playEffect(player.getLocation().add(random.nextDouble() - 0.5, 0, random.nextDouble() - 0.5), Effect.WATERDRIP, 0);
				}
			}
			else
			{
				player.sendMessage(ChatColor.RED + "Vous n'êtes pas en feu !");
			}
		}
		else
		{
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}
}
