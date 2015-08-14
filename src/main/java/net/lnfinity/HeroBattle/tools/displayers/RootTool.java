package net.lnfinity.HeroBattle.tools.displayers;

import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.md_5.bungee.api.ChatColor;
import net.samagames.utils.GlowEffect;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class RootTool extends PlayerTool {

	// TODO not finished yet
	
	private final int COOLDOWN;
	
	public RootTool(HeroBattle plugin, int cooldown) {
		super(plugin);
		
		COOLDOWN = cooldown;
	}

	@Override
	public String getToolID() {
		return "tool.root";
	}

	@Override
	public String getName() {
		return ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Déracinement";
	}

	@Override
	public List<String> getDescription() {
		return null;
	}

	@Override
	public ItemStack getItem() {
		return GlowEffect.addGlow(new ItemStack(Material.DEAD_BUSH));
	}

	@Override
	public void onRightClick(final Player player, ItemStack tool, PlayerInteractEvent event) {
		if (ToolsUtils.isToolAvailable(tool)) {
			new ItemCooldown(p, player, this, COOLDOWN);

			GamePlayer best = null;
			
			for(GamePlayer gamePlayer : p.getGamePlayers().values()) {
				if(!gamePlayer.isPlaying()) continue;
				
				Player victim = Bukkit.getPlayer(gamePlayer.getPlayerUniqueID());
				
				if(!victim.equals(player) && (best == null || victim.getLocation().distanceSquared(player.getLocation()) < Bukkit.getPlayer(best.getPlayerUniqueID()).getLocation().distanceSquared(player.getLocation())))
					best = gamePlayer;
			}
			
			final GamePlayer closest = best;
			
			System.out.println(Bukkit.getPlayer(best.getPlayerUniqueID()).getLocation().distance(player.getLocation()));
			
			if(best == null || Bukkit.getPlayer(best.getPlayerUniqueID()).getLocation().distance(player.getLocation()) > 10) {
				player.sendMessage(ChatColor.RED + "Il n'y a aucun joueur alentour à déraciner !");
				player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 1, 0.5F);
			} else {
				final Player victim = Bukkit.getPlayer(closest.getPlayerUniqueID());
				
				player.getWorld().playSound(player.getLocation(), Sound.CHEST_OPEN, 1, 0.5F);
				
				final Location loc1 = player.getLocation();
				final Location loc2 = victim.getLocation();
				final Vector vec = new Vector(loc2.getX() - loc1.getX(), loc2.getY() - loc1.getY(), loc2.getZ() - loc1.getZ()).multiply(0.05);
				
				new Runnable() {

					private BukkitTask task = null;
					
					@Override
					public void run() {
						task = Bukkit.getScheduler().runTaskTimer(p, new Runnable() {
							
							private Location currentLoc = loc1;
							private int i = 0;
						
							@Override
							public void run() {				
									
								victim.getWorld().playEffect(currentLoc, Effect.STEP_SOUND, Material.LOG.getId(), 0);
								
								currentLoc.add(vec);
								
								if(i < 20) {
									i++;
								} else {
									task.cancel();
									
									victim.setVelocity(new Vector(player.getLocation().getX() - victim.getLocation().getX(), player.getLocation().getY() - victim.getLocation().getY(), player.getLocation().getZ() - victim.getLocation().getZ()).normalize().multiply(2));
									victim.damage(0);
									for(int k = 0; k < 5; k++)
										victim.getWorld().playEffect(currentLoc, Effect.STEP_SOUND, Material.LEAVES.getId(), 10);
								}
								
							}
						}, 2, 1);
					}
					
				}.run();
				
			}
			
			
			//player.playSound(player.getLocation(), Sound.SPLASH, 1, 1);
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {
	}

}
