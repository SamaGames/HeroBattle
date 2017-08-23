package net.lnfinity.HeroBattle.game;

import java.util.HashMap;
import java.util.Map;

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
public class Assist
{
	private Integer totalAssist = 0;
	private Long lastAssistTime = 0l;

	private Map<Long, Integer> assists = new HashMap<>();


	public Assist(Integer initialAssist)
	{
		addAssist(initialAssist);
	}


	/**
	 * Adds an assist.
	 *
	 * @param assist The damage added.
	 */
	public void addAssist(Integer assist)
	{
		this.totalAssist += assist;
		lastAssistTime = System.currentTimeMillis();

		assists.put(lastAssistTime, assist);
	}


	/**
	 * The global assistance
	 *
	 * @return All the damages made by this player.
	 */
	public Integer getTotalAssist()
	{
		return totalAssist;
	}

	/**
	 * The last time an assist has been done.
	 *
	 * @return The time (milli-timestamp).
	 */
	public Long getLastAssistTime()
	{
		return lastAssistTime;
	}

	/**
	 * Returns the damages made in the last `milliseconds` milliseconds.
	 *
	 * @param milliseconds The delay.
	 *
	 * @return The damages made in this interval.
	 */
	public Integer getRecentAssists(Long milliseconds)
	{
		Long minimalTime = System.currentTimeMillis() - milliseconds;

		Integer damages = 0;
		for (Map.Entry<Long, Integer> assist : assists.entrySet())
		{
			if (assist.getKey() >= minimalTime)
				damages += assist.getValue();
		}

		return damages;
	}
}
