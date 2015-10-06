/**
 * Plugin BelovedBlocks Copyright (C) 2014-2015 Amaury Carrade & Florian Cassayre <p/> This program
 * is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version. <p/> This program is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. <p/> You should
 * have received a copy of the GNU General Public License along with this program.  If not, see
 * [http://www.gnu.org/licenses/].
 */

package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import net.samagames.tools.Titles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;


public class PommEhxploseTool extends PlayerTool
{
	private final Random random = new Random();

	private final Integer DAMAGES_MIN = 25;
	private final Integer DAMAGES_MAX = 35;

	private final Double BLINDNESS_PROBABILITY = 0.06;

	private final Integer COOLDOWN = 15;


	public PommEhxploseTool(final HeroBattle plugin)
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
		return Utils.getToolDescription(
				ChatColor.GRAY + "Cherchant l'identité du mystérieux Meh, le Maître des Pommes s'acharne sur ses pommes et découvre la capacité incroyable de faire exploser ses pommes."
		);
	}

	@Override
	public ItemStack getItem()
	{
		final ItemStack apple = new ItemStack(Material.APPLE);
		ToolsUtils.resetTool(apple);

		return apple;
	}

	@Override
	public void onRightClick(final Player player, final ItemStack tool, final PlayerInteractEvent event)
	{
		if (!ToolsUtils.isToolAvailable(tool))
		{
			player.sendMessage(ChatColor.RED + "Plus de POMMES :c");
			return;
		}


		final World world = player.getWorld();
		final HeroBattlePlayer gPlayer = HeroBattle.get().getGamePlayer(player);

		if (gPlayer == null || gPlayer.isSpectator()) return;

		final Item apple = world.dropItem(player.getLocation().add(0, 1, 0), new ItemStack(Material.APPLE));
		apple.setVelocity(player.getLocation().getDirection().normalize().multiply(2));
		apple.setPickupDelay(32767); // Cannot be picked up


		new BukkitRunnable()
		{
			private int delay = 3 * 20 - 10;

			@Override
			public void run()
			{
				if (apple.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR || delay <= 0)
				{
					final Location boomLocation = apple.getLocation();
					apple.remove();


					// Explosions
					final Location fwLocation = net.lnfinity.HeroBattle.utils.Utils.blockLocation(boomLocation).add(0, 1, 0);

					final Firework fw = boomLocation.getWorld().spawn(fwLocation, Firework.class);
					final FireworkMeta fwm = fw.getFireworkMeta();
					final FireworkEffect effect = FireworkEffect.builder()
							.withColor(Color.RED).with(FireworkEffect.Type.BALL_LARGE)
							.withFade(Color.RED.mixColors(Color.YELLOW)).build();
					fwm.addEffects(effect);
					fwm.setPower(0);
					fw.setFireworkMeta(fwm);

					Bukkit.getScheduler().runTaskLater(p, fw::detonate, 1l);


					// Sounds
					for (final Player player : world.getPlayers())
					{
						player.playSound(boomLocation, Sound.EXPLODE, 1, 5.0f);
					}


					// Damages, motion, title
					for (final Entity e : player.getNearbyEntities(10, 10, 10))
					{
						if (e instanceof Player)
						{
							final Player victim = (Player) e;

							final HeroBattlePlayer gVictim = HeroBattle.get().getGamePlayer(victim);
							if (gVictim == null || gVictim.isSpectator()) continue;

							final Double distanceSquared = victim.getLocation().distanceSquared(player.getLocation());

							// Damages
							final Integer damages = ((int) ((random.nextInt(DAMAGES_MAX - DAMAGES_MIN) + DAMAGES_MIN) * Math.max(0.7, 1 - (distanceSquared / 50))));
							gVictim.setPercentage(gVictim.getPercentage() + damages, gPlayer);

							// Motion
							if (distanceSquared < 68 && !gVictim.getUUID().equals(player.getUniqueId()))
							{
								victim.setVelocity(victim.getVelocity().add(victim.getLocation().toVector().subtract(boomLocation.toVector()).normalize().multiply(distanceSquared / 38)));
							}

							// Blindness (apple juice c:)
							if (random.nextDouble() <= BLINDNESS_PROBABILITY)
							{
								victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 0, true, false));
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
}
