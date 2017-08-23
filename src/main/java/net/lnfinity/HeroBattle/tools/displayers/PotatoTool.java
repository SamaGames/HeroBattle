package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
public class PotatoTool extends PlayerTool
{

	private final int COOLDOWN;
	private final int DURATION;

	public PotatoTool(HeroBattle plugin)
	{
		super(plugin);

		COOLDOWN = 30;
		DURATION = 10;
	}

	@Override
	public String getToolID()
	{
		return "tool.potato";
	}

	@Override
	public String getName()
	{
		return ChatColor.YELLOW + "" + ChatColor.BOLD + "La patoche";
	}

	@Override
	public List<String> getDescription()
	{
		return Utils.getToolDescription(ChatColor.GRAY + "Parce que la patate c'est bon, j'empoisonne mes ennemis pendant " + ChatColor.GOLD + DURATION + " " + ChatColor.GRAY + "secondes. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.POISONOUS_POTATO);
		ToolsUtils.resetTool(item);

		return item;
	}

	@Override
	public void onRightClick(final Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (ToolsUtils.isToolAvailable(tool))
		{
			new ItemCooldown(p, player, this, COOLDOWN);
			player.getWorld().playSound(player.getLocation(), Sound.WITHER_SPAWN, 1, 2);

			final List<UUID> victims = new ArrayList<>();
			player.getNearbyEntities(5, 5, 5).stream()
					.filter(entity -> entity instanceof Player)
					.forEach(entity -> {
								Player victim = (Player) entity;
								victim.playSound(victim.getLocation(), Sound.AMBIENCE_CAVE, 1, 2);
								victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * DURATION, 0));

								victims.add(victim.getUniqueId());
							}
					);

			for (UUID victim : victims)
			{
				p.getGame().getPoisonsInProgress().put(victim, player.getUniqueId());
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
