package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.gui.*;
import net.lnfinity.HeroBattle.gui.core.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;


public class PreStartInteractionsListener implements Listener
{
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		if (e.hasItem())
		{
			Status gameStatus = HeroBattle.get().getGame().getStatus();

			if(gameStatus == Status.Available || gameStatus == Status.Starting)
			{
				if (e.getItem().getType() == Material.NETHER_STAR)
				{
					Gui.open(e.getPlayer(), new ClassSelectorGui());
				}
				else if (e.getItem().getType() == Material.BOOK)
				{
					HeroBattle.get().getTutorialDisplayer().start(e.getPlayer().getUniqueId());
				}
			}
		}
	}
}
