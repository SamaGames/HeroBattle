package net.lnfinity.HeroBattle.Listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Game.GamePlayer;
import net.md_5.bungee.api.ChatColor;
import net.samagames.gameapi.GameAPI;
import net.samagames.gameapi.events.FinishJoinPlayerEvent;
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

public class MasterListener implements Listener {

	private HeroBattle plugin;

	public MasterListener(HeroBattle p) {
		plugin = p;
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(FinishJoinPlayerEvent ev) {
		final Player p = plugin.getServer().getPlayer(ev.getPlayer());
		plugin.addGamePlayer(p);
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.setExp(0);
		p.setLevel(0);
		p.setTotalExperience(0);
		p.setGameMode(GameMode.ADVENTURE);

		// If the player left during a tutorial, this value may be set to 0f.
		p.setFlySpeed(0.1f);

		// Needed so the toggleFlight event is fired when the player
		// double-jump.
		// The event is always cancelled.
		p.setAllowFlight(false); // Temp disabled

		plugin.getGame().teleportHub(p.getUniqueId());
		
		plugin.getClassManager().addPlayerClasses(p);

		if (p.getName().equals("6infinity8") || p.getName().equals("AmauryPi")) {
			plugin.getServer().broadcastMessage(
					HeroBattle.GAME_TAG
							+ ChatColor.RED + ChatColor.MAGIC + "|||"
							+ ChatColor.GREEN + ChatColor.BOLD + " " + p.getName() + " "
							+ ChatColor.RED + ChatColor.MAGIC + "|||"
							+ ChatColor.YELLOW + " a rejoint la partie !");
		} else {
			plugin.getCoherenceMachine().getMessageManager().writePlayerJoinArenaMessage(p, plugin.getGame());
		}
		plugin.getCoherenceMachine().getMessageManager().writeWelcomeInGameMessage(p);

		if (!plugin.getTimer().isEnabled() && plugin.getPlayerCount() >= plugin.getGame().getMinPlayers()) {
			plugin.getTimer().restartTimer();
		}

		p.setScoreboard(plugin.getScoreboardManager().getScoreboard());

		p.setMaxHealth(20);
		p.setHealth(20);

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
				int elo = 2000;
				if (MasterBundle.isDbEnabled) {
					elo = StatsApi.getPlayerStat(gamePlayer.getPlayerUniqueID(), "herobattle", "elo");
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
				plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
					@Override
					public void run() {
						p.sendMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Votre niveau est actuellement de " + ChatColor.DARK_GREEN + gamePlayer.getElo());
					}
				}, 20L);
			}
		});

		GameAPI.getManager().sendArena();
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent ev) {
		if (plugin.getGame().getStatus() == Status.Available || plugin.getGame().getStatus() == Status.Starting) {
			if (ev.getPlayer().getName().equals("6infinity8") || ev.getPlayer().getName().equals("AmauryPi")) {
				plugin.getServer().broadcastMessage(
						HeroBattle.GAME_TAG
								+ ChatColor.RED + ChatColor.MAGIC + "|||"
								+ ChatColor.GREEN + ChatColor.BOLD + " " + ev.getPlayer().getName() + " "
								+ ChatColor.RED + ChatColor.MAGIC + "|||"
								+ ChatColor.YELLOW + " s'est déconnecté");
			} else {
				plugin.getServer().broadcastMessage(
						HeroBattle.GAME_TAG + ChatColor.YELLOW + ev.getPlayer().getName() + ChatColor.YELLOW
								+ " s'est déconnecté");
			}
		}

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
		ev.setQuitMessage(null);
		
		for (Player player : plugin.getServer().getOnlinePlayers()) {
				player.showPlayer(ev.getPlayer());
				ev.getPlayer().showPlayer(player);
		}
		
		if(plugin.getGame().getStatus() != Status.InGame && plugin.getGame().getStatus() != Status.Stopping) {
			plugin.removeGamePlayer(ev.getPlayer());
		}
		
		GameAPI.getManager().sendArena();
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (plugin.getGame().getStatus() != Status.InGame && e.hasItem()
				&& e.getItem().equals(plugin.getCoherenceMachine().getLeaveItem())) {
			e.getPlayer().kickPlayer("");
		}
	}
}
