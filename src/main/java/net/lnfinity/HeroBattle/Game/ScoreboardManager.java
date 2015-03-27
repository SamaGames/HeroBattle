package net.lnfinity.HeroBattle.Game;

import net.lnfinity.HeroBattle.HeroBattle;

import net.lnfinity.HeroBattle.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager {

	HeroBattle p = null;
	Scoreboard board = null;
	Objective percentageBelowName = null;
	Objective percentageSidebar = null;

	public ScoreboardManager(HeroBattle plugin) {
		p = plugin;

		board = p.getServer().getScoreboardManager().getNewScoreboard();

		percentageBelowName = board.registerNewObjective("perct_below", "dummy");
		percentageBelowName.setDisplayName("%");

		percentageSidebar = board.registerNewObjective("perct_sidebar", "dummy");
		percentageSidebar.setDisplayName(HeroBattle.GAME_NAME_BICOLOR_BOLD + ChatColor.DARK_GRAY + " │ " + ChatColor.WHITE + p.getGameTimer().getFormattedTime());
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
	}

	public void refresh() {
		percentageSidebar.unregister();
		percentageSidebar = board.registerNewObjective("perct_sidebar", "dummy");
		percentageSidebar.setDisplayName(HeroBattle.GAME_NAME_BICOLOR_BOLD + ChatColor.WHITE + " | " + ChatColor.GRAY + p.getGameTimer().getFormattedTime());
		percentageSidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

		for (GamePlayer player : p.getGamePlayers().values()) {
			update(player);
		}
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
