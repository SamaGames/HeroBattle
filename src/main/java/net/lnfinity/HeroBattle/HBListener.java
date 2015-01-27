package net.lnfinity.HeroBattle;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;

public class HBListener implements Listener {

	private HeroBattle plugin;
	private boolean waiting = true;

	public HBListener(HeroBattle p) {
		plugin = p;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent ev) {
		Player p = ev.getPlayer();
		plugin.addHBPlayer(p.getUniqueId());
		p.getInventory().clear();
		p.setGameMode(GameMode.ADVENTURE);
		ev.getPlayer().teleport(
				new Location(ev.getPlayer().getWorld(), plugin.getConfig()
						.getInt("locations.hub.x"), plugin.getConfig().getInt(
						"locations.hub.y"), plugin.getConfig().getInt(
						"locations.hub.z")));
		ev.setJoinMessage(HeroBattle.NAME + ChatColor.YELLOW
				+ p.getDisplayName() + ChatColor.YELLOW + " a rejoint l'arène "
				+ ChatColor.DARK_GRAY + "[" + ChatColor.RED
				+ plugin.getPlayerCount() + ChatColor.DARK_GRAY + "/"
				+ ChatColor.RED + "4" + ChatColor.DARK_GRAY + "]");
		if (plugin.getPlayerCount() == 4) {
			plugin.getTimer().restartTimer();
		}
		plugin.getHBPlayer(p.getUniqueId()).setLives(6);
		Damageable d = (Damageable) p;
		d.setHealth(6);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent ev) {
		if (waiting) {
			plugin.getTimer().cancelTimer();
		}
		plugin.removeHBPlayer(ev.getPlayer().getUniqueId());
	}

	public boolean isWaiting() {
		return waiting;
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e) {
		if (waiting) {
			e.setCancelled(true);
		}
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getCause() == DamageCause.FALL) {
				e.setCancelled(true);
				plugin.getHBPlayer(p.getUniqueId()).setDoubleJump(2);
			} else {
				e.setDamage(0);
			}
		}
		if(e.getCause() == DamageCause.ENTITY_ATTACK) {
		
		}	
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player && !waiting) {
			Player player = (Player) e.getEntity();
			plugin.getHBPlayer(player.getUniqueId()).setPercentage(plugin.getHBPlayer(player.getUniqueId()).getPercentage() + 1 + (int)(Math.random() * ((8 - 1) + 1)));
			
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard board = manager.getNewScoreboard();
			 
			Objective objective = board.registerNewObjective("percentage", "dummy");
			objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
			objective.setDisplayName("%");
			 
			for(Player online : Bukkit.getOnlinePlayers()){
			  Score score = objective.getScore(online);
			  score.setScore(plugin.getHBPlayer(online.getUniqueId()).getPercentage());
			}
			 
			for(Player online : Bukkit.getOnlinePlayers()){
			online.setScoreboard(board);
			}
			
			double pw = 10;
			double xa = e.getDamager().getLocation().getX();
			double ya = e.getDamager().getLocation().getZ();
			double xb = e.getEntity().getLocation().getX();
			double yb = e.getEntity().getLocation().getZ();
			double m = (ya - yb) / (xa - xb);
			double p = ya - m * xa;
			double alpha = Math.atan(m * xa + p);
			double xc1 = xb + pw * Math.cos(alpha);
			double xc2 = xb - pw * Math.cos(alpha);
			double yc;
			double xc;
			if(Math.abs(xa - xc1) > Math.abs(xa - xc2)) {
				yc = m * xc1 + p;
				xc = xc1;
			} else {
				yc = m * xc2 + p;
				xc = xc2;
			}
			double a = xc - e.getEntity().getLocation().getX();
			double b = yc - e.getEntity().getLocation().getZ();
			e.getEntity().setVelocity(new Vector(a * plugin.getHBPlayer(player.getUniqueId()).getPercentage(), e.getEntity().getVelocity().getY(), b * plugin.getHBPlayer(player.getUniqueId()).getPercentage()));
		}
	}
	


	@EventHandler
	public void onInventoryMoveItemEvent(InventoryMoveItemEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		HBPlayer hbp = plugin.getHBPlayer(p.getUniqueId());
		if (e.getAction() == Action.RIGHT_CLICK_AIR
				|| e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getItem().getType() == Material.IRON_SWORD) {
				if (p.getLocation().getBlock().getRelative(BlockFace.DOWN)
						.getType() != Material.AIR) {
					hbp.setDoubleJump(2);
				}
				if (hbp.getDoubleJump() > 0) {
					hbp.setDoubleJump(hbp.getDoubleJump() - 1);
					p.setVelocity(p.getVelocity().setY(1.5));
				}
			} else if (e.getItem().getType() == Material.SUGAR) {
				if (e.getItem().getEnchantments().size() >= 1) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
							100, 1));
					new HBCouldown(plugin, p.getUniqueId(), 1, 20);
				} else {
					e.getPlayer()
							.sendMessage(
									ChatColor.RED
											+ "Vous êtes trop fatigué pour réutiliser ça maintenant");
				}
			}
		}
	}

	@EventHandler
	public boolean Foodlevel(FoodLevelChangeEvent event) {
		event.setFoodLevel(100);
		return true;
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (e.getTo().getBlockY() <= 0) {
			plugin.getGame().onPlayerKill(e.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onEntityRegainHealthEvent(EntityRegainHealthEvent e) {
		e.setCancelled(true);
	}

	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}

}
