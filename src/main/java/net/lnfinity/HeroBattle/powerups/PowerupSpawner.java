package net.lnfinity.HeroBattle.powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

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
