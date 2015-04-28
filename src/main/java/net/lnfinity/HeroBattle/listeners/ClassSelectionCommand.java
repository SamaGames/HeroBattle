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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ClassSelectionCommand implements CommandExecutor {

	private HeroBattle p;

	private int triesNeededForMaite = new Random().nextInt(7) + 3;
	private Map<UUID,Integer> triesForMaite = new HashMap<>();

	public ClassSelectionCommand(HeroBattle plugin) {
		p = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args == null || args.length == 0) {
			sender.sendMessage(ChatColor.RED + "/classe <classe>");
		}

		else {
			Player player = p.getServer().getPlayer(sender.getName());

			if(player != null) {

				if(p.getGame().getStatus() == Status.Available || p.getGame().getStatus() == Status.PreStarting || p.getGame().getStatus() == Status.Starting) {
					GamePlayer gamePlayer = p.getGamePlayer(player);

					if(args[0].equalsIgnoreCase("maite") || args[0].equalsIgnoreCase("maité") || args[0].equalsIgnoreCase("maïté")) {

						Integer triesForMaiteForThisPlayer = triesForMaite.get(player.getUniqueId());

						if(triesForMaiteForThisPlayer == null) {
							triesForMaite.put(player.getUniqueId(), 1);
							player.sendMessage(ChatColor.RED + "Vous ne possédez pas cette classe, ou elle n'existe pas !");
						}

						else if(triesForMaiteForThisPlayer < triesNeededForMaite) {
							triesForMaite.put(player.getUniqueId(), triesForMaiteForThisPlayer + 1);
							player.sendMessage(ChatColor.RED + "Vous ne possédez pas cette classe, ou elle n'existe pas !");
						}

						else {
							PlayerClass theClass = new MaiteClass(p);
							gamePlayer.setPlayerClass(theClass);

							player.sendMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Vous avez choisi la classe "
									+ ChatColor.DARK_GREEN + theClass.getName() + ChatColor.GREEN + " !");

							triesForMaite.remove(player.getUniqueId());
						}

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

					player.sendMessage(ChatColor.RED + "Vous ne possédez pas cette classe, ou elle n'existe pas !");
				}

				else {
					player.sendMessage(ChatColor.RED + "Le jeu est déjà commencé !");
				}
			}
		}
		return true;
	}

}
