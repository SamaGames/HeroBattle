package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
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
public class CrackTool extends PlayerTool
{

	private final int COOLDOWN;
	// TODO
	private final int POWER;

	public CrackTool(HeroBattle plugin, int cooldown, int power)
	{
		super(plugin);

		COOLDOWN = cooldown;
		POWER = power;
	}

	@Override
	public String getToolID()
	{
		return "tool.crack";
	}

	@Override
	public String getName()
	{
		return ChatColor.RED + "" + ChatColor.BOLD + "Fissure";
	}

	@Override
	public List<String> getDescription()
	{
		return Utils.getToolDescription(ChatColor.GRAY + "Le mineur lance violemment sa pioche dans le sol créant ainsi une onde de choc qui dommage de " + ChatColor.RED + POWER + ChatColor.GRAY + " à " + ChatColor.RED + (POWER + 20) + ChatColor.GRAY + " dégâts les joueurs sur sa portée. L'onde se répand sur " + ChatColor.GOLD + "5 " + ChatColor.GRAY + "blocs. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.STONE);
		ToolsUtils.resetTool(item);
		return item;
	}

	@Override
	public void onRightClick(final Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (ToolsUtils.isToolAvailable(tool))
		{
			if (player.getLocation().add(0, -1, 0).getBlock().getType() != Material.AIR && player.getLocation().clone().add(0, -1, 0).getBlock().getType().isBlock())
			{
				new ItemCooldown(p, player, this, COOLDOWN);

				player.getWorld().playSound(player.getLocation(), Sound.ANVIL_LAND, 1F, 2F);

				final ArmorStand am = (ArmorStand) player.getWorld().spawnEntity(new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getBlock().getLocation().add(0, -0.65, 0).getY(), player.getLocation().getZ()), EntityType.ARMOR_STAND);
				am.setVisible(false);
				am.setGravity(false);
				am.setRightArmPose(new EulerAngle(0, Math.PI * 2 * Math.random(), 0));
				am.setItemInHand(new ItemStack(Material.DIAMOND_PICKAXE));

				final List<Player> touched = new ArrayList<>();

				new Runnable()
				{
					private BukkitTask task = null;

					@Override
					public void run()
					{
						task = Bukkit.getScheduler().runTaskTimer(HeroBattle.get(), new Runnable()
						{
							int k = 0;

							@Override
							public void run()
							{

								for (int i = 0; i < 10; i++)
								{
									Location loc = new Location(am.getWorld(), am.getLocation().getX() + Math.sin((double) (i * 2 * Math.PI) / 10) * k * 0.6, am.getLocation().getY() + 0.65, am.getLocation().getZ() + Math.cos((double) (i * 2 * Math.PI) / 10) * k * 0.6);
									if (loc.clone().add(0, -0.65, 0).getBlock().getType() != Material.AIR && loc.clone().add(0, -0.65, 0).getBlock().getType().isSolid())
										am.getWorld().playEffect(loc, Effect.STEP_SOUND, loc.clone().add(0, -1, 0).getBlock().getType().getId(), 5);
								}

								for (HeroBattlePlayer potential : p.getGame().getGamePlayers().values())
								{
									if (potential.isSpectator() || potential.getUUID().equals(player.getUniqueId()))
										continue;

									Player victim = Bukkit.getPlayer(potential.getUUID());
									if (victim == null) continue;

									if (touched.contains(victim)) continue;

									if (victim.getLocation().distanceSquared(am.getLocation()) > Math.pow(k * 0.6, 2))
										continue;

									touched.add(victim);

									// TODO : correct damage
									potential.damage(20, POWER + 20, p.getGamePlayer(player), am.getLocation());
								}

								if (k < 10)
								{
									k++;
								}
								else
								{
									task.cancel();
								}
							}
						}, 4, 2);
					}
				}.run();

				Bukkit.getScheduler().runTaskLater(p, am::remove, 4 * 20);
			}
			else
			{
				player.sendMessage(ChatColor.RED + "Vous êtes trop haut pour lancer votre pioche !");
				player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1F, 0.5F);
			}
		}
		else
		{
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		onRightClick(player, tool, event);
	}
}
