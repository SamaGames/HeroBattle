package net.lnfinity.HeroBattle.listeners;

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

			if (HeroBattle.getInstance().getGame().getStatus() == Status.Available || HeroBattle.getInstance().getGame().getStatus() == Status.PreStarting || HeroBattle.getInstance().getGame().getStatus() == Status.Starting)
			{
				GamePlayer gamePlayer = HeroBattle.getInstance().getGamePlayer(player);

				if (args[0].equalsIgnoreCase("ArsenalVG50"))
				{
					if (HeroBattle.getInstance().getClassManager().getDewoitineUnlocks().contains(player.getUniqueId()))
					{
						HeroBattle.getInstance().getClassManager().setPlayerClass(player, new DewoitineClass(HeroBattle.getInstance(), 0, 0, 0), true);
						return true;
					}
				}

				else if (args[0].equalsIgnoreCase("ArsenalVG39"))
				{
					if (HeroBattle.getInstance().getClassManager().getDewoitineUnlocks().contains(player.getUniqueId()))
					{
						HeroBattle.getInstance().getClassManager().setPlayerClass(player, new DewoitineD550Class(HeroBattle.getInstance(), 0, 0, 0), true);
						return true;
					}
				}

				else if (args[0].equalsIgnoreCase("PokemonJaune"))
				{
					if (HeroBattle.getInstance().getClassManager().getPikachuUnlocks().contains(player.getUniqueId()))
					{
						HeroBattle.getInstance().getClassManager().setPlayerClass(player, new PikachuClass(), true);
						return true;
					}
				}

				else if (args[0].equalsIgnoreCase("Pommeeeh"))
				{
					HeroBattle.getInstance().getClassManager().getPommeUnlocks().add(player.getUniqueId());
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

			GamePlayer target = HeroBattle.getInstance().getGamePlayer(HeroBattle.getInstance().getServer().getPlayer(args[1]));
			if (target == null)
			{
				sender.sendMessage(ChatColor.RED + "Target lost.");
				return true;
			}

			PlayerClass pClass = HeroBattle.getInstance().getClassManager().getAnyClassByFriendlyName(args[0], target);
			if (pClass != null)
			{
				HeroBattle.getInstance().getClassManager().setPlayerClass(Bukkit.getPlayer(target.getPlayerUniqueID()), pClass, true);
				sender.sendMessage(ChatColor.GREEN + "Classe modifiée (normalement, cf. tab).");
			}
		}

		return true;
	}
}
