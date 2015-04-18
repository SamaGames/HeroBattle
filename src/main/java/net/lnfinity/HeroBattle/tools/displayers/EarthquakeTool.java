package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tasks.displayers.EarthquakeTask;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.samagames.utils.GlowEffect;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EarthquakeTool extends PlayerTool {

	public final int COOLDOWN;

	public EarthquakeTool(HeroBattle plugin, int cooldown) {
		super(plugin);
		COOLDOWN = cooldown;
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
		return Utils.getToolDescription(ChatColor.GRAY + "Vous fait tomber au sol provoquant ainsi un séisme qui occasionne aux joueurs alentours " + ChatColor.RED + "20 " + ChatColor.GRAY + "à " + ChatColor.RED + "50 " + ChatColor.GRAY + "dégâts. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.FLINT, 1);
		GlowEffect.addGlow(item);

		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (ToolsUtils.isToolAvailable(tool)) {
			new ItemCooldown(p, player, this, COOLDOWN);
			if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
				player.setVelocity(player.getVelocity().setY(-1));
				player.setFallDistance(10);

				p.getGamePlayer(player).addTask(new EarthquakeTask(p, player));

			} else {
				player.sendMessage(ChatColor.RED + "Vous ne chutez pas d'assez haut, vous échouez !");
			}
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}

	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {

	}

}
