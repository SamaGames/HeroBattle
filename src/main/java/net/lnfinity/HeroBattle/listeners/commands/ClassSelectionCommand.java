package net.lnfinity.HeroBattle.listeners.commands;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.classes.*;
import net.lnfinity.HeroBattle.classes.displayers.eastereggs.*;
import net.lnfinity.HeroBattle.game.*;
import net.md_5.bungee.api.ChatColor;
import net.samagames.gameapi.json.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;


public class ClassSelectionCommand implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage("Only as a player.");
			return true;
		}

		if (args == null || args.length == 0)
		{
			sender.sendMessage(ChatColor.RED + "/" + command.getName() + " <classe>");
		}

		else if (args.length == 1)
		{
			Player player = (Player) sender;

			if (HeroBattle.get().getGame().getStatus() == Status.Available || HeroBattle.get().getGame().getStatus() == Status.PreStarting || HeroBattle.get().getGame().getStatus() == Status.Starting)
			{
				GamePlayer gamePlayer = HeroBattle.get().getGamePlayer(player);

				if (args[0].equalsIgnoreCase("ArsenalVG50"))
				{
					if (HeroBattle.get().getClassManager().getDewoitineUnlocks().contains(player.getUniqueId()))
					{
						HeroBattle.get().getClassManager().setPlayerClass(player, new DewoitineClass(HeroBattle.get(), 0, 0, 0), true);
						return true;
					}
				}

				else if (args[0].equalsIgnoreCase("ArsenalVG39"))
				{
					if (HeroBattle.get().getClassManager().getDewoitineUnlocks().contains(player.getUniqueId()))
					{
						HeroBattle.get().getClassManager().setPlayerClass(player, new DewoitineD550Class(HeroBattle.get(), 0, 0, 0), true);
						return true;
					}
				}

				else if (args[0].equalsIgnoreCase("PokemonJaune"))
				{
					if (HeroBattle.get().getClassManager().getPikachuUnlocks().contains(player.getUniqueId()))
					{
						HeroBattle.get().getClassManager().setPlayerClass(player, new PikachuClass(), true);
						return true;
					}
				}

				else if (args[0].equalsIgnoreCase("Pommeeeh"))
				{
					HeroBattle.get().getClassManager().getPommeUnlocks().add(player.getUniqueId());
					player.sendMessage(ChatColor.RED + "Vous ne possédez pas cette classe, ou elle n'existe pas !");

					return true;
				}

				for (PlayerClass theClass : gamePlayer.getAvaibleClasses())
				{
					if (args[0].equalsIgnoreCase(theClass.getType().getId()))
					{
						gamePlayer.setPlayerClass(theClass);
						player.sendMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Vous avez choisi la classe "
								+ ChatColor.DARK_GREEN + theClass.getName() + ChatColor.GREEN + " !");
						return true;
					}
				}

				player.sendMessage(ChatColor.RED + "Vous ne possédez pas cette classe, ou elle n'existe pas !");
			}

			else
			{
				player.sendMessage(ChatColor.RED + "Le jeu est déjà commencé !");
			}
		}

		else
		{
			if (!sender.isOp())
			{
				sender.sendMessage(ChatColor.RED + "Lolnope.");
				return true;
			}

			GamePlayer target = HeroBattle.get().getGamePlayer(HeroBattle.get().getServer().getPlayer(args[1]));
			if (target == null)
			{
				sender.sendMessage(ChatColor.RED + "Target lost.");
				return true;
			}

			PlayerClass pClass = HeroBattle.get().getClassManager().getAnyClassByFriendlyName(args[0], target);
			if (pClass != null)
			{
				HeroBattle.get().getClassManager().setPlayerClass(Bukkit.getPlayer(target.getPlayerUniqueID()), pClass, true);
				sender.sendMessage(ChatColor.GREEN + "Classe modifiée (normalement, cf. tab).");
			}
		}

		return true;
	}
}
