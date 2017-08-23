package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.samagames.tools.GlowEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
public class AnguilleTool extends PlayerTool
{

	private final int COOLDOWN;
	private final int DURATION;

	public AnguilleTool(HeroBattle plugin)
	{
		super(plugin);

		COOLDOWN = 20;
		DURATION = 6;
	}

	@Override
	public String getToolID()
	{
		return "tool.anguille";
	}

	@Override
	public String getName()
	{
		return ChatColor.DARK_RED + "" + ChatColor.BOLD + "L'anguille";
	}

	@Override
	public List<String> getDescription()
	{
		return Utils.getToolDescription(ChatColor.GRAY + "Enflamme les joueurs trop proches de vous pendant " + ChatColor.GOLD + DURATION + " " + ChatColor.GRAY + "secondes, en tant que vengeance pour nous avoir martyrisé ! Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.RAW_FISH);
		GlowEffect.addGlow(item);
		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (ToolsUtils.isToolAvailable(tool))
		{
			new ItemCooldown(p, player, this, COOLDOWN);
			player.getWorld().playSound(player.getLocation(), Sound.CAT_HISS, 1, 1);

			final List<UUID> victims = new ArrayList<>();

			player.getNearbyEntities(5, 5, 5).stream()
					.filter(entity -> entity instanceof Player)
					.forEach(entity -> {
								entity.setFireTicks(20 * DURATION);
								victims.add(entity.getUniqueId());
							}
					);

			for (UUID victim : victims)
			{
				p.getGame().getFiresInProgress().put(victim, player.getUniqueId());
			}

			p.getServer().getScheduler().runTaskLaterAsynchronously(p, () -> {
				for (UUID victim : victims)
				{
					p.getGame().getPoisonsInProgress().remove(victim);
				}
			}, 20 * DURATION);

		}
		else
		{
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}
}
