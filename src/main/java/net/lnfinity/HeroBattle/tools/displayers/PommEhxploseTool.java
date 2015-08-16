/**
 * Plugin BelovedBlocks
 * Copyright (C) 2014-2015 Amaury Carrade & Florian Cassayre
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see [http://www.gnu.org/licenses/].
 */

package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.*;
import net.samagames.utils.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;


public class PommEhxploseTool extends PlayerTool
{
	private Random random = new Random();

	private Integer DAMAGES_MIN = 25;
	private Integer DAMAGES_MAX = 35;

	private Double BLINDNESS_PROBABILITY = 0.06;

	private Integer COOLDOWN = 15;


	public PommEhxploseTool(HeroBattle plugin)
	{
		super(plugin);
	}

	@Override
	public String getToolID()
	{
		return "tool.pommexplose";
	}

	@Override
	public String getName()
	{
		return ChatColor.RED + "" + ChatColor.BOLD + "POMMEHXPLOSE";
	}

	@Override
	public List<String> getDescription()
	{
		return Arrays.asList(
				"Lance une pomme explosive sur l'ennemi"
		);
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack apple = new ItemStack(Material.APPLE);
		ToolsUtils.resetTool(apple);

		return apple;
	}

	@Override
	public void onRightClick(final Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if(!ToolsUtils.isToolAvailable(tool))
		{
			player.sendMessage(ChatColor.RED + "Vous etes trop fatigué pour réutiliser ceci maintenant.");
			return;
		}


		final World world = player.getWorld();
		final GamePlayer gPlayer = HeroBattle.get().getGamePlayer(player);

		if(gPlayer == null || !gPlayer.isPlaying()) return;

		final Item apple = world.dropItem(player.getLocation().add(0, 1, 0), new ItemStack(Material.APPLE));
		apple.setVelocity(player.getLocation().getDirection().normalize().multiply(2));
		apple.setPickupDelay(32767); // Cannot be picked up


		new BukkitRunnable()
		{
			private int delay = 3 * 20 - 10;

			@Override
			public void run()
			{
				if(apple.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR || delay <= 0)
				{
					Location boomLocation = apple.getLocation();
					apple.remove();


					// Explosions
					final Location fwLocation = net.lnfinity.HeroBattle.utils.Utils.blockLocation(boomLocation).add(0, 1, 0);

					final Firework fw = boomLocation.getWorld().spawn(fwLocation, Firework.class);
					FireworkMeta fwm = fw.getFireworkMeta();
					FireworkEffect effect = FireworkEffect.builder()
							.withColor(Color.RED).with(FireworkEffect.Type.BALL_LARGE)
							.withFade(Color.RED.mixColors(Color.YELLOW)).build();
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


					// Sounds
					for(Player player : world.getPlayers())
					{
						player.playSound(boomLocation, Sound.EXPLODE, 1, 5.0f);
					}


					// Damages, motion, title
					for (Entity e : player.getNearbyEntities(10, 10, 10)) {
						if (e instanceof Player) {
							Player victim = (Player) e;

							GamePlayer gVictim = HeroBattle.get().getGamePlayer(victim);
							if(gVictim == null || !gVictim.isPlaying()) continue;

							Double distanceSquared = victim.getLocation().distanceSquared(player.getLocation());

							// Damages
							Integer damages = ((int) ((random.nextInt(DAMAGES_MAX - DAMAGES_MIN) + DAMAGES_MIN) * Math.max(0.7, 1 - (distanceSquared / 50))));
							gVictim.setPercentage(gVictim.getPercentage() + damages, gPlayer);

							// Motion
							if(distanceSquared < 68 && !gVictim.getPlayerUniqueID().equals(player.getUniqueId()))
							{
								player.setVelocity(player.getLocation().toVector().subtract(boomLocation.toVector()).normalize().multiply(distanceSquared / 38));
							}

							// Blindness (apple juice c:)
							if(random.nextDouble() <= BLINDNESS_PROBABILITY) {
								victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3*20, 0, true, false));
							}

							// Title
							Titles.sendTitle(victim, 0, 15, 3, ChatColor.RED + "POMME", "");
						}
					}


					cancel();
				}

				delay -= 2;
			}
		}.runTaskTimer(HeroBattle.get(), 10, 2);


		new ItemCooldown(HeroBattle.get(), player, this, COOLDOWN);
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		onRightClick(player, tool, event);
	}
}
