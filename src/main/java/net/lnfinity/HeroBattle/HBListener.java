package net.lnfinity.HeroBattle;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
		ev.getPlayer().teleport(new Location(ev.getPlayer().getWorld(), plugin.getConfig()
				.getInt("locations.hub.x"), plugin.getConfig()
				.getInt("locations.hub.y"), plugin.getConfig()
				.getInt("locations.hub.z")));
		ev.setJoinMessage(HeroBattle.NAME + ChatColor.YELLOW
				+ p.getDisplayName() + ChatColor.YELLOW + " a rejoint l'arène "
				+ ChatColor.DARK_GRAY + "[" + ChatColor.RED
				+ plugin.getPlayerCount() + ChatColor.DARK_GRAY + "/"
				+ ChatColor.RED + "4" + ChatColor.DARK_GRAY + "]");
		if (plugin.getPlayerCount() == 2) {
			plugin.getTimer().restartTimer();
		}
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
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getCause() == DamageCause.FALL) {
				e.setCancelled(true);
				plugin.getHBPlayer(p.getUniqueId()).setDoubleJump(2);
			} else {
				e.setDamage(0);
			}
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player) {
			
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

		if (e.getItem().getType() == Material.RABBIT_FOOT) {
			if (p.getLocation().getBlock().getRelative(BlockFace.DOWN)
					.getType() != Material.AIR) {
				hbp.setDoubleJump(2);
			}
			if (hbp.getDoubleJump() > 0) {
				hbp.setDoubleJump(hbp.getDoubleJump() - 1);
				p.setVelocity(p.getVelocity().setY(1.0));
				
			}
		} else if (e.getItem().getType() == Material.SUGAR) {
			if(e.getItem().getEnchantments().size() >= 1) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
			new HBCouldown(plugin, p.getUniqueId(), 2, 5);
			} else {
				e.getPlayer().sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
			}
		}
	}

	@EventHandler
	public boolean Foodlevel(FoodLevelChangeEvent event) {
		event.setFoodLevel(100);
		return true;
	}
	
	@EventHandler
    public void onItemDrop (PlayerDropItemEvent e) {
        e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if(e.getTo().getBlockY() <= 0) {
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
