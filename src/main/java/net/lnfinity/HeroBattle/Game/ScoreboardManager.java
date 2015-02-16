package net.lnfinity.HeroBattle.Game;

import net.lnfinity.HeroBattle.HeroBattle;
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
		percentageSidebar.setDisplayName("Pourcentages");
	}

	/**
	 * To be called when the game starts.
	 */
	public void init() {
		for(Player player : p.getServer().getOnlinePlayers()) {
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
		int percentage = p.getGamePlayer(player).getPercentage();

		percentageSidebar.getScore(player.getName()).setScore(percentage);
		percentageBelowName.getScore(player.getName()).setScore(percentage);
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
