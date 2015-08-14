package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.gui.*;
import net.lnfinity.HeroBattle.gui.core.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;


public class ClassSelectorListener implements Listener
{
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		if (e.hasItem())
		{
			if (e.getItem().getType() == Material.NETHER_STAR)
			{
				Gui.open(e.getPlayer(), new ClassSelectorGui());
			}
			else if (e.getItem().getType() == Material.BOOK)
			{
				HeroBattle.getInstance().getTutorialDisplayer().start(e.getPlayer().getUniqueId());
			}
		}
	}
}
