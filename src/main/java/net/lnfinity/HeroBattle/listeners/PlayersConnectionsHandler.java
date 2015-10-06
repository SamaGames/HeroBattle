package net.lnfinity.HeroBattle.listeners;


import net.lnfinity.HeroBattle.HeroBattle;
import net.samagames.tools.chat.fanciful.FancyMessage;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * This listener watches players logs in and out, to display grouped join & quit messages, instead
 * of a flood of « player joined the game ». <p/> A join/quit message is displayed every
 * MESSAGES_INTERVAL ticks, if someone joined. <p/> This was before a listener and is similar to
 * them. R.I.P., beloved join listeners replaced by ugly methods.
 */
public class PlayersConnectionsHandler
{
	/**
	 * A join/quit message will be displayed every MESSAGES_INTERVAL ticks (if someone joined, of
	 * course).
	 */
	final private long MESSAGES_INTERVAL = 30l;
	/**
	 * Stores the new players who are connected but were not announced in a join message.
	 */
	final private List<String> playersConnectedNotAnnounced = new CopyOnWriteArrayList<>();
	/**
	 * Store the players who disconnected but were not announced in a quit message.
	 */
	final private List<String> playersDisconnectedNotAnnounced = new CopyOnWriteArrayList<>();
	private HeroBattle p;


	public PlayersConnectionsHandler(HeroBattle plugin)
	{
		p = plugin;

		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if (!isEnabled()) cancel();

				if (playersConnectedNotAnnounced.size() != 0)
				{
					String playersCount = " " + ChatColor.DARK_GRAY + "[" + ChatColor.RED + p.getGame().getConnectedPlayers() + ChatColor.DARK_GRAY + "/" + ChatColor.RED + p.getGame().getMaxPlayers() + ChatColor.DARK_GRAY + "]";

					if (p.getGame().getConnectedPlayers() > p.getGame().getMaxPlayers())
					{
						playersCount = playersCount + ChatColor.GREEN + " [Slots VIP]";
					}

					writeJoinOrQuitMessage(playersConnectedNotAnnounced, "a rejoint la partie !" + playersCount, "ont rejoint la partie !" + playersCount);

					playersConnectedNotAnnounced.clear();
				}

				if (playersDisconnectedNotAnnounced.size() != 0)
				{
					writeJoinOrQuitMessage(playersDisconnectedNotAnnounced, "a quitté la partie.", "ont quitté la partie.");

					playersDisconnectedNotAnnounced.clear();
				}
			}
		}.runTaskTimer(p, 5l, MESSAGES_INTERVAL);
	}


	/**
	 * Returns true if this join display system is enabled.
	 *
	 * @return true if enabled.
	 */
	private boolean isEnabled()
	{
		return !p.getGame().isGameStarted();
	}


	private String getDisplayName(Player player)
	{
		UUID id = player.getUniqueId();

		if (id.equals(UUID.fromString("da04cd54-c6c7-4672-97c5-85663f5bccf6"))
				|| id.equals(UUID.fromString("9cc7b403-3ce8-47d7-9d95-eb2a03dd78b4"))
				|| id.equals(UUID.fromString("0dd34bda-c13b-473b-a887-368027ca05ca")))
		{

			return ChatColor.DARK_GREEN + "" + ChatColor.MAGIC + "||" + ChatColor.RESET
					+ ChatColor.GREEN + ChatColor.BOLD + " " + player.getName() + " "
					+ ChatColor.GREEN + "" + ChatColor.MAGIC + "||" + ChatColor.RESET;
		}

		else
		{
			return ChatColor.GOLD + player.getName();
		}
	}

	/**
	 * Writes a join or quit message for multiple players. <p/> The message will be displayed as
	 * follows: - Player [endOfTheMessageSolo] - Player1 et Player2 [endOfTheMessageMultiple] -
	 * Player1, Player2 et x autres [endOfTheMessageMultiple]
	 *
	 * @param players                 The players who joined or quit.
	 * @param endOfTheMessageSolo     The end of the message, if only one player joined.
	 * @param endOfTheMessageMultiple The end of the message, if more than one player joined.
	 */
	private void writeJoinOrQuitMessage(List<String> players, String endOfTheMessageSolo, String endOfTheMessageMultiple)
	{

		if (players.size() == 0) return;


		if (players.size() <= 2)
		{
			Bukkit.broadcastMessage(HeroBattle.GAME_TAG + StringUtils.join(players, ChatColor.YELLOW + " et ") + ChatColor.YELLOW + " " + (players.size() == 1 ? endOfTheMessageSolo : endOfTheMessageMultiple));
		}

		else
		{
			ChatColor colorFirstPlayer = null;
			String lastColorsFirstPlayer = ChatColor.getLastColors(players.get(0)).replace("" + ChatColor.COLOR_CHAR, "");
			for (Character colorChar : lastColorsFirstPlayer.toCharArray())
			{
				ChatColor color = ChatColor.getByChar(colorChar);
				if (color != null && color.isColor())
				{
					colorFirstPlayer = color;
				}
			}
			if (colorFirstPlayer == null) colorFirstPlayer = ChatColor.YELLOW;

			FancyMessage message = new FancyMessage().text(HeroBattle.GAME_TAG)
					.then().text(players.get(0)).color(colorFirstPlayer)
					.then().text(" et ").color(ChatColor.YELLOW)
					.then().text((players.size() - 1) + " autres")
					.tooltip(players.subList(1, players.size())).color(ChatColor.GOLD)
					.then().text(" " + endOfTheMessageMultiple).color(ChatColor.YELLOW);

			message.send(Bukkit.getOnlinePlayers());
			p.getServer().getConsoleSender().sendMessage(message.toOldMessageFormat());
		}
	}


	public void registerPlayerJoin(Player player)
	{
		if (!isEnabled()) return;

		if (player != null)
		{
			playersConnectedNotAnnounced.add(getDisplayName(player));
		}
	}

	public void registerPlayerQuit(Player player)
	{
		if (!isEnabled()) return;

		if (player != null)
		{
			playersDisconnectedNotAnnounced.add(getDisplayName(player));
		}
	}
}
