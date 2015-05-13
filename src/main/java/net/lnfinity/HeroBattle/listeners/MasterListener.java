package net.lnfinity.HeroBattle.listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.utils.ActionBar;
import net.lnfinity.HeroBattle.utils.Utils;
import org.bukkit.ChatColor;
import net.samagames.gameapi.GameAPI;
import net.samagames.gameapi.events.FinishJoinPlayerEvent;
import net.samagames.gameapi.events.JoinModEvent;
import net.samagames.gameapi.json.Status;
import net.samagames.utils.Titles;
import net.zyuiop.MasterBundle.MasterBundle;
import net.zyuiop.statsapi.StatsApi;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.Random;

public class MasterListener implements Listener {

	private HeroBattle plugin;

	public MasterListener(HeroBattle p) {
		plugin = p;
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(FinishJoinPlayerEvent ev) {

		if(plugin.getGamePlayers().size() >= plugin.getGame().getTotalMaxPlayers()) {
			ev.refuse(ChatColor.RED + "Cette partie est complète, désolé !");
			return;
		}

		final Player p = plugin.getServer().getPlayer(ev.getPlayer());
		plugin.addGamePlayer(p);

		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.setExp(0);
		p.setLevel(0);
		p.setTotalExperience(0);
		p.setGameMode(GameMode.ADVENTURE);

		p.setScoreboard(plugin.getScoreboardManager().getScoreboard());

		// Debug
		if(!MasterBundle.isDbEnabled)
			p.setDisplayName(ChatColor.values()[new Random().nextInt(ChatColor.values().length)] + p.getName() + ChatColor.RESET);

		// Good color in the tab list
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				String groupColor = Utils.getPlayerColor(p);

				String teamName = "_" + new Random().nextInt(1000) + p.getName();
				teamName = teamName.substring(0, Math.min(teamName.length(), 16));

				Team playerTeam = plugin.getScoreboardManager().getScoreboard().registerNewTeam(teamName);
				playerTeam.setDisplayName(p.getName());
				playerTeam.setPrefix(groupColor);
				playerTeam.addPlayer(p);
			}
		}, 10l);

		// If the player left during a tutorial, this value may be set to 0f.
		p.setFlySpeed(0.1f);

		// Needed so the toggleFlight event is fired when the player
		// double-jump.
		// The event is always cancelled.
		p.setAllowFlight(false); // Temp disabled

		plugin.getGame().teleportHub(p.getUniqueId());
		
		plugin.getClassManager().addPlayerClasses(p);

		plugin.getCoherenceMachine().getMessageManager().writeWelcomeInGameMessage(p);

		/*
		if (p.getName().equals("6infinity8") || p.getName().equals("AmauryPi")) {
			plugin.getServer().broadcastMessage(
					HeroBattle.GAME_TAG
							+ ChatColor.RED + ChatColor.MAGIC + "|||"
							+ ChatColor.GREEN + ChatColor.BOLD + " " + p.getName() + " "
							+ ChatColor.RED + ChatColor.MAGIC + "|||"
							+ ChatColor.YELLOW + " a rejoint la partie ! " + ChatColor.DARK_GRAY + "[" + ChatColor.RED + plugin.getGame().countGamePlayers() + ChatColor.DARK_GRAY + "/" + ChatColor.RED + plugin.getGame().getMaxPlayers() + ChatColor.DARK_GRAY + "]");
		} else {
			plugin.getCoherenceMachine().getMessageManager().writePlayerJoinArenaMessage(p, plugin.getGame());
		}
		*/

		if (!plugin.getTimer().isEnabled() && plugin.getPlayerCount() >= plugin.getGame().getMinPlayers()) {
			plugin.getTimer().restartTimer();
		}

		p.setMaxHealth(20);
		p.setHealth(20);

		if(plugin.getArenaConfig().getBoolean("map.permanentNightVision")) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
		}
		else {
			p.removePotionEffect(PotionEffectType.NIGHT_VISION);
		}

		plugin.getGame().equipPlayer(p);

		if (!plugin.getTimer().isEnabled() || plugin.getTimer().getSecondsLeft() > 15) {
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					if(!plugin.getTutorialDisplayer().isWatchingTutorial(p.getUniqueId())) {
						Titles.sendTitle(p, 10, 80, 0, HeroBattle.GAME_NAME_BICOLOR, ChatColor.WHITE + "Bienvenue en "
								+ HeroBattle.GAME_NAME);
					}
				}
			}, 40l);

			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					if(!plugin.getTutorialDisplayer().isWatchingTutorial(p.getUniqueId())) {
						Titles.sendTitle(p, 0, 80, 0, HeroBattle.GAME_NAME_BICOLOR, ChatColor.WHITE + "N'oubliez pas de "
								+ ChatColor.LIGHT_PURPLE + "choisir une classe" + ChatColor.WHITE + " !");
					}
				}
			}, 120l);
			
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					if(!plugin.getTutorialDisplayer().isWatchingTutorial(p.getUniqueId())) {
						Titles.sendTitle(p, 0, 80, 10, HeroBattle.GAME_NAME_BICOLOR, ChatColor.WHITE + "Un "
								+ ChatColor.GOLD + "tutoriel" + ChatColor.WHITE + " est mis à disposition !");
					}
				}
			}, 200l);
		}

		for (Player player : p.getServer().getOnlinePlayers()) {
			if (plugin.getTutorialDisplayer().isWatchingTutorial(player.getUniqueId()) && !player.equals(p)) {
				player.hidePlayer(p);
				p.hidePlayer(player);
			}
		}
		
		final GamePlayer gamePlayer = plugin.getGamePlayer(p);
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				int elo;
				if (MasterBundle.isDbEnabled) {
					elo = StatsApi.getPlayerStat(gamePlayer.getPlayerUniqueID(), HeroBattle.GAME_NAME_WHITE, "elo");
				} else {
					// Default
					elo = 2000;
				}
				if(elo < 1000) {
					elo = 2000;
				}
				if(elo > 10000) {
					elo = 10000;
				}
				gamePlayer.setElo(elo);
				gamePlayer.setOriginalElo(elo);
				plugin.getScoreboardManager().refreshTab();
				plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
					@Override
					public void run() {
						p.sendMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Votre " + ChatColor.DARK_GREEN + "ELO" + ChatColor.GREEN + " est actuellement de " + ChatColor.DARK_GREEN + gamePlayer.getElo());
					}
				}, 30L);
			}
		});


		ActionBar.sendPermanentMessage(p, ChatColor.GREEN + "Classe sélectionnée : " + ChatColor.DARK_RED + "aucune" + ChatColor.GRAY + " (aléatoire sans bonus)");


		GameAPI.getManager().sendArena();
	}


	@EventHandler
	public void onModeratorJoin(JoinModEvent ev) {
		final Player moderator = plugin.getServer().getPlayer(ev.getPlayer());

		if(moderator == null) return; // Just in case...

		moderator.setScoreboard(plugin.getScoreboardManager().getScoreboard());

		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				moderator.setGameMode(GameMode.SPECTATOR);
			}
		}, 10l);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent ev) {

		/*
		if (plugin.getGame().getStatus() == Status.Available || plugin.getGame().getStatus() == Status.Starting) {
			if (ev.getPlayer().getName().equals("6infinity8") || ev.getPlayer().getName().equals("AmauryPi")) {
				plugin.getServer().broadcastMessage(
						HeroBattle.GAME_TAG
								+ ChatColor.RED + ChatColor.MAGIC + "|||"
								+ ChatColor.GREEN + ChatColor.BOLD + " " + ev.getPlayer().getName() + " "
								+ ChatColor.RED + ChatColor.MAGIC + "|||"
								+ ChatColor.YELLOW + " a quitté la partie");
			} else {
				plugin.getServer().broadcastMessage(
						HeroBattle.GAME_TAG + ChatColor.YELLOW + ev.getPlayer().getName() + ChatColor.YELLOW
								+ " a quitté la partie");
			}
		}
		*/

		if (plugin.getGame().getStatus() == Status.Starting) {
			if (plugin.getTimer().isEnabled() && plugin.getPlayerCount() - 1 < plugin.getGame().getMinPlayers()) {
				plugin.getTimer().cancelTimer();
				plugin.getServer().broadcastMessage(
						HeroBattle.GAME_TAG + ChatColor.YELLOW
								+ "Il n'y a plus assez de joueurs pour commencer la partie !");
			}
		}

		else if (plugin.getGame().getStatus() == Status.InGame) {
			if (plugin.getPlayerCount() == 0 && MasterBundle.isDbEnabled) {
				plugin.getGame().setStatus(Status.Stopping);
				Bukkit.shutdown();
			} else {
				plugin.getGame().onPlayerQuit(ev.getPlayer().getUniqueId());
			}
		}
		
		for (Player player : plugin.getServer().getOnlinePlayers()) {
				player.showPlayer(ev.getPlayer());
				ev.getPlayer().showPlayer(player);
		}
		
		if(plugin.getGame().getStatus() != Status.InGame && plugin.getGame().getStatus() != Status.Stopping) {
			plugin.removeGamePlayer(ev.getPlayer());
		}

		ActionBar.removeMessage(ev.getPlayer());

		plugin.getScoreboardManager().removePlayer(ev.getPlayer());
		
		GameAPI.getManager().sendArena();
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (plugin.getGame().getStatus() != Status.InGame && e.hasItem()
				&& e.getItem().equals(plugin.getCoherenceMachine().getLeaveItem())) {
			GameAPI.kickPlayer(e.getPlayer());
		}
	}
}
