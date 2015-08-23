package net.lnfinity.HeroBattle.listeners.commands;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.game.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;


public class CommandListener implements CommandExecutor
{

	private HeroBattle p;

	public CommandListener(HeroBattle plugin)
	{
		p = plugin;
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String alias, String[] args)
	{

		if (sender instanceof Player)
		{
			UUID id = ((Player) sender).getUniqueId();

			if (!id.equals(UUID.fromString("da04cd54-c6c7-4672-97c5-85663f5bccf6"))
					&& !id.equals(UUID.fromString("9cc7b403-3ce8-47d7-9d95-eb2a03dd78b4"))
					&& !sender.isOp())
			{
				return false;
			}
		}


		if (cmd.getName().equalsIgnoreCase("start"))
		{

			if (p.getPlayerCount() >= 2 || !MasterBundle.isDbEnabled)
			{

				p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Le jeu a été démarré manuellement.");
				p.getTimer().cancelTimer();
				p.getGame().start();

			}
			else
			{

				sender.sendMessage(ChatColor.RED + "Vous devez être au moins deux joueurs !");
			}

		}
		else if (cmd.getName().equalsIgnoreCase("forcestop"))
		{

			p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.RED + "Le jeu a été interrompu de force.");

			p.getGame().onPlayerWin(null);

		}
		else if (cmd.getName().equalsIgnoreCase("powerup"))
		{

			if (p.getGame().getStatus() == Status.InGame)
			{
				p.getPowerupManager().spawnRandomPowerup();
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Impossible de faire apparaître un powerup si le jeu n'est pas démarré.");
			}

		}
		else if (cmd.getName().equalsIgnoreCase("elo"))
		{

			if (args == null || args.length < 2)
			{
				sender.sendMessage(ChatColor.RED + "/elo <joueur> <elo>");
			}
			else if (args.length >= 2)
			{
				final OfflinePlayer player = p.getServer().getOfflinePlayer(args[0]);
				final HeroBattlePlayer heroBattlePlayer = player instanceof Player ? p.getGamePlayer((Player) player) : null;

				if (player != null)
				{
					try
					{
						final int val = Integer.parseInt(args[1]);
						if (val >= 1000 && val <= 10000)
						{
							p.getServer().getScheduler().runTaskAsynchronously(p, () -> {
								StatsApi.increaseStat(player.getUniqueId(), HeroBattle.GAME_NAME_WHITE, "elo", val - StatsApi.getPlayerStat(player.getUniqueId(), HeroBattle.GAME_NAME_WHITE, "elo"));
								if (heroBattlePlayer != null)
								{
									heroBattlePlayer.setElo(val - heroBattlePlayer.getElo());
									((CommandSender) player).sendMessage(ChatColor.GREEN + "Votre " + ChatColor.DARK_GREEN + "ELO" + ChatColor.GREEN + " a été mis à " + ChatColor.DARK_GREEN + val);
									((CommandSender) player).sendMessage(ChatColor.GOLD + "Merci de rejoindre à nouveau la partie pour que les changements visuels soient appliqués.");
								}
								sender.sendMessage(ChatColor.GREEN + "L'" + ChatColor.DARK_GREEN + "ELO" + ChatColor.GREEN + " du joueur " + ChatColor.DARK_GREEN + player.getName() + ChatColor.GREEN + " a été mis à " + ChatColor.DARK_GREEN + val);
							});
						}
						else
						{
							sender.sendMessage(ChatColor.RED + "L'Elo doit être compris entre 1000 et 10000.");
						}

					}
					catch (Exception ex)
					{
						sender.sendMessage(ChatColor.RED + "La valeur de l'Elo indiquée n'est pas correcte.");
					}
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "Le joueur n'est pas connecté/introuvable.");
				}
			}
		}
		else return false;


		return true;
	}
}
