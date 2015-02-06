package net.lnfinity.HeroBattle.Tools;

import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Tasks.EarthquakeTask;
import net.lnfinity.HeroBattle.Utils.GlowEffect;
import net.lnfinity.HeroBattle.Utils.ItemCouldown;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EarthquakeTool extends PlayerTool {

	public final int COOLDOWN = 30;

	public EarthquakeTool(HeroBattle plugin) {
		super(plugin);
	}

	@Override
	public String getToolID() {
		return "tool.earthquake";
	}

	@Override
	public String getName() {
		return ChatColor.BLACK + "Tremblement de terre";
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList(ChatColor.GRAY + "Clic droit pour activer l'effet.", ChatColor.DARK_GRAY
				+ "Ne peut être utilisé que toutes les " + COOLDOWN + " secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.FLINT, 1);
		GlowEffect.addGlow(item);

		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (tool.containsEnchantment(GlowEffect.getGlow())) {
			new ItemCouldown(p, player, this, COOLDOWN);
			if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
				player.setVelocity(player.getVelocity().setY(-1));

				player.setFallDistance(10);

				p.getGamePlayer(player).addTask(new EarthquakeTask(p, player));
			} else {
				player.sendMessage(ChatColor.RED + "Vous échouez tremblement de terre");
			}
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}

	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {

	}

}
