package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;
import net.samagames.gameapi.json.Status;
import net.zyuiop.MasterBundle.MasterBundle;
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
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

		// Permissions, please! ><
		if (!sender.isOp() && !sender.getName().equals("6infinity8") && !sender.getName().equals("AmauryPi")) {
			return false;
		}


		if (cmd.getName().equalsIgnoreCase("start")) {

			if (p.getPlayerCount() >= 2 || !MasterBundle.isDbEnabled) {

				p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Le jeu a été démarré manuellement.");
				p.getTimer().cancelTimer();
				p.getGame().start();

			} else {

				sender.sendMessage(ChatColor.RED + "Vous devez être au moins deux joueurs !");
			}

		} else if (cmd.getName().equalsIgnoreCase("forcestop")) {

			p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.RED + "Le jeu a été interrompu de force.");

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

		} else if (cmd.getName().equalsIgnoreCase("powerup")) {

			if(p.getGame().getStatus() == Status.InGame) {
				p.getPowerupManager().spawnRandomPowerup();
			}
			else {
				sender.sendMessage(ChatColor.RED + "Impossible de faire apparaître un powerup si le jeu n'est pas démarré.");
			}

		} else return false;



		return true;
	}
}
