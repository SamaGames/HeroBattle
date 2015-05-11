package net.lnfinity.HeroBattle.listeners;


import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.utils.Utils;
import net.samagames.gameapi.events.FinishJoinPlayerEvent;
import net.samagames.gameapi.json.Status;
import net.samagames.utils.FancyMessage;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This listener watches players logs in and out, to display grouped join & quit messages,
 * instead of a flood of « player joined the game ».
 *
 * A join/quit message is displayed every MESSAGES_INTERVAL ticks, if someone joined.
 */
public class PlayersConnectionsListener implements Listener {

	private HeroBattle p;

	/**
	 * A join/quit message will be displayed every MESSAGES_INTERVAL ticks (if someone joined, of course).
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



	public PlayersConnectionsListener(HeroBattle plugin) {
		p = plugin;

		p.getServer().getScheduler().runTaskTimer(p, new Runnable() {
			@Override
			public void run() {
				if(playersConnectedNotAnnounced.size() != 0) {
					String playersCount = " " + ChatColor.DARK_GRAY + "[" + ChatColor.RED + p.getGame().countGamePlayers() + ChatColor.DARK_GRAY + "/" + ChatColor.RED + p.getGame().getMaxPlayers() + ChatColor.DARK_GRAY + "]";

					if(p.getGame().countGamePlayers() > p.getGame().getMaxPlayers()) {
						playersCount = playersCount + ChatColor.GREEN + " [Slots VIP]";
					}

					writeJoinOrQuitMessage(playersConnectedNotAnnounced, "a rejoint la partie !" + playersCount, "ont rejoint la partie !" + playersCount);

					playersConnectedNotAnnounced.clear();
				}

				if(playersDisconnectedNotAnnounced.size() != 0) {
					writeJoinOrQuitMessage(playersDisconnectedNotAnnounced, "a quitté la partie.", "ont quitté la partie.");

					playersDisconnectedNotAnnounced.clear();
				}
			}
		}, 5l, MESSAGES_INTERVAL);
	}


	/**
	 * Returns true if this join display system is enabled.
	 *
	 * @return true if enabled.
	 */
	private boolean isEnabled() {
		return p.getGame().getStatus() == Status.Available
				|| p.getGame().getStatus() == Status.Starting
				|| p.getGame().getStatus() == Status.PreStarting;
	}


	private String getDisplayName(Player player) {
		if (player.getName().equals("6infinity8") || player.getName().equals("AmauryPi")) {
			return ChatColor.GREEN + "" + ChatColor.MAGIC + "||" + ChatColor.RESET
					+ Utils.getPlayerColor(player) + ChatColor.BOLD + " " + player.getName() + " "
					+ ChatColor.GREEN + "" + ChatColor.MAGIC + "||" + ChatColor.RESET;
		}
		else {
			return Utils.getPlayerColor(player) + player.getName();
		}
	}

	/**
	 * Writes a join or quit message for multiple players.
	 *
	 * The message will be displayed as follows:
	 *  - Player [endOfTheMessageSolo]
	 *  - Player1 et Player2 [endOfTheMessageMultiple]
	 *  - Player1, Player2 et x autres [endOfTheMessageMultiple]
	 *
	 * @param players The players who joined or quit.
	 * @param endOfTheMessageSolo The end of the message, if only one player joined.
	 * @param endOfTheMessageMultiple The end of the message, if more than one player joined.
	 */
	private void writeJoinOrQuitMessage(List<String> players, String endOfTheMessageSolo, String endOfTheMessageMultiple) {

		if(players.size() == 0) return;


		if(players.size() <= 2) {
			Bukkit.broadcastMessage(HeroBattle.GAME_TAG + StringUtils.join(players, ChatColor.YELLOW + " et ") + ChatColor.YELLOW + " " + (players.size() == 1 ? endOfTheMessageSolo : endOfTheMessageMultiple));
		}

		else {
			ChatColor colorFirstPlayer = null;
			String lastColorsFirstPlayer = ChatColor.getLastColors(players.get(0)).replace("" + ChatColor.COLOR_CHAR, "");
			for(Character colorChar : lastColorsFirstPlayer.toCharArray()) {
				ChatColor color = ChatColor.getByChar(colorChar);
				if(color != null && color.isColor()) {
					colorFirstPlayer = color;
				}
			}
			if(colorFirstPlayer == null) colorFirstPlayer = ChatColor.YELLOW;

			FancyMessage message = new FancyMessage().text(HeroBattle.GAME_TAG)
					.then().text(players.get(0)).color(colorFirstPlayer)
					.then().text(" et ").color(ChatColor.YELLOW)
					.then().text((players.size() - 1) + " autres")
					       .tooltip(StringUtils.join(players.subList(1, players.size()), "\n")).color(ChatColor.GOLD)
					.then().text(" " + endOfTheMessageMultiple).color(ChatColor.YELLOW);

			System.out.println(message.toJSONString());

			message.send(((Iterable<Player>) Bukkit.getOnlinePlayers()));
			p.getServer().getConsoleSender().sendMessage(message.toOldMessageFormat());
		}
	}


	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(FinishJoinPlayerEvent ev) {
		if(!isEnabled()) return;

		Player player = Bukkit.getPlayer(ev.getPlayer());

		if(player != null) {
			playersConnectedNotAnnounced.add(getDisplayName(player));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent ev) {
		if(!isEnabled()) return;

		ev.setQuitMessage(null);
		playersDisconnectedNotAnnounced.add(getDisplayName(ev.getPlayer()));
	}
}
