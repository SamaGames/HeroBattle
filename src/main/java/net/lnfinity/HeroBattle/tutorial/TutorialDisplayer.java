package net.lnfinity.HeroBattle.tutorial;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;


public class TutorialDisplayer {

	private HeroBattle p;

	public static final long READING_TIME = 50l; // ticks


	/**
	 * Map: player's UUID -> task executing the tutorial
	 */
	private Map<UUID, BukkitTask> viewers = new HashMap<>();

	/**
	 * Chapter's contents
	 */
	private List<TutorialChapter> content = new LinkedList<>();


	// Big variables names FTW
	private long timeNeededToPlayThisTutorial = 0l; // ticks


	public TutorialDisplayer(HeroBattle plugin) {
		p = plugin;


		/* ***  Tutorial's content  *** */

		addChapter(new TutorialChapter(
				p.getGame().getTutorialLocations().get(0),
				HeroBattle.GAME_NAME_BICOLOR,
				Arrays.asList(
						ChatColor.GREEN + "Comment jouer ?"
				),
				false
		));

		addChapter(new TutorialChapter(
				p.getGame().getTutorialLocations().get(0),
				ChatColor.AQUA + "I. " + ChatColor.GOLD + "Gameplay",
				Arrays.asList(
						ChatColor.GREEN + "Chaque joueur possède une jauge de pourcentage",
						ChatColor.GREEN + "Elle définit les dommages du joueur",
						ChatColor.GREEN + "Plus elle est élevée, plus les dégâts le feront reculer"
				)
		));

		addChapter(new TutorialChapter(
				p.getGame().getTutorialLocations().get(1),
				ChatColor.AQUA + "II. " + ChatColor.GOLD + "But du Jeu",
				Arrays.asList(
						ChatColor.GREEN + "Faites tomber vos adversaires dans le vide",
						ChatColor.GREEN + "...ou mettez les K.O.",
						ChatColor.GREEN + "Remportez la partie en étant le dernier en lice"
				)
		));

		addChapter(new TutorialChapter(
				p.getGame().getTutorialLocations().get(2),
				ChatColor.AQUA + "III. " + ChatColor.GOLD + "Classes",
				Arrays.asList(
						ChatColor.GREEN + "Toutes les classes ont deux attaques spéciales",
						ChatColor.GREEN + "Certaines classes possèdent des effets uniques",
						ChatColor.GREEN + "Vies et résistance sont propres à chaque classe",
						ChatColor.RED + "Attention" + ChatColor.GREEN + ", chaque capacité a un cooldown"
				)
		));

		addChapter(new TutorialChapter(
				p.getGame().getTutorialLocations().get(2),
				ChatColor.AQUA + "IV. " + ChatColor.GOLD + "Types de combat",
				Arrays.asList(
						ChatColor.GREEN + "Différents types de combat en fonction des classes",
						ChatColor.DARK_GREEN + "Mêlée :" + ChatColor.GREEN + " dégâts de zone",
						ChatColor.DARK_GREEN + "Corps à corps :" + ChatColor.GREEN + " dégâts ciblés proches",
						ChatColor.DARK_GREEN + "Distant :" + ChatColor.GREEN + " dégât longue portée"
				)
		));

		addChapter(new TutorialChapter(
				p.getGame().getTutorialLocations().get(3),
				ChatColor.AQUA + "V. " + ChatColor.GOLD + "Powerups",
				Arrays.asList(
						ChatColor.GREEN + "Des powerups peuvent apparaître",
						ChatColor.GREEN + "aléatoirement durant le jeu",
						ChatColor.GREEN + "Restez sur vos gardes !"
				)
		));

		addChapter(new TutorialChapter(
				p.getGame().getTutorialLocations().get(3),
				HeroBattle.GAME_NAME_BICOLOR,
				Arrays.asList(
						ChatColor.GREEN + "Bon jeu et bonne chance !"
				),
				false
		));
	}

	/**
	 * Adds a chapter in the tutorial.
	 *
	 * @param chapter The chapter to add.
	 */
	public void addChapter(TutorialChapter chapter) {
		content.add(chapter);
		timeNeededToPlayThisTutorial += READING_TIME * chapter.getContent().size();
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
		if(p.getTimer().isEnabled() && p.getTimer().getSecondsLeft() * 20 <= timeNeededToPlayThisTutorial) {
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
		
		player.setPlayerTime(p.getArenaConfig().getLong("map.dayTime"), false);

		// The book is removed
		player.getInventory().remove(Material.BOOK);


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

			// The book is restored
			p.getGame().equipPlayer(player);
			
			player.resetPlayerTime();
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
