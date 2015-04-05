package net.lnfinity.HeroBattle.Tools;

import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Utils.ItemCooldown;
import net.md_5.bungee.api.ChatColor;
import net.samagames.utils.GlowEffect;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ThunderTool extends PlayerTool {

	private final int COOLDOWN; // seconds

	public ThunderTool(HeroBattle plugin, int cooldown) {
		super(plugin);
		COOLDOWN = cooldown;
	}

	@Override
	public String getToolID() {
		return "tool.thunder";
	}

	@Override
	public String getName() {
		return ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Colère de Zeus";
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList(ChatColor.GRAY + "Invoque un éclair dans la direction visée.", "", ChatColor.DARK_GRAY
				+ "" + ChatColor.ITALIC + "Clic droit pour activer l'effet.", ChatColor.DARK_GRAY + ""
				+ ChatColor.ITALIC + "Ne peut être utilisé que toutes les " + COOLDOWN + " secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.FIREBALL);
		GlowEffect.addGlow(item);
		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (tool.containsEnchantment(GlowEffect.getGlow())) {
			new ItemCooldown(p, player, this, COOLDOWN);
			Block b = p.getGame().getTargetBlock(player, 20);
			if (b != null) {
				player.getWorld().strikeLightning(b.getLocation());
			} else {
				player.sendMessage(ChatColor.RED + "Vous échouez votre colère");
			}
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {

	}

}
