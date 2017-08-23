package net.lnfinity.HeroBattle.tutorial;


import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;
import net.samagames.tools.Titles;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/*
 * This file is part of HeroBattle.
 *
 * HeroBattle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HeroBattle is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HeroBattle.  If not, see <http://www.gnu.org/licenses/>.
 */
public class TutorialRunner implements Runnable
{

	private final String tutorialInChatPrefix = ChatColor.GRAY + "│ " + ChatColor.RESET;
	private HeroBattle p;
	private Player player;
	private int currentChapter = 0;
	private int currentText = 0;
	private List<TutorialChapter> content;

	public TutorialRunner(HeroBattle plugin, UUID playerId)
	{
		this.p = plugin;
		this.player = p.getServer().getPlayer(playerId);

		this.content = p.getTutorialDisplayer().getContent();
	}

	@Override
	public void run()
	{

		if (!player.isOnline())
		{
			p.getTutorialDisplayer().stop(player.getUniqueId());
			return;
		}

		if (currentChapter == content.size())
		{
			// The end.
			p.getTutorialDisplayer().stop(player.getUniqueId());
			return;
		}


		TutorialChapter chapter = content.get(currentChapter);

		// Delays of fade-in, fade-out and display
		int fadeIn = (currentText == 0) ? 10 : 0;
		int fadeOut = (currentText == chapter.getContent().size() - 1) ? 10 : 0;
		int readingTime = (int) (fadeOut == 10 ? TutorialDisplayer.READING_TIME - 10 : TutorialDisplayer.READING_TIME + 5);


		// New chapter, new location
		if (currentText == 0)
		{
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
		if (chapter.isDisplayedInChat())
		{
			if (currentText == 0) player.sendMessage(tutorialInChatPrefix + chapter.getTitle());
			player.sendMessage(tutorialInChatPrefix + chapter.getContent().get(currentText));
		}

		// Next one?
		currentText++;
		if (currentText == chapter.getContent().size())
		{
			currentChapter++;
			currentText = 0;
		}
	}
}
