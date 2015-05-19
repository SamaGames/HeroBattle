package net.lnfinity.HeroBattle.tools.displayers.weapons;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class SwordVariant9Tool extends SwordTool {
	public SwordVariant9Tool(HeroBattle plugin) {
		super(plugin);
	}

	@Override
	public String getToolID() {
		return "tool.sword.variant8";
	}

	@Override
	public String getName() {
		return ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "MAC-34";
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.DIAMOND_HOE, 1);

		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);

		return item;
	}
}
