package net.lnfinity.HeroBattle.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.samagames.gameapi.json.Status;

public class AntiCamping implements Runnable {

	private final HeroBattle p;
	
	private final Map<GamePlayer, Location> players = new HashMap<>();
	private final Map<GamePlayer, Integer> damagesGiven = new HashMap<>();
	private final Map<GamePlayer, Integer> life = new HashMap<>();
	private final Map<GamePlayer, Integer> combo = new HashMap<>();
	private final List<GamePlayer> averted = new ArrayList<>();
	
	public AntiCamping(HeroBattle plugin) {
		p = plugin;
	}
	
	@Override
	public void run() {
		if(p.getGame().getStatus() == Status.Stopping) return;
		
		for(GamePlayer player : p.getGamePlayers().values()) {
			if(!player.isPlaying()) continue; // Next.
			
			if(players.get(player) == null) {
				// Not registered. Ok. Registering you.
				players.put(player, player.getPlayer().getLocation());
				damagesGiven.put(player, (int) player.getPercentageInflicted());
				life.put(player, player.getPercentage());
				combo.put(player, 0);
			} else {
				// Hey ! We know you, lets check you're clean.
				if(players.get(player).distanceSquared(player.getPlayer().getLocation()) <= 0.05 && damagesGiven.get(player) == player.getPercentageInflicted() && life.get(player) == player.getPercentage()) {
					// You're potentially breaking the rule, we'll add a flag to you.
					
					combo.put(player, combo.get(player) + 1);
				} else {
					combo.put(player, 0);
					damagesGiven.put(player, (int) player.getPercentageInflicted());
					life.put(player, player.getPercentage());
				}
				
				if(combo.get(player) >= 5 && !averted.contains(player)) {
					// First time cheating ? Humm... get that last warning
					player.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Nous avons détecté que vous campiez. Merci de jouer plus activement sous peine d'être puni !");
					
					// You're flagged for the rest of the game :>
					averted.add(player);
					combo.put(player, 1);
				}
				
				if(combo.get(player) >= 3 && averted.contains(player)) {
					// Want to play that game with me ? Let's finish it then !
					int n = Utils.randomNumber(40, 80);
					player.basicDamage(n, null);
					player.getPlayer().sendMessage(ChatColor.RED + "Vous avez été puni de " + ChatColor.DARK_RED + n + ChatColor.RED + " dégâts pour avoir été inactif !");
					combo.put(player, 0);
					
					System.out.println("Player " + player.getPlayer().getName() + " has been punished for camping !");
				}
				
				// Update you stats
				players.put(player, player.getPlayer().getLocation());
			}
		}
	}
	
}