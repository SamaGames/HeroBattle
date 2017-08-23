package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
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
public class BouncingFireballTool extends PlayerTool
{

	// TODO not finished yet

	public BouncingFireballTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.bouncingball";
	}

	@Override
	public String getName()
	{
		return ChatColor.GREEN + "" + ChatColor.BOLD + "Balle rebondissante";
	}

	@Override
	public List<String> getDescription()
	{
		return null;
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.MAGMA_CREAM);
		ToolsUtils.resetTool(item);
		return item;
	}

	@Override
	public void onRightClick(final Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (ToolsUtils.isToolAvailable(tool))
		{
			new ItemCooldown(p, player, this, 3);

			final ArmorStand am = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
			am.setVisible(false);
			am.setSmall(true);

			final Vector v = player.getLocation().getDirection().setY(0).normalize().multiply(0.3);
			am.setVelocity(v.clone().setY(0.5));
			am.getWorld().playSound(am.getLocation(), Sound.SHOOT_ARROW, 1, 0.5F);

			new Runnable()
			{
				private BukkitTask task = null;

				@Override
				public void run()
				{
					task = Bukkit.getScheduler().runTaskTimer(p, () -> {
						// Effects
						for (int i = 0; i < 5; i++)
							am.getWorld().playEffect(am.getLocation(), Effect.SNOWBALL_BREAK, 0);

						for (int i = 0; i < 20; i++)
							am.getWorld().playEffect(am.getLocation(), Effect.COLOURED_DUST, 0);

						// Check for wall bouncing
						if (Math.abs(am.getVelocity().getX()) < 0.01 && Math.abs(am.getVelocity().getZ()) < 0.01)
						{
							v.setX(-v.getX());
							v.setZ(-v.getZ());

							am.setVelocity(new Vector(v.getX(), am.getLocation().add(0, -0.1, 0).getBlock().getType() == Material.AIR ? am.getVelocity().getY() : 0.5, v.getZ()));

							am.getWorld().playSound(am.getLocation(), Sound.SLIME_WALK2, 1, 1.5F);

							return;
						}

						// Check for ground bouncing
						if (am.getLocation().add(0, -0.1, 0).getBlock().getType() != Material.AIR)
						{
							am.getWorld().playSound(am.getLocation(), Sound.SLIME_WALK, 1, 1);
						}

						am.setVelocity(new Vector(v.getX(), am.getLocation().add(0, -0.1, 0).getBlock().getType() == Material.AIR ? am.getVelocity().getY() : 0.5, v.getZ()));

						// Check for target
						for (HeroBattlePlayer heroBattlePlayer : p.getGame().getGamePlayers().values())
						{
							Player target = Bukkit.getPlayer(heroBattlePlayer.getUUID());
							if (target == null || target.equals(player)) continue;

							Location loc = am.getLocation().clone();
							loc.setY(target.getLocation().getY());

							if (target.getLocation().distanceSquared(loc) < 1 && Math.abs(target.getLocation().getY() - am.getLocation().getY()) < 2)
							{

								heroBattlePlayer.damage(10, 20, p.getGamePlayer(player), am.getLocation());
								am.remove();

								task.cancel();
								return;
							}
						}
					}, 1, 1);
				}
			}.run();
		}
		else
		{
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
	}

}
