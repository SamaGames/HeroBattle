package net.lnfinity.HeroBattle.Listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {

	private HeroBattle p;

	public CommandListener(HeroBattle plugin) {
		p = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
		// Permissions, please ! ><
		if (command.equalsIgnoreCase("start")) {
			if (sender.isOp() || sender.getName().equals("6infinity8") || sender.getName().equals("AmauryPi")) {
				if (p.getPlayerCount() > 1) {
					p.getServer().broadcastMessage(
							HeroBattle.NAME + ChatColor.GREEN + "Le jeu a été démarré manuellement.");
					p.getTimer().cancelTimer();
					p.getGame().start();
				} else {
					sender.sendMessage(ChatColor.RED + "Vous devez être au moins deux joueurs !");
				}
				return true;
			} else {
				return false;
			}
		} else if (command.equalsIgnoreCase("forcestop")) {
			if (sender.isOp() || sender.getName().equals("6infinity8") || sender.getName().equals("AmauryPi")) {
				p.getServer().broadcastMessage(p.NAME + ChatColor.RED + "Le jeu a été interrompu de force.");
				Bukkit.getServer().getScheduler().runTaskLater(p, new Runnable() {
					@Override
					public void run() {
						for (Player player : p.getServer().getOnlinePlayers()) {
							player.kickPlayer("");
						}
					}
				}, 10 * 20L);

				Bukkit.getServer().getScheduler().runTaskLater(p, new Runnable() {
					@Override
					public void run() {
						Bukkit.shutdown();
					}
				}, 15 * 20L);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
