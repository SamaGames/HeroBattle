package net.lnfinity.HeroBattle.Listeners;

import java.util.Arrays;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;
import net.samagames.gameapi.GameAPI;
import net.samagames.gameapi.events.FinishJoinPlayerEvent;
import net.samagames.gameapi.json.Status;
import net.samagames.utils.Titles;
import net.zyuiop.MasterBundle.MasterBundle;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

		// Needed so the toggleFlight event is fired when the player double-jump.
		// The event is always cancelled.
		p.setAllowFlight(false); // Temp disabled

		plugin.getGame().teleportHub(p.getUniqueId());

		if(p.getName().equals("6infinity8") || p.getName().equals("AmauryPi")) {
			plugin.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.RED + ChatColor.MAGIC + "iii " + ChatColor.GREEN + ChatColor.BOLD + p.getName() + ChatColor.RED + ChatColor.MAGIC + " iii" + ChatColor.YELLOW + " a rejoint la partie !");
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


		ItemStack classSelectorItem = new ItemStack(Material.NETHER_STAR);
		ItemMeta classSelectorItemMeta = classSelectorItem.getItemMeta();
		classSelectorItemMeta.setDisplayName(
				ChatColor.LIGHT_PURPLE + "Choisissez une " +
				ChatColor.DARK_PURPLE + "classe"
		);
		classSelectorItemMeta.setLore(Arrays.asList(
				ChatColor.GRAY + "Cliquez-droit pour choisir la classe",
				ChatColor.GRAY + "avec laquelle vous allez jouer."
		));
		classSelectorItem.setItemMeta(classSelectorItemMeta);
		p.getInventory().setItem(0, classSelectorItem);

		p.getInventory().setItem(8, plugin.getCoherenceMachine().getLeaveItem());

		p.updateInventory();

		p.getInventory().setHeldItemSlot(0);

		if(plugin.getTimer().getSecondsLeft() > 10) {
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					Titles.sendTitle(p, 10, 80, 0, HeroBattle.GAME_NAME_BICOLOR, ChatColor.WHITE + "Bienvenue en " + HeroBattle.GAME_NAME);
				}
			}, 40l);

			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					Titles.sendTitle(p, 0, 80, 10, HeroBattle.GAME_NAME_BICOLOR, ChatColor.WHITE + "N'oubliez pas de " + ChatColor.LIGHT_PURPLE + "choisir une classe" + ChatColor.WHITE + " !");
				}
			}, 120l);
		}

		GameAPI.getManager().sendArena();
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent ev) {
		if(plugin.getGame().getStatus() == Status.Available || plugin.getGame().getStatus() == Status.Starting) {
			if(ev.getPlayer().getName().equals("6infinity8") || ev.getPlayer().getName().equals("AmauryPi")) {
				plugin.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.RED + ChatColor.MAGIC + "iii " + ChatColor.GREEN + ChatColor.BOLD + ev.getPlayer().getName() + ChatColor.RED + ChatColor.MAGIC + " iii" + ChatColor.YELLOW + " s'est déconnecté");
			} else {
			plugin.getServer().broadcastMessage(HeroBattle.GAME_TAG + ev.getPlayer().getDisplayName() + ChatColor.YELLOW + " s'est déconnecté");
			}
		}

		if (plugin.getGame().getStatus() == Status.Starting) {
			if (plugin.getTimer().isEnabled() && plugin.getPlayerCount() - 1 < plugin.getGame().getMinPlayers()) {
				plugin.getTimer().cancelTimer();
				plugin.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.YELLOW + "Il n'y a plus assez de joueurs pour commencer la partie !");
			}
		}

		else if(plugin.getGame().getStatus() == Status.InGame) {
			if (plugin.getPlayerCount() == 0 && MasterBundle.isDbEnabled) {
				plugin.getGame().setStatus(Status.Stopping);
				Bukkit.shutdown();
			} else {
				plugin.getGame().onPlayerQuit(ev.getPlayer().getUniqueId());
			}
		}
		ev.setQuitMessage(null);
		GameAPI.getManager().sendArena();
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (plugin.getGame().getStatus() != Status.InGame
				&& e.hasItem() && e.getItem().equals(plugin.getCoherenceMachine().getLeaveItem())) {
			e.getPlayer().kickPlayer("");
		}
	}
}
