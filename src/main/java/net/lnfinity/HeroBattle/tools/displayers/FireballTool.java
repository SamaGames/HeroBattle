package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import net.samagames.utils.GlowEffect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FireballTool extends PlayerTool {
	
	public final int COOLDOWN;

	public FireballTool(HeroBattle plugin, int cooldown) {
		super(plugin);
		
		COOLDOWN = cooldown;
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
		return Utils.getToolDescription(ChatColor.GRAY + "Tire une boule de feu qui occasionne " + ChatColor.RED + "16 " + ChatColor.GRAY + "à " + ChatColor.RED + "25 " + ChatColor.GRAY + "pourcents aux joueurs situés près de l'impact. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.CLAY_BALL);
		GlowEffect.addGlow(item);
		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (ToolsUtils.isToolAvailable(tool)) {
			new ItemCooldown(p, player, this, COOLDOWN);
			Fireball fireball = (Fireball) player.getWorld().spawnEntity(player.getLocation().add(player.getLocation().getDirection().normalize().multiply(2)), EntityType.FIREBALL);
			fireball.setVelocity(player.getLocation().getDirection().normalize());
			p.getGame().getFireballsLaunched().put(fireball.getUniqueId(), player.getUniqueId());
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		
	}

}
