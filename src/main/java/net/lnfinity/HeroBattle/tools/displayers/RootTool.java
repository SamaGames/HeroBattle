package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.tools.*;
import net.lnfinity.HeroBattle.utils.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.scheduler.*;
import org.bukkit.util.Vector;

import java.util.*;


public class RootTool extends PlayerTool
{

	// TODO not finished yet

	private final int COOLDOWN;

	public RootTool(HeroBattle plugin, int cooldown)
	{
		super(plugin);

		COOLDOWN = cooldown;
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
	public List<String> getDescription()
	{
		return null;
	}

	@Override
	public ItemStack getItem()
	{
		final ItemStack item = new ItemStack(Material.DEAD_BUSH);
		ToolsUtils.resetTool(item);

		return item;
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

			System.out.println(Bukkit.getPlayer(best.getUUID()).getLocation().distance(player.getLocation()));

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
									victim.damage(0);

									for (int k = 0; k < 5; k++)
										victim.getWorld().playEffect(currentLoc, Effect.STEP_SOUND, Material.LEAVES.getId(), 10);
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
