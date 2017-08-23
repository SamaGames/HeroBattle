package net.lnfinity.HeroBattle.tasks;

import net.lnfinity.HeroBattle.HeroBattle;
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
public abstract class Task
{

	protected Player player;
	protected HeroBattle p;

	public Task(HeroBattle p, Player player)
	{
		this.player = player;
		this.p = p;
	}

	public abstract void playTask();

}
