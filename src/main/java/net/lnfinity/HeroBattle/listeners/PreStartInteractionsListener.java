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
