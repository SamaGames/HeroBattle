package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
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
public class SaintePommeTool extends PlayerTool
{

	private final Integer COOLDOWN = 55;

	private final Double FAILURE_PROBABILITY = 0.25;
	private final Integer PERCENTAGE_REMOVED = 60;

	private final Random random = new Random();


	public SaintePommeTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.holyApple";
	}

	@Override
	public String getName()
	{
		return ChatColor.GOLD + "" + ChatColor.BOLD + "Sainte POMME";
	}

	@Override
	public List<String> getDescription()
	{
		return Arrays.asList(
				ChatColor.GRAY + "Diminue le pourcentage actuel de quelques pourcents.",
				ChatColor.GRAY + "Mais cet outil ne marche pas à tous les coups..."
		);
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack holy = new ItemStack(Material.GOLDEN_APPLE);
		holy.setDurability((short) 1);

		ToolsUtils.resetTool(holy);
		return holy;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (!ToolsUtils.isToolAvailable(tool))
		{
			player.sendMessage(ChatColor.RED + "Plus de POMMES :c");
			return;
		}

		HeroBattlePlayer gPlayer = HeroBattle.get().getGamePlayer(player);
		if (gPlayer == null || gPlayer.isSpectator()) return;


		if (random.nextDouble() < FAILURE_PROBABILITY)
		{
			player.sendMessage(ChatColor.RED + "Meh, ce n'est pas la sainte pomme...");
		}
		else
		{
			gPlayer.setPercentage(gPlayer.getPercentage() - PERCENTAGE_REMOVED);
			player.sendMessage(ChatColor.RED + "POMMEH PUISSANT");
		}

		new ItemCooldown(HeroBattle.get(), player, this, COOLDOWN);
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		onRightClick(player, tool, event);
	}
}
