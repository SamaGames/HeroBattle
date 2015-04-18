package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClass;
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
		if(args == null || args.length == 0) {
			sender.sendMessage(ChatColor.RED + "/classe <classe>");
		} else {
			Player player = p.getServer().getPlayer(sender.getName());
			if(player != null) {
				if(p.getGame().getStatus() == Status.Available || p.getGame().getStatus() == Status.PreStarting || p.getGame().getStatus() == Status.Starting) {
				GamePlayer gamePlayer = p.getGamePlayer(player);
				if(args[0].equalsIgnoreCase("maite") || args[0].equalsIgnoreCase("maité") || args[0].equalsIgnoreCase("maïté")) {
					PlayerClass theClass = new MaiteClass(p);
					gamePlayer.setPlayerClass(theClass);
					player.sendMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Vous avez choisi la classe "
							+ ChatColor.DARK_GREEN + theClass.getName() + ChatColor.GREEN + " !");
					return true;
				}
				for(PlayerClass theClass : gamePlayer.getAvaibleClasses()) {
					if(args[0].equalsIgnoreCase(theClass.getType().getId())) {
						gamePlayer.setPlayerClass(theClass);
						player.sendMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Vous avez choisi la classe "
								+ ChatColor.DARK_GREEN + theClass.getName() + ChatColor.GREEN + " !");
						return true;
					}
				}
				player.sendMessage(ChatColor.RED + "Vous ne possédez pas cette classe !");
			} else {
				player.sendMessage(ChatColor.RED + "Le jeu est déjà commencé !");
			}
		}
		}
		return true;
	}

}
