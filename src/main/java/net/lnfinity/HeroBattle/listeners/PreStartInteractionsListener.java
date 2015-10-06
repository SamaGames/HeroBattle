package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.gui.ClassSelectorGui;
import net.lnfinity.HeroBattle.gui.core.Gui;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;


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
