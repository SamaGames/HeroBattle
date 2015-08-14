package net.lnfinity.HeroBattle.tools.displayers;

import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.samagames.utils.GlowEffect;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class TNTTool extends PlayerTool {

	// TODO not finished yet
	
	private final int COOLDOWN; // seconds
	private final int IGNITION_TIME; // seconds
	
	public TNTTool(HeroBattle plugin, int cooldown, int ignition) {
		super(plugin);
		
		COOLDOWN = cooldown;
		IGNITION_TIME = ignition;
	}

	@Override
	public String getToolID() {
		return "tool.tnt";
	}

	@Override
	public String getName() {
		return ChatColor.GRAY + "" + ChatColor.BOLD + "Minage explosif";
	}

	@Override
	public List<String> getDescription() {
		return Utils.getToolDescription(ChatColor.GRAY + "Le mineur pose une TNT au sol qui explose instantanément. Etant habitué, il ne subit aucun dégâts ; les autres joueurs subissent " + ChatColor.RED + "10 " + ChatColor.GRAY + "à " + ChatColor.RED + "18 " + ChatColor.GRAY + "dégâts. La TNT explose au bout de " + ChatColor.GOLD + IGNITION_TIME + " " + ChatColor.GRAY + "secondes après avoir été lancée. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.TNT);
		GlowEffect.addGlow(item);
		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (ToolsUtils.isToolAvailable(tool)) {
			new ItemCooldown(p, player, this, COOLDOWN);
			player.playSound(player.getLocation(), Sound.FIRE_IGNITE, 1F, 1F);
			
			TNTPrimed tnt = (TNTPrimed) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation().clone().add(0, 1, 0), EntityType.PRIMED_TNT);
			tnt.setVelocity(event.getPlayer().getLocation().getDirection().normalize().multiply(0.5));
			tnt.setFuseTicks(IGNITION_TIME * 20);
		}
        else {
            player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
        }
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {
	}

}
