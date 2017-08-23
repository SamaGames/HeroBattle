package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
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
public class IceTool extends PlayerTool
{

	private final int COOLDOWN; // seconds
	private final int DURATION; // seconds

	private Random random = new Random();

	public IceTool(HeroBattle plugin, int cooldown, int duration)
	{
		super(plugin);
		COOLDOWN = cooldown;
		DURATION = duration;
	}

	@Override
	public String getToolID()
	{
		return "tool.ice";
	}

	@Override
	public String getName()
	{
		return ChatColor.AQUA + "" + ChatColor.BOLD + "Bloc de glace";
	}

	@Override
	public List<String> getDescription()
	{
		return Utils.getToolDescription(ChatColor.GRAY + "Construit un bloc de glace dans la direction visée qui reste pendant " + ChatColor.GOLD + DURATION + " " + ChatColor.GRAY + "secondes. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.ICE);
		ToolsUtils.resetTool(item);

		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (ToolsUtils.isToolAvailable(tool))
		{
			final List<Location> toClean = new ArrayList<>();
			Block b = null;

			try
			{
				b = p.getGame().getTargetBlock(player, 20).getLocation().clone().add(0, 1, 0).getBlock();
			}
			catch (Exception ignored) {}

			if (b != null)
			{
				new ItemCooldown(p, player, this, COOLDOWN);

				for (HeroBattlePlayer heroBattlePlayer : p.getGame().getGamePlayers().values())
				{
					Player target = p.getServer().getPlayer(heroBattlePlayer.getUUID());
					if (target != null)
					{
						if (target.getLocation().distanceSquared(b.getLocation()) <= 9)
						{
							target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, DURATION * 20, 3));
						}
					}
				}

				buildBlock(b.getLocation().add(0, -1, -1), toClean);
				buildBlock(b.getLocation().add(0, 0, -1), toClean);
				buildBlock(b.getLocation().add(0, 1, -1), toClean);
				buildBlock(b.getLocation().add(0, 2, -1), toClean);

				buildBlock(b.getLocation().add(-1, 0, 0), toClean);
				buildBlock(b.getLocation().add(1, 0, 0), toClean);
				buildBlock(b.getLocation().add(1, 1, 0), toClean);

				buildBlock(b.getLocation().add(-1, 0, 1), toClean);
				buildBlock(b.getLocation().add(0, 0, 1), toClean);
				buildBlock(b.getLocation().add(0, 1, 1), toClean);
				buildBlock(b.getLocation().add(0, 2, 1), toClean);

				buildBlock(b.getLocation().add(-1, -1, 1), toClean);
				buildBlock(b.getLocation().add(-1, 0, 1), toClean);

				buildBlock(b.getLocation().add(-1, -1, 0), toClean);
				buildBlock(b.getLocation().add(-1, 0, 0), toClean);
				buildBlock(b.getLocation().add(-1, 1, 0), toClean);
				buildBlock(b.getLocation().add(-1, 2, 0), toClean);
				buildBlock(b.getLocation().add(-1, 3, 0), toClean);

				buildBlock(b.getLocation().add(0, 2, 0), toClean);
				buildBlock(b.getLocation().add(0, 3, 0), toClean);
				buildBlock(b.getLocation().add(0, 4, 0), toClean);

				p.getServer().getScheduler().runTaskLater(p, () -> {
					for (Location loc : toClean)
					{
						loc.getBlock().setType(Material.AIR);
						loc.getWorld().spigot().playEffect(loc, Effect.TILE_BREAK, 79, 0, 0.0F, 0.0F, 0.0F, 0.0F, 32, 5);
						loc.getWorld().playSound(loc, Sound.GLASS, 1, 1);
					}
				}, DURATION * 20L);
			}
			else
			{
				player.sendMessage(ChatColor.RED + "Vous ne gelez rien !");
			}
		}
		else
		{
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	private void buildBlock(Location loc, List<Location> locs)
	{
		if (loc.getBlock().getType() == Material.AIR)
		{
			loc.getBlock().setType(random.nextBoolean() ? Material.ICE : Material.PACKED_ICE);
			locs.add(loc);
		}
	}
}
