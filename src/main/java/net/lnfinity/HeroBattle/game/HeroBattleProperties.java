/*
 * Copyright or © or Copr. AmauryCarrade (2015)
 * 
 * http://amaury.carrade.eu
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package net.lnfinity.HeroBattle.game;

import com.google.gson.JsonPrimitive;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.IGameProperties;


public class HeroBattleProperties
{
	private IGameProperties properties;

	// Rewards
	private final Integer coinsPerKill;
	private final Integer coinsPerAssist;
	private final Integer coinsPerVictory;
	private final Integer starsPerVictory;
	private final Integer coinsIfFirstRanked;
	private final Integer coinsIfSecondRanked;
	private final Integer coinsIfThirdRanked;

	public HeroBattleProperties()
	{
		this.properties = SamaGamesAPI.get().getGameManager().getGameProperties();


		/* **  Rewards  ** */

		coinsPerKill        = getInt("rewards.coins.per-kill", 5);
		coinsPerAssist      = getInt("rewards.coins.per-assist", 3);
		coinsPerVictory     = getInt("rewards.coins.per-victory", 20);
		starsPerVictory     = getInt("rewards.stars.per-victory", 1);

		coinsIfFirstRanked  = getInt("rewards.coins.if-first-ranked", 10);
		coinsIfSecondRanked = getInt("rewards.coins.if-second-ranked", 6);
		coinsIfThirdRanked  = getInt("rewards.coins.if-third-ranked", 4);
	}


	public Integer getInt(String key, Number defaultValue)
	{
		return properties.getOption(key, new JsonPrimitive(defaultValue)).getAsInt();
	}


	public Integer getCoinsPerKill()
	{
		return coinsPerKill;
	}

	public Integer getCoinsPerAssist()
	{
		return coinsPerAssist;
	}

	public Integer getCoinsPerVictory()
	{
		return coinsPerVictory;
	}

	public Integer getStarsPerVictory()
	{
		return starsPerVictory;
	}

	public Integer getCoinsIfFirstRanked()
	{
		return coinsIfFirstRanked;
	}

	public Integer getCoinsIfSecondRanked()
	{
		return coinsIfSecondRanked;
	}

	public Integer getCoinsIfThirdRanked()
	{
		return coinsIfThirdRanked;
	}
}
