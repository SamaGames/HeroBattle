package net.lnfinity.HeroBattle.Game;

import net.lnfinity.HeroBattle.HeroBattle;

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
		if (player.getGameMode() == GameMode.ADVENTURE) {
			percentageSidebar.getScore(heartsToString(lives, maxLives) + ChatColor.WHITE + " " + player.getName())
					.setScore(percentage);
		} else if(player.getGameMode() == GameMode.SPECTATOR) {
			percentageSidebar.getScore(heartsToString(0, maxLives) + ChatColor.GRAY + " " + player.getName())
			.setScore(-1);
		}
		

		percentageBelowName.getScore(player.getName()).setScore(percentage);
	}

	private String heartsToString(int hearts, int maxHearts) {
		String str = ChatColor.RED + "";
		for (int i = 1; i <= maxHearts; i++) {
			if (i <= hearts) {
				str += "❤";
			} else {
				str += ChatColor.GRAY + "❤";
			}
		}
		return str;
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
