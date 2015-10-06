package net.lnfinity.HeroBattle.game;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.samagames.api.games.Status;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class AntiCamping implements Runnable
{
	private final HeroBattle p;

	private final Map<UUID, Location> players = new HashMap<>();
	private final Map<UUID, Integer> damagesGiven = new HashMap<>();
	private final Map<UUID, Integer> life = new HashMap<>();
	private final Map<UUID, Integer> combo = new HashMap<>();
	private final List<UUID> averted = new ArrayList<>();

	public AntiCamping(HeroBattle plugin)
	{
		p = plugin;
	}

	@Override
	public void run()
	{
		if (p.getGame().getStatus() != Status.IN_GAME) return;

		for (HeroBattlePlayer player : p.getGame().getInGamePlayers().values())
		{
			final UUID id = player.getUUID();

			if (players.get(id) == null)
			{
				// Not registered. Ok. Registering you.
				players.put(id, player.getPlayerIfOnline().getLocation());
				damagesGiven.put(id, (int) player.getPercentageInflicted());
				life.put(id, player.getPercentage());
				combo.put(id, 0);
			}
			else
			{
				// Hey ! We know you, lets check you're clean.
				if (players.get(id).distanceSquared(player.getPlayerIfOnline().getLocation()) <= 0.05 && damagesGiven.get(id) == player.getPercentageInflicted() && life.get(id) == player.getPercentage())
				{
					// You're potentially breaking the rule, we'll add a flag to you.
					combo.put(id, combo.get(id) + 1);
				}
				else
				{
					combo.put(id, 0);
					damagesGiven.put(id, (int) player.getPercentageInflicted());
					life.put(id, player.getPercentage());
				}

				if (combo.get(id) >= 5 && !averted.contains(id))
				{
					// First time cheating ? Humm... get that last warning
					player.getPlayerIfOnline().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Nous avons détecté que vous campiez. Merci de jouer plus activement sous peine d'être puni !");

					// You're flagged for the rest of the game :>
					averted.add(id);
					combo.put(id, 1);
				}

				if (combo.get(id) >= 3 && averted.contains(id))
				{
					// Want to play that game with me ? Let's finish it then !
					int n = Utils.randomNumber(40, 80);
					player.basicDamage(n, null);
					player.getPlayerIfOnline().sendMessage(ChatColor.RED + "Vous avez été puni de " + ChatColor.DARK_RED + n + ChatColor.RED + " pourcents de dégâts pour avoir été inactif !");
					combo.put(id, 0);

					System.out.println("Player " + player.getPlayerIfOnline().getName() + " has been punished for camping !");
				}

				// Update you stats
				players.put(id, player.getPlayerIfOnline().getLocation());
			}
		}
	}
}
