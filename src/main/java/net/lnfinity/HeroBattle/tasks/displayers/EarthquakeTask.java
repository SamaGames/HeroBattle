package net.lnfinity.HeroBattle.tasks.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tasks.Task;
import net.lnfinity.HeroBattle.utils.Utils;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

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
public class EarthquakeTask extends Task
{

	private final int MIN_DAMAGES;
	private final int MAX_DAMAGES;

	public EarthquakeTask(HeroBattle p, Player player, int min, int max)
	{
		super(p, player);
		MIN_DAMAGES = min;
		MAX_DAMAGES = max;
	}

	@Override
	public void playTask()
	{
		player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_LARGE, 1);

		for (int i = 0; i <= 20; i++)
		{
			player.getWorld().playEffect(player.getLocation(), Effect.TILE_BREAK, 1, 100);
		}

		player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1, 1);

		for (Entity e : player.getNearbyEntities(4, 4, 4))
		{
			if (e instanceof Player)
			{
				Player damaged = (Player) e;
				p.getGamePlayer(damaged).setPercentage(p.getGamePlayer(damaged).getPercentage() + Utils.randomNumber(MIN_DAMAGES, MAX_DAMAGES), p.getGamePlayer(player));
				damaged.damage(0);
			}
		}
	}

}
