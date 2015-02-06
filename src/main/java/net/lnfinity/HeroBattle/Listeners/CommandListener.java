package net.lnfinity.HeroBattle.Listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandListener implements CommandExecutor {

	private HeroBattle p;

	public CommandListener(HeroBattle plugin) {
		p = plugin;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg0.isOp()) {
		p.getGame().start();
		p.getServer().broadcastMessage(HeroBattle.NAME + ChatColor.GREEN + "Le jeu a été démarré manuellement.");
		return true;
		} else {
			return false;
		}
	}
}
