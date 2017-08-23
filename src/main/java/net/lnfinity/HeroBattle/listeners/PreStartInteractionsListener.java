package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.gui.ClassSelectorGui;
import net.lnfinity.HeroBattle.gui.core.Gui;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

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
public class PreStartInteractionsListener implements Listener
{
	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent ev)
	{
		if (ev.hasItem() && !HeroBattle.get().getGame().isGameStarted())
		{
			if (ev.getItem().getType() == Material.NETHER_STAR)
			{
				Gui.open(ev.getPlayer(), new ClassSelectorGui());
			}
			else if (ev.getItem().getType() == Material.BOOK)
			{
				HeroBattle.get().getTutorialDisplayer().start(ev.getPlayer().getUniqueId());
			}
		}
	}
}
