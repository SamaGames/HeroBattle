package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

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
public class PowerupsListener implements Listener
{

	private HeroBattle p;

	public PowerupsListener(HeroBattle plugin)
	{
		p = plugin;
	}

	@EventHandler
	public void onPowerupPickup(PlayerPickupItemEvent ev)
	{

		ev.setCancelled(true);

		HeroBattlePlayer gPlayer = p.getGamePlayer(ev.getPlayer());
		if (gPlayer != null && !gPlayer.isSpectator())
		{
			p.getPowerupManager().onPowerupPickup(ev.getItem(), ev.getPlayer());
		}

	}
}
