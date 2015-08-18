package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.tasks.displayers.*;
import net.lnfinity.HeroBattle.tools.*;
import net.lnfinity.HeroBattle.utils.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

import java.util.*;

public class EarthquakeTool extends PlayerTool {

	private final int COOLDOWN;
	private final int MIN_DAMAGES;
	private final int MAX_DAMAGES;

	public EarthquakeTool(HeroBattle plugin, int cooldown, int min, int max) {
		super(plugin);
		COOLDOWN = cooldown;
		MIN_DAMAGES = min;
		MAX_DAMAGES = max;
	}

	@Override
	public String getToolID() {
		return "tool.earthquake";
	}

	@Override
	public String getName() {
		return ChatColor.BLACK + "" + ChatColor.BOLD + "Tremblement de terre";
	}

	@Override
	public List<String> getDescription() {
		return Utils.getToolDescription(ChatColor.GRAY + "Vous fait tomber au sol provoquant ainsi un séisme qui occasionne aux joueurs alentours " + ChatColor.RED + MIN_DAMAGES + " " + ChatColor.GRAY + "à " + ChatColor.RED + MAX_DAMAGES + " " + ChatColor.GRAY + "dégâts. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.FLINT, 1);
		ToolsUtils.resetTool(item);

		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (ToolsUtils.isToolAvailable(tool)) {
			new ItemCooldown(p, player, this, COOLDOWN);
			if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
				player.setVelocity(player.getVelocity().setY(-1));
				player.setFallDistance(10);

				p.getGamePlayer(player).addTask(new EarthquakeTask(p, player, MIN_DAMAGES, MAX_DAMAGES));

			} else {
				player.sendMessage(ChatColor.RED + "Vous ne chutez pas d'assez haut, vous échouez !");
			}
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}

	}
}
