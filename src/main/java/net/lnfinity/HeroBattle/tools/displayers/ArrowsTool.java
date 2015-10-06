package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.TripleParameters;
import net.lnfinity.HeroBattle.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;


public class ArrowsTool extends PlayerTool
{

	protected final int COOLDOWN; // seconds
	protected final int MIN_EXPLOSION;
	protected final int MAX_EXPLOSION;
	private final int ARROWS_TO_FIRE;
	private final int MIN_POWER;
	private final int MAX_POWER;
	private Integer taskId = null;

	public ArrowsTool(HeroBattle plugin, int cooldown, int count, int min, int max, int minExplosion, int maxExplosion)
	{
		super(plugin);
		COOLDOWN = cooldown;
		ARROWS_TO_FIRE = count;
		MIN_POWER = min;
		MAX_POWER = max;
		MIN_EXPLOSION = minExplosion;
		MAX_EXPLOSION = maxExplosion;
	}

	@Override
	public String getToolID()
	{
		return "tool.arrows";
	}

	@Override
	public String getName()
	{
		return ChatColor.GRAY + "" + ChatColor.BOLD + "Arc";
	}

	@Override
	public List<String> getDescription()
	{
		return Utils.getToolDescription(ChatColor.GRAY + "Clic gauche pour lancer une flèche explosant uniquement au contact d'un joueur. A son explosion, elle cause entre " + ChatColor.RED + MIN_EXPLOSION + " " + ChatColor.GRAY + "à " + ChatColor.RED + MAX_EXPLOSION + " " + ChatColor.GRAY + "dégâts au joueur touché et à ceux dans un rayon de " + ChatColor.GOLD + "3 " + ChatColor.GRAY + "autour. Clic droit pour lancer une pluie de " + ChatColor.GOLD + ARROWS_TO_FIRE + " " + ChatColor.GRAY + "flèches occasionant chacune " + ChatColor.RED + MIN_POWER + " " + ChatColor.GRAY + "à " + ChatColor.RED + MAX_POWER + " " + ChatColor.GRAY + "dégâts. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.BOW);
		ToolsUtils.resetTool(item);
		return item;
	}

	@Override
	public void onRightClick(final Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (ToolsUtils.isToolAvailable(tool))
		{
			if (taskId != null)
			{
				try
				{
					p.getServer().getScheduler().cancelTask(taskId);
				}
				catch (IllegalStateException ignored) {}
			}
			taskId = p.getServer().getScheduler().runTaskTimer(p, new Runnable()
			{
				int loop = 1;

				@Override
				public void run()
				{
					player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 1L, 1L);

					double pitch = ((player.getLocation().getPitch() + 90) * Math.PI) / 180;
					double yaw = ((player.getLocation().getYaw() + 90) * Math.PI) / 180;
					double x = Math.sin(pitch) * Math.cos(yaw);
					double y = Math.sin(pitch) * Math.sin(yaw);
					double z = Math.cos(pitch);

					Vector vector = new Vector(x, z, y);
					vector.multiply(2);

					Location loc = player.getLocation();
					loc.setY(loc.getY() + 1);

					Arrow arrow = player.getWorld().spawn(loc, Arrow.class);
					arrow.setShooter(player);
					arrow.setVelocity(vector);

					p.getGame().addEntityParameters(arrow.getUniqueId(), new TripleParameters(MIN_POWER, MAX_POWER));

					if (loop >= ARROWS_TO_FIRE)
					{
						p.getServer().getScheduler().cancelTask(taskId);
						taskId = null;
					}
					loop++;
				}
			}, 0L, 5L).getTaskId();
			new ItemCooldown(p, player, this, COOLDOWN);
		}
		else
		{
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}

	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (ToolsUtils.isToolAvailable(tool))
		{
			new ItemCooldown(p, player, this, COOLDOWN);

			player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 1L, 1L);

			double pitch = ((player.getLocation().getPitch() + 90) * Math.PI) / 180;
			double yaw = ((player.getLocation().getYaw() + 90) * Math.PI) / 180;
			double x = Math.sin(pitch) * Math.cos(yaw);
			double y = Math.sin(pitch) * Math.sin(yaw);
			double z = Math.cos(pitch);

			Vector vector = new Vector(x, z, y);
			vector.multiply(2);

			Location loc = player.getLocation();
			loc.setY(loc.getY() + 1);

			Arrow arrow = player.getWorld().spawn(loc, Arrow.class);
			arrow.setShooter(player);
			arrow.setVelocity(vector);
			arrow.setCustomName(" ");
			arrow.setFireTicks(Integer.MAX_VALUE);

			p.getGame().addEntityParameters(arrow.getUniqueId(), new TripleParameters(MIN_EXPLOSION, MAX_EXPLOSION));

		}
		else
		{
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}
}
