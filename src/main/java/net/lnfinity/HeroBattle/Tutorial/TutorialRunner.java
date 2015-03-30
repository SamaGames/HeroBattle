package net.lnfinity.HeroBattle.Tutorial;


import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;
import net.samagames.utils.Titles;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class TutorialRunner implements Runnable {

	private HeroBattle p;
	private Player player;

	private int currentChapter = 0;
	private int currentText = 0;

	private List<TutorialChapter> content;

	private final String tutorialInChatPrefix = ChatColor.GRAY + "│ " + ChatColor.RESET;

	public TutorialRunner(HeroBattle plugin, UUID playerId) {
		this.p = plugin;
		this.player = p.getServer().getPlayer(playerId);

		this.content = p.getTutorialDisplayer().getContent();
	}

	@Override
	public void run() {

		if(!player.isOnline()) {
			p.getTutorialDisplayer().stop(player.getUniqueId());
			return;
		}

		if(currentChapter == content.size()) {
			// The end.
			p.getTutorialDisplayer().stop(player.getUniqueId());
			return;
		}


		TutorialChapter chapter = content.get(currentChapter);

		// Delays of fade-in, fade-out and display
		int fadeIn      = (currentText == 0) ? 10 : 0;
		int fadeOut     = (currentText == chapter.getContent().size() - 1) ? 10 : 0;
		int readingTime = (int) ((fadeIn == 10 || fadeOut == 10) ? TutorialDisplayer.READING_TIME - 10 : TutorialDisplayer.READING_TIME);


		// New chapter, new location
		if(currentText == 0) {
			chapter.teleport(player);
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 1L, 2L);
		}


		// Title version
		Titles.sendTitle(
				player,
				fadeIn, readingTime, fadeOut,
				chapter.getTitle(),
				chapter.getContent().get(currentText)
		);

		// Chat version
		if(currentText == 0) player.sendMessage(tutorialInChatPrefix + chapter.getTitle());
		player.sendMessage(tutorialInChatPrefix + chapter.getContent().get(currentText));


		// Next one?
		currentText++;
		if(currentText == chapter.getContent().size()) {
			currentChapter++;
			currentText = 0;
		}
	}
}
