package net.lnfinity.HeroBattle.Listeners;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;
import net.samagames.gameapi.GameAPI;
import net.samagames.gameapi.events.FinishJoinPlayerEvent;

import net.samagames.gameapi.json.Status;
import net.zyuiop.MasterBundle.MasterBundle;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MasterListener implements Listener {

	private HeroBattle plugin;

	public MasterListener(HeroBattle p) {
		plugin = p;
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(FinishJoinPlayerEvent ev) {
		Player p = plugin.getServer().getPlayer(ev.getPlayer());
		plugin.addGamePlayer(p);
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.setExp(0);
		p.setLevel(0);
		p.setTotalExperience(0);
		p.setGameMode(GameMode.ADVENTURE);

		plugin.getGame().teleportHub(p.getUniqueId());

		plugin.getCoherenceMachine().getMessageManager().writePlayerJoinArenaMessage(p, plugin.getGame());

/*
		p.getServer().broadcastMessage(
				HeroBattle.GAME_TAG + ChatColor.YELLOW + p.getDisplayName() + ChatColor.YELLOW + " a rejoint l'arène "
						+ ChatColor.DARK_GRAY + "[" + ChatColor.RED + plugin.getPlayerCount() + ChatColor.DARK_GRAY
						+ "/" + ChatColor.RED + plugin.getGame().getMaxPlayers() + ChatColor.DARK_GRAY + "]");
*/

		if (!plugin.getTimer().isEnabled() && plugin.getPlayerCount() >= plugin.getGame().getMinPlayers()) {
			plugin.getTimer().restartTimer();
		}

		p.setScoreboard(plugin.getScoreboardManager().getScoreboard());

		p.setMaxHealth(20);
		p.setHealth(20);

		// TODO Better display.
		p.getInventory().addItem(new ItemStack(Material.NETHER_STAR));

		p.updateInventory();

		GameAPI.getManager().sendArena();
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent ev) {
		if(plugin.getGame().getStatus() == Status.Available || plugin.getGame().getStatus() == Status.Starting) {
			plugin.getServer().broadcastMessage(HeroBattle.GAME_TAG + ev.getPlayer().getDisplayName() + ChatColor.YELLOW + " s'est déconnecté");
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
}
