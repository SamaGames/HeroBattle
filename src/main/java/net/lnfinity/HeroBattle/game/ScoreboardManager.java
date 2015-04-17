package net.lnfinity.HeroBattle.game;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager {

	private HeroBattle p = null;
	private Scoreboard board = null;

	private Objective percentageBelowName = null;
	private Objective percentageSidebar = null;
	private Objective eloPlayerList = null;

	
	public ScoreboardManager(HeroBattle plugin) {
		p = plugin;

		board = p.getServer().getScoreboardManager().getNewScoreboard();

		percentageBelowName = board.registerNewObjective("perct_below", "dummy");
		percentageBelowName.setDisplayName("%");

		percentageSidebar = board.registerNewObjective("perct_sidebar", "dummy");
		percentageSidebar.setDisplayName("  " + HeroBattle.GAME_NAME_BICOLOR_BOLD + ChatColor.DARK_GRAY + " │ " + ChatColor.GRAY + p.getGameTimer().getFormattedTime());
		
		eloPlayerList = board.registerNewObjective("elo_playerlist", "dummy");
		eloPlayerList.setDisplaySlot(DisplaySlot.PLAYER_LIST);
	}

	/**
	 * To be called when the game starts.
	 */
	public void init() {
		for (GamePlayer player : p.getGamePlayers().values()) {
			update(player);
		}

		percentageBelowName.setDisplaySlot(DisplaySlot.BELOW_NAME);
		percentageSidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
		eloPlayerList.setDisplaySlot(DisplaySlot.PLAYER_LIST);
	}
	
	public void refreshTab() {
		for (GamePlayer player : p.getGamePlayers().values()) {
			Player realPlayer = p.getServer().getPlayer(player.getPlayerUniqueID());
			if(player != null && realPlayer != null) {
				eloPlayerList.getScore(realPlayer.getName()).setScore(player.getElo());
			}
		}
	}

	/**
	 * Updates the scoreboards for the given player.
	 *
	 * @param player The player.
	 */
	public void update(Player player) {
		update(p.getGamePlayer(player));
	}

	/**
	 * Updates the scoreboards for the given player.
	 * 
	 * @param player The player.
	 */
	public void update(GamePlayer player) {

		int percentage = player.getPercentage();

		if (player.isPlaying()) {
			percentageSidebar.getScore(Utils.heartsToString(player) + ChatColor.WHITE + " " + player.getPlayerName())
					.setScore(percentage);
		}
		else {
			percentageSidebar.getScore(Utils.heartsToString(player) + ChatColor.GRAY + " " + player.getPlayerName())
			.setScore(-1);
		}

		percentageBelowName.getScore(player.getPlayerName()).setScore(percentage);
		
		eloPlayerList.getScore(player.getPlayerName()).setScore(player.getElo());
	}

	public void refresh() {
		percentageSidebar.unregister();
		percentageSidebar = board.registerNewObjective("perct_sidebar", "dummy");
		percentageSidebar.setDisplayName("  " + HeroBattle.GAME_NAME_BICOLOR_BOLD + ChatColor.DARK_GRAY + " │ " + ChatColor.GRAY + p.getGameTimer().getFormattedTime());
		percentageSidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

		for (GamePlayer player : p.getGamePlayers().values()) {
			update(player);
		}
	}
	
	public void updateTimer() {
		percentageSidebar.setDisplayName("  " + HeroBattle.GAME_NAME_BICOLOR_BOLD + ChatColor.DARK_GRAY + " │ " + ChatColor.GRAY + p.getGameTimer().getFormattedTime());
	}

	/**
	 * Returns the scoreboard the players will have to use.
	 * 
	 * @return The scoreboard.
	 */
	public Scoreboard getScoreboard() {
		return board;
	}
}
