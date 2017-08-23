package net.lnfinity.HeroBattle.tools.displayers;


import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

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
public class RootTool extends PlayerTool
{

	private final int COOLDOWN;
	private final int MIN_DAMAGES;
	private final int MAX_DAMAGES;

	public RootTool(HeroBattle plugin, int cooldown, int min, int max)
	{

		super(plugin);

		COOLDOWN = cooldown;
		MIN_DAMAGES = min;
		MAX_DAMAGES = max;
	}

	@Override
	public String getToolID()
	{
		return "tool.root";
	}

	@Override
	public String getName()
	{
		return ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Déracinement";
	}

	@Override
	public ItemStack getItem()
	{
		final ItemStack item = new ItemStack(Material.VINE);
		ToolsUtils.resetTool(item);

		return item;
	}

	@Override
	public List<String> getDescription()
	{
		return Utils.getToolDescription(ChatColor.GRAY + "Une racine est violement projetée vers le joueur le plus proche, elle s'agrippe à se dernier et le tire avec force vers vous en occasionant " + ChatColor.RED + MIN_DAMAGES + " " + ChatColor.GRAY + "à " + ChatColor.RED + MAX_DAMAGES + " " + ChatColor.GRAY + "dégâts. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public void onRightClick(final Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (ToolsUtils.isToolAvailable(tool))
		{
			new ItemCooldown(p, player, this, COOLDOWN);

			HeroBattlePlayer best = null;

			for (HeroBattlePlayer heroBattlePlayer : p.getGame().getGamePlayers().values())
			{
				if (heroBattlePlayer.isSpectator()) continue;

				Player victim = Bukkit.getPlayer(heroBattlePlayer.getUUID());

				if (!victim.equals(player) && (best == null || victim.getLocation().distanceSquared(player.getLocation()) < Bukkit.getPlayer(best.getUUID()).getLocation().distanceSquared(player.getLocation())))
					best = heroBattlePlayer;
			}

			final HeroBattlePlayer closest = best;

			if (best == null || Bukkit.getPlayer(best.getUUID()).getLocation().distance(player.getLocation()) > 10)
			{
				player.sendMessage(ChatColor.RED + "Il n'y a aucun joueur alentour à déraciner !");
				player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 1, 0.5F);
			}
			else
			{
				final Player victim = Bukkit.getPlayer(closest.getUUID());

				player.getWorld().playSound(player.getLocation(), Sound.CHEST_OPEN, 1, 0.5F);

				final Location loc1 = player.getLocation();
				final Location loc2 = victim.getLocation();
				final Vector vec = new Vector(loc2.getX() - loc1.getX(), loc2.getY() - loc1.getY(), loc2.getZ() - loc1.getZ()).multiply(0.05);

				new Runnable()
				{
					private BukkitTask task = null;

					@Override
					public void run()
					{
						task = Bukkit.getScheduler().runTaskTimer(p, new Runnable()
						{
							private Location currentLoc = loc1;
							private int i = 0;

							@Override
							public void run()
							{
								victim.getWorld().playEffect(currentLoc, Effect.STEP_SOUND, Material.LOG.getId(), 0);

								currentLoc.add(vec);

								if (i < 20)
								{
									i++;
								}
								else
								{
									task.cancel();

									victim.setVelocity(new Vector(player.getLocation().getX() - victim.getLocation().getX(), player.getLocation().getY() - victim.getLocation().getY(), player.getLocation().getZ() - victim.getLocation().getZ()).normalize().multiply(2));

									closest.basicDamage(Utils.randomNumber(MIN_DAMAGES, MAX_DAMAGES), p.getGamePlayer(player));

									for (int k = 0; k < 5; k++)
									{
										victim.getWorld().playEffect(currentLoc, Effect.STEP_SOUND, Material.LEAVES.getId(), 10);
									}
								}

							}
						}, 2, 1);
					}

				}.run();
			}
		}
		else
		{
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}
}
