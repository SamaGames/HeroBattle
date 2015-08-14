package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.samagames.utils.GlowEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public class Cannon20mmTool extends ArrowsTool {
	public Cannon20mmTool(HeroBattle plugin, int cooldown, int count, int min, int max) {
		super(plugin, cooldown, count, 0, 0, min, max);
	}

	@Override
	public String getToolID() {
		return "tool.cannon";
	}

	@Override
	public String getName() {
		return ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Canon 20mm";
	}

	@Override
	public List<String> getDescription() {
		return Utils.getToolDescription(ChatColor.GRAY + "Clic pour lancer une flèche explosive causant " + ChatColor.RED + MIN_EXPLOSION + " " + ChatColor.GRAY + "à " + ChatColor.RED + MAX_EXPLOSION + " " + ChatColor.GRAY + "dégâts au joueur touché. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.STICK);
		GlowEffect.addGlow(item);
		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		super.onLeftClick(player, tool, event);
	}
}
