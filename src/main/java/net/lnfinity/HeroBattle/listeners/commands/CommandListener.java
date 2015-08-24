package net.lnfinity.HeroBattle.listeners.commands;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import net.md_5.bungee.api.ChatColor;
import net.samagames.api.SamaGamesAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;


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


		if (cmd.getName().equalsIgnoreCase("forcestop"))
		{

			p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.RED + "Le jeu a été interrompu de force.");

			p.getGame().onPlayerWin(null);

		}
		else if (cmd.getName().equalsIgnoreCase("powerup"))
		{

			if (p.getGame().isGameStarted())
			{
				p.getPowerupManager().spawnRandomPowerup();
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Impossible de faire apparaître un powerup lorsque le jeu n'est pas démarré.");
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

				p.getServer().getScheduler().runTaskAsynchronously(p, () ->
				{
					final int newELO;

					try
					{
						newELO = Integer.parseInt(args[1]);

						if(newELO < 1000 || newELO > 10000)
						{
							throw new NumberFormatException();
						}
					}
					catch(NumberFormatException e)
					{
						sender.sendMessage(ChatColor.RED + "La valeur de l'Elo indiquée n'est pas correcte.");
						return;
					}


					final String playerName = args[0];
					final UUID playerID     = SamaGamesAPI.get().getUUIDTranslator().getUUID(playerName);

					final HeroBattlePlayer hbPlayer = p.getGame().getPlayer(playerID);


					SamaGamesAPI.get().getStatsManager(HeroBattle.get().getGame().getGameCodeName())
							.setValue(playerID, "elo", newELO);

					sender.sendMessage(ChatColor.GREEN + "L'" + ChatColor.DARK_GREEN + "ELO" + ChatColor.GREEN + " du joueur " + ChatColor.DARK_GREEN + playerName + ChatColor.GREEN + " a été mis à " + ChatColor.DARK_GREEN + newELO);


					if (hbPlayer != null)
					{
						hbPlayer.setElo(newELO);

						Player player = hbPlayer.getPlayerIfOnline();
						if(player != null)
						{
							player.sendMessage(ChatColor.GREEN + "Votre " + ChatColor.DARK_GREEN + "ELO" + ChatColor.GREEN + " a été mis à " + ChatColor.DARK_GREEN + newELO);
						}
					}
				});
			}
		}

		else return false;


		return true;
	}
}
