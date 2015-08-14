/*
 * Copyright (C) 2015 Amaury Carrade
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.classes.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.gui.*;
import net.lnfinity.HeroBattle.gui.core.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;


public class ClassPreviewCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args)
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("Only as a player.");
			return true;
		}

		if(args == null || args.length == 0)
		{
			sender.sendMessage(ChatColor.RED + "/" + command.getName() + " <classe>");
			return true;
		}

		GamePlayer target = HeroBattle.getInstance().getGamePlayer(((Player) sender).getUniqueId());
		if(target == null)
		{
			sender.sendMessage(ChatColor.RED + "Vous n'êtes pas un joueur !");
			return true;
		}

		PlayerClass playerClass = HeroBattle.getInstance().getClassManager().getAnyClassByFriendlyName(args[0], target);
		if(playerClass != null)
		{
			Gui.open(((Player) sender), new ClassDetailsGui(playerClass));
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "Classe inconnue. " + ChatColor.GRAY + "Vérifiez l'orthographe ?");
		}

		return true;
	}
}
