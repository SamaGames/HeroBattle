package net.lnfinity.HeroBattle.Tutorial;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;


public class TutorialDisplayer {

	private HeroBattle p;

	public static final long READING_TIME = 60l; // ticks


	/**
	 * Map: player's UUID -> task executing the tutorial
	 */
	private Map<UUID, BukkitTask> viewers = new HashMap<>();

	/**
	 * Map: chapter's title -> chapter's content in the {@link TutorialChapter} object.
	 */
	private List<TutorialChapter> content = new LinkedList<>();


	// Big variables names FTW
	private long timeNeededToPlayThisTutorial = 0l; // ticks


	public TutorialDisplayer(HeroBattle plugin) {
		p = plugin;


		/* ***  Tutorial's content  *** */

		content.add(new TutorialChapter(
				p.getGame().getTutorialLocations().get(0),
				HeroBattle.GAME_NAME_BICOLOR,
				Arrays.asList(
						"Comment jouer ?"
				)
		));

		content.add(new TutorialChapter(
				p.getGame().getTutorialLocations().get(0),
				ChatColor.AQUA + "I. " + ChatColor.GOLD + "Gameplay",
				Arrays.asList(
						"Chaque joueur possède une jauge de pourcentage",
						"Elle définit les dommages du joueur",
						"Plus il est élevé, plus les dégâts le feront reculer"
				)
		));

		content.add(new TutorialChapter(
				p.getGame().getTutorialLocations().get(1),
				ChatColor.AQUA + "II. " + ChatColor.GOLD + "But du Jeu",
				Arrays.asList(
						"Faites tomber vos adversaires dans le vide ou mettez les K.O.",
						"Remportez la partie en étant le dernier en lice"
				)
		));

		content.add(new TutorialChapter(
				p.getGame().getTutorialLocations().get(2),
				ChatColor.AQUA + "III. " + ChatColor.GOLD + "Classes",
				Arrays.asList(
						"Choisissez votre classe au début du jeu",
						"Chacune possède ses spécificités"
				)
		));

		content.add(new TutorialChapter(
				p.getGame().getTutorialLocations().get(3),
				ChatColor.AQUA + "IV. " + ChatColor.GOLD + "Objets Spéciaux",
				Arrays.asList(
						"Chaque classe possède des objets différents",
						"Ils permettent d'éxecuter des actions spéciales",
						ChatColor.RED + "Attention" + ChatColor.WHITE + ", ils possèdent un cooldown après chaque utilisation"
				)
		));

		content.add(new TutorialChapter(
				p.getGame().getTutorialLocations().get(3),
				HeroBattle.GAME_NAME_BICOLOR,
				Arrays.asList(
						"Bon jeu et bonne chance !"
				)
		));


		// Time needed?
		for(TutorialChapter chapter : content) {
			timeNeededToPlayThisTutorial += READING_TIME * chapter.getContent().size();
		}
	}

	/**
	 * @return A list of {@link TutorialChapter}s.
	 */
	public List<TutorialChapter> getContent() {
		return content;
	}

	/**
	 * Starts the tutorial for the given player.
	 *
	 * @param id The UUID of the player.
	 */
	public void start(UUID id) {

		if(isWatchingTutorial(id)) {
			p.getLogger().info(p.getServer().getPlayer(id).getName() + "(" + id + ") is trying to see the tutorial whilst watching it.");
			return;
		}

		Player player = p.getServer().getPlayer(id);


		// Sufficient time left?
		if(p.getTimer().getSecondsLeft() * 20 <= timeNeededToPlayThisTutorial) {
			player.sendMessage(ChatColor.RED + "Il ne reste pas assez de temps pour consulter le tutoriel...");
			player.sendMessage(ChatColor.RED + "La partie va bientôt commencer !");

			// TODO text-only version of the tutorial

			return;
		}


		// The player cannot move anymore (except with our teleportations)
		player.setFlySpeed(0f);
		player.setAllowFlight(true);
		player.setFlying(true);

		// All other players are hidden
		for(Player other : p.getServer().getOnlinePlayers()) {
			player.hidePlayer(other);
			other.hidePlayer(player);
		}

		// It's ugly without that. PERFECTIONIST IN THE PLACE.
		player.getInventory().setHeldItemSlot(2);


		// The tutorial is started
		viewers.put(
				id, p.getServer().getScheduler().runTaskTimer(p, new TutorialRunner(p, id), 20l, READING_TIME)
		);
	}

	/**
	 * Stops the tutorial for the given player.
	 *
	 * @param id The UUID of the player.
	 */
	public void stop(UUID id) {

		if(!isWatchingTutorial(id)) return;


		Player player = p.getServer().getPlayer(id);

		if(player != null) {

			// The player can now move.
			player.setFlySpeed(0.1f);
			player.setFlying(false);
			player.setAllowFlight(false);

			// All other players are displayed
			for(Player other : p.getServer().getOnlinePlayers()) {
				player.showPlayer(other);
				other.showPlayer(player);
			}

			// The player is teleported back to the Hub
			p.getGame().teleportHub(id);

			// See start()
			player.getInventory().setHeldItemSlot(0);
		}

		try {
			viewers.get(id).cancel();
		} catch(IllegalStateException ignored) {}

		viewers.remove(id);
	}

	/**
	 * Stops the tutorial for everyone.
	 *
	 * @param reason A reason displayed to the viewers.<br />
	 *               Added in the text « Le tutoriel a été interrompu ! {@code [reason]} » if not null.
	 */
	public void stopForAll(String reason) {
		for(UUID viewerID : viewers.keySet()) {
			stop(viewerID);

			p.getServer().getPlayer(viewerID)
					.sendMessage(ChatColor.RED + "Le tutoriel a été interrompu ! " + ((reason != null) ? reason : ""));
		}
	}

	public boolean isWatchingTutorial(UUID id) {
		return viewers.containsKey(id);
	}
}
