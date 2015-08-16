/*
 * Copyright (C) 2015 Amaury Carrade
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.tools.*;
import net.lnfinity.HeroBattle.utils.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;


public class PikachuShockBlastTool extends PlayerTool
{
	private final Integer COOLDOWN = 30;
	private final Integer RADIUS = 7;

	private final Integer DAMAGES_MIN = 20;
	private final Integer DAMAGES_MAX = 48;


	public PikachuShockBlastTool()
	{
		super(HeroBattle.get());
	}


	@Override
	public String getToolID()
	{
		return "tools.shockBlast";
	}

	@Override
	public String getName()
	{
		return ChatColor.YELLOW + "" + ChatColor.BOLD + "Shock Blast";
	}

	@Override
	public List<String> getDescription()
	{
		return Utils.getToolDescription(
				ChatColor.GRAY + "Envoie une vague électrique autour de vous lorsque vous l'utilisez, occasionnant de " + ChatColor.GOLD + DAMAGES_MIN + ChatColor.GRAY + " à " + ChatColor.GOLD + DAMAGES_MAX + ChatColor.GRAY + "% de dégâts selon la distance des cibles. Ne peut être utilisé qu'au sol."
		);
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack icon = new ItemStack(Material.NETHER_STAR);
		ToolsUtils.resetTool(icon);

		return icon;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (!ToolsUtils.isToolAvailable(tool))
		{
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser cela maintenant. Chuu!");
			return;
		}

		// Only on the ground
		if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR)
		{
			player.sendMessage(ChatColor.GOLD + "Vous devez être au sol pour lancer ceci !");
			return;
		}

		GamePlayer gPlayer = HeroBattle.get().getGamePlayer(player.getUniqueId());


		// Effects

		final Location center = player.getLocation();
		center.setPitch(0f);

		player.getWorld().strikeLightningEffect(center);

		center.add(0, 2, 0);

		final Firework fw = center.getWorld().spawn(Utils.blockLocation(center), Firework.class);
		FireworkMeta fwm = fw.getFireworkMeta();
		FireworkEffect effect = FireworkEffect.builder()
				.withColor(Color.YELLOW).with(FireworkEffect.Type.BALL_LARGE)
				.withFade(Color.WHITE).build();
		fwm.addEffects(effect);
		fwm.setPower(0);
		fw.setFireworkMeta(fwm);

		Bukkit.getScheduler().runTaskLater(p, new Runnable()
		{
			@Override
			public void run()
			{
				fw.detonate();
			}
		}, 1l);

		// Particle circles
		center.add(0, -0.6, 0);

		for (int i = 1; i < 5; i++)
		{
			Bukkit.getScheduler().runTaskLater(p, new Runnable()
			{
				@Override
				public void run()
				{
					generateParticleCircle(center);
				}
			}, i * 2l);
		}

		Integer radiusSquared = (int) Math.pow(RADIUS, 2);
		for(GamePlayer gVictim : HeroBattle.get().getGamePlayers().values())
		{
			if(!gVictim.isPlaying()) continue;
			if(gVictim.getPlayerUniqueID().equals(player.getUniqueId())) continue;

			Player victim = Bukkit.getPlayer(gVictim.getPlayerUniqueID());
			Double distance = victim.getLocation().distanceSquared(center);
			if(distance <= radiusSquared)
			{
				Float force = ((float) (1 - (distance / radiusSquared)));
				gVictim.damage((int) (DAMAGES_MIN + force * (DAMAGES_MAX - DAMAGES_MIN)), gPlayer, center);
			}
		}

		new ItemCooldown(HeroBattle.get(), player, this, COOLDOWN);
	}

	private void generateParticleCircle(Location center)
	{
		final double particleCount = 200d;
		final float angleBetweenParticles = ((float) (360f / particleCount));

		for(float yaw = 0; yaw <= 360; yaw += angleBetweenParticles)
		{
			center.setYaw(yaw);
			ParticleEffect.FIREWORKS_SPARK.display(center.getDirection(), 0.6f, center, 256);
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		onRightClick(player, tool, event);
	}
}
