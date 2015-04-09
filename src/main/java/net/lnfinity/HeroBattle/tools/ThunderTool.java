package net.lnfinity.HeroBattle.tools;

import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.Utils;
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
		return Utils.getToolDescription(ChatColor.GRAY + "Invoque un éclair dans la direction visée blessant les joueurs touchés par la foudre de " + ChatColor.RED + "25 " + ChatColor.GRAY + "à " + ChatColor.RED + "50 " + ChatColor.GRAY + "pourcents. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
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
