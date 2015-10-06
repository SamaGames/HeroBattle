package net.lnfinity.HeroBattle.utils;

import net.lnfinity.HeroBattle.HeroBattle;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;


public class WinnerFirework
{
	private int task;
	private int i = 0;


	public WinnerFirework(final HeroBattle plugin, final int loops, final Player player)
	{
		task = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
			generateRandomFirework(player.getLocation(), 5, 5);
			if (i >= loops)
			{
				plugin.getServer().getScheduler().cancelTask(task);
			}
			i++;
		}, 20L, 10L).getTaskId();
	}


	/**
	 * Spawns a random firework at the given location.
	 *
	 * Please note: because the power of a firework is an integer, the min/max heights are with a
	 * precision of ±5 blocks.
	 *
	 * @param location  The location where the firework will be spawned.
	 * @param heightMin The minimal height of the explosion.
	 * @param heightMax The maximal height of the explosion.
	 *
	 * @return The random firework generated.
	 */
	public static Firework generateRandomFirework(Location location, int heightMin, int heightMax)
	{
		Random rand = new Random();

		Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
		FireworkMeta meta = firework.getFireworkMeta();

		int effectsCount = rand.nextInt(3) + 1;

		for (int i = 0; i < effectsCount; i++)
		{
			meta.addEffect(generateRandomFireworkEffect());
		}

		// One level of power is half a second of flight time.
		// In half a second, a firework fly ~5 blocks.
		// So, one level of power = ~5 blocks.
		meta.setPower((int) Math.min(Math.floor((heightMin / 5) + rand.nextInt(heightMax / 5)), 128D));

		firework.setFireworkMeta(meta);

		return firework;
	}

	/**
	 * Generates a random firework effect.
	 *
	 * @return The firework effect.
	 */
	private static FireworkEffect generateRandomFireworkEffect()
	{
		Random rand = new Random();
		Builder fireworkBuilder = FireworkEffect.builder();

		int colorCount = rand.nextInt(3) + 1;
		int trailCount = rand.nextInt(3) + 1;

		fireworkBuilder.flicker(rand.nextInt(3) == 1);
		fireworkBuilder.trail(rand.nextInt(3) == 1);

		for (int i = 0; i < colorCount; i++)
		{
			fireworkBuilder.withColor(generateRandomColor());
		}

		for (int i = 0; i < trailCount; i++)
		{
			fireworkBuilder.withFade(generateRandomColor());
		}

		// Random shape
		FireworkEffect.Type[] types = FireworkEffect.Type.values();
		fireworkBuilder.with(types[rand.nextInt(types.length)]);

		return fireworkBuilder.build();
	}

	/**
	 * Generates a random color.
	 *
	 * @return The color.
	 */
	private static Color generateRandomColor()
	{
		Random rand = new Random();
		return Color.fromBGR(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
	}

}
