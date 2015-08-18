package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.tools.*;
import net.lnfinity.HeroBattle.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

import java.util.*;

public class FireballTool extends PlayerTool {
	
	private final int COOLDOWN;
	public final int MIN_DAMAGES;
	public final int MAX_DAMAGES;

	public FireballTool(HeroBattle plugin, int cooldown, int min, int max) {
		super(plugin);
		
		COOLDOWN = cooldown;
		MIN_DAMAGES = min;
		MAX_DAMAGES = max;
	}

	@Override
	public String getToolID() {
		return "tool.fireball";
	}

	@Override
	public String getName() {
		return ChatColor.RED + "" + ChatColor.BOLD + "Boule de feu";
	}

	@Override
	public List<String> getDescription() {
		return Utils.getToolDescription(ChatColor.GRAY + "Tire une boule de feu qui occasionne " + ChatColor.RED + MIN_DAMAGES + " " + ChatColor.GRAY + "à " + ChatColor.RED + MAX_DAMAGES + " " + ChatColor.GRAY + "pourcents aux joueurs situés près de l'impact. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.CLAY_BALL);
		ToolsUtils.resetTool(item);
		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (ToolsUtils.isToolAvailable(tool)) {
			new ItemCooldown(p, player, this, COOLDOWN);
			Fireball fireball = (Fireball) player.getWorld().spawnEntity(player.getLocation().add(player.getLocation().getDirection().normalize().multiply(2)), EntityType.FIREBALL);
			fireball.setVelocity(player.getLocation().getDirection().normalize());
			p.getGame().getFireballsLaunched().put(fireball.getUniqueId(), player.getUniqueId());
			
			p.getGame().addEntityParameters(fireball.getUniqueId(), new TripleParameters(MIN_DAMAGES, MAX_DAMAGES));
			
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		
	}
}
