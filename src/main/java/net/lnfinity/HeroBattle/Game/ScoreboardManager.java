package net.lnfinity.HeroBattle.Game;

import net.lnfinity.HeroBattle.HeroBattle;

import net.lnfinity.HeroBattle.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
		percentageSidebar.setDisplayName(HeroBattle.GAME_NAME_FORMATTED);
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
		GamePlayer hbPlayer = p.getGamePlayer(player);

		int percentage = hbPlayer.getPercentage();

		if (player.getGameMode() == GameMode.ADVENTURE) {
			percentageSidebar.getScore(Utils.heartsToString(hbPlayer) + ChatColor.WHITE + " " + player.getName())
					.setScore(percentage);
		}
		else if(player.getGameMode() == GameMode.SPECTATOR) {
			percentageSidebar.getScore(Utils.heartsToString(hbPlayer) + ChatColor.GRAY + " " + player.getName())
			.setScore(-1);
		}

		percentageBelowName.getScore(player.getName()).setScore(percentage);
	}

	public void refresh() {
		percentageSidebar.unregister();
		percentageSidebar = board.registerNewObjective("perct_sidebar", "dummy");
		percentageSidebar.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Hero" + ChatColor.LIGHT_PURPLE
				+ "" + ChatColor.BOLD + "Battle");
		percentageSidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
		for (Player player : p.getServer().getOnlinePlayers()) {
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
