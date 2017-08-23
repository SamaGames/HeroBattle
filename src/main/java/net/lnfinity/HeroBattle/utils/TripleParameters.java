package net.lnfinity.HeroBattle.utils;

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
public class TripleParameters
{
	private final int min;
	private final int max;
	private final int other;


	public TripleParameters(int min, int max)
	{
		this(min, max, 0);
	}

	public TripleParameters(int min, int max, int other)
	{
		this.min = min;
		this.max = max;
		this.other = other;
	}


	public int getMinDamages()
	{
		return min;
	}

	public int getMaxDamages()
	{
		return max;
	}

	public int getOtherParam()
	{
		return other;
	}
}
