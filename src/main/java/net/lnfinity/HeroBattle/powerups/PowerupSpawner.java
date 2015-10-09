package net.lnfinity.HeroBattle.powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;


public class PowerupSpawner
{

	private final HeroBattle p;
	Random random = new Random();
	private BukkitTask task = null;

	public PowerupSpawner(final HeroBattle plugin)
	{
		p = plugin;
	}

	public boolean isEnabled()
	{
		return task != null;
	}

	public void stopTimer()
	{
        if(!isEnabled()) return;

		task.cancel();
		task = null;
	}

	public void startTimer()
	{
		if (isEnabled()) return;

		task = p.getServer().getScheduler().runTaskTimer(p, () ->
		{
			if (random.nextInt(PowerupManager.INVERSE_PROBABILITY_OF_SPAWN_PER_TICK) == 0)
			{
				p.getPowerupManager().spawnRandomPowerup();
			}
		}, 1L, 1L);
	}
}
