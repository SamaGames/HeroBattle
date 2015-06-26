package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.displayers.DewoitineClass;
import net.lnfinity.HeroBattle.classes.displayers.DewoitineD550Class;
import net.lnfinity.HeroBattle.classes.displayers.MaiteClass;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.md_5.bungee.api.ChatColor;
import net.samagames.gameapi.json.Status;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassSelectionCommand implements CommandExecutor {

	private HeroBattle p;

	public ClassSelectionCommand(HeroBattle plugin) {
		p = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Only as a player.");
			return true;
		}

		if(args == null || args.length == 0) {
			sender.sendMessage(ChatColor.RED + "/classe <classe>");
		}

		else if(args.length == 1) {
			Player player = (Player) sender;

			if(p.getGame().getStatus() == Status.Available || p.getGame().getStatus() == Status.PreStarting || p.getGame().getStatus() == Status.Starting) {
				GamePlayer gamePlayer = p.getGamePlayer(player);

				if(args[0].equalsIgnoreCase("ArsenalVG50")) {
					if(ClassSelectorListener.getDewotineTries().containsKey(player.getUniqueId())) {
						if(ClassSelectorListener.getDewotineTries().get(player.getUniqueId()) == 6) {
							DewoitineClass theClass = new DewoitineClass(p, 0, 0, 0);
							gamePlayer.setPlayerClass(theClass);
							player.sendMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Vous avez choisi la classe "
									+ ChatColor.DARK_GREEN + theClass.getName() + ChatColor.GREEN + " !");
							return true;
						}
					}
				}

				else if(args[0].equalsIgnoreCase("ArsenalVG39")) {
					if(ClassSelectorListener.getDewotineTries().containsKey(player.getUniqueId())) {
						if(ClassSelectorListener.getDewotineTries().get(player.getUniqueId()) == 6) {
							DewoitineClass theClass = new DewoitineD550Class(p, 0, 0, 0);
							gamePlayer.setPlayerClass(theClass);
							player.sendMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Vous avez choisi la classe "
									+ ChatColor.DARK_GREEN + theClass.getName() + ChatColor.GREEN + " !");
							return true;
						}
					}
				}

				for(PlayerClass theClass : gamePlayer.getAvaibleClasses()) {
					if(args[0].equalsIgnoreCase(theClass.getType().getId())) {
						gamePlayer.setPlayerClass(theClass);
						player.sendMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Vous avez choisi la classe "
								+ ChatColor.DARK_GREEN + theClass.getName() + ChatColor.GREEN + " !");
						return true;
					}
				}

				player.sendMessage(ChatColor.RED + "Vous ne possédez pas cette classe, ou elle n'existe pas !");
			}

			else {
				player.sendMessage(ChatColor.RED + "Le jeu est déjà commencé !");
			}
		}

		else {
			if(!sender.isOp()) {
				sender.sendMessage(ChatColor.RED + "Lolnope.");
				return true;
			}

			GamePlayer target = p.getGamePlayer(p.getServer().getPlayer(args[1]));
			if(target == null) {
				sender.sendMessage(ChatColor.RED + "Target lost.");
				return true;
			}

			switch(args[0].toLowerCase()) {
				case "maite":
				case "maïte":
				case "maité":
				case "maïté":
					target.setPlayerClass(new MaiteClass(p), true);
					break;

				case "dewoitine":
					target.setPlayerClass(new DewoitineClass(p, 0, 0, 0), true);
					break;

				case "dewoitined550":
					target.setPlayerClass(new DewoitineD550Class(p, 0, 0, 0), true);
					break;

                case "random":
                    target.setPlayerClass(null, true);

				default:
					PlayerClass playerClass = p.getClassManager().getClassFromName(target, args[0]);
					if(playerClass != null) {
						target.setPlayerClass(playerClass, true);
					}
			}

			sender.sendMessage(ChatColor.GREEN + "Classe modifiée (normalement, cf. tab).");

		}

		return true;
	}

}
