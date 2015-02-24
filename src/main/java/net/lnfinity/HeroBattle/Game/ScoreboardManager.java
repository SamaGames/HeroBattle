package net.lnfinity.HeroBattle.Game;

import net.lnfinity.HeroBattle.HeroBattle;

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
		percentageSidebar.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Hero" + ChatColor.LIGHT_PURPLE
				+ "" + ChatColor.BOLD + "Battle");
	}

	/**
	 * To be called when the game starts.
	 */
	public void init() {
		for (Player player : p.getServer().getOnlinePlayers()) {
			update(player);
		}

		percentageBelowName.setDisplaySlot(DisplaySlot.BELOW_NAME);
		percentageSidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
	}

	/**
	 * Updates the scoreboards for the given player.
	 * 
	 * @param player
	 *            The player.
	 */
	public void update(Player player) {
		int percentage = p.getGamePlayer(player).getPercentage();
		int lives = p.getGamePlayer(player).getLives();
		int maxLives = 3;
		if (p.getGamePlayer(player).getPlayerClass() != null) {
			maxLives = p.getGamePlayer(player).getPlayerClass().getLives();
		}
		if (p.getGamePlayer(player).isPlaying()) {
			board.resetScores(heartsToString(lives + 1, maxLives) + ChatColor.WHITE + " " + player.getName());
			percentageSidebar.getScore(heartsToString(lives, maxLives) + ChatColor.WHITE + " " + player.getName())
					.setScore(percentage);
		} else {
			board.resetScores(heartsToString(lives, maxLives) + ChatColor.WHITE + " " + player.getName());
		}

		percentageBelowName.getScore(player.getName()).setScore(percentage);
	}

	private String heartsToString(int hearts, int maxHearts) {
		String str = ChatColor.GRAY + "";
		for (int i = maxHearts; i >= 1; i--) {
			if (i > hearts) {
				str = str + "❤";
			} else {
				str = str + ChatColor.RED + "❤";
			}
		}

		return str;
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
