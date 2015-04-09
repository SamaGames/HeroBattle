package net.lnfinity.HeroBattle.tools;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SwordVariant3Tool extends SwordTool {

	public SwordVariant3Tool(HeroBattle plugin) {
		super(plugin);
	}
	
	@Override
	public String getToolID() {
		return "tool.sword.variant3";
	}

	@Override
	public String getName() {
		return ChatColor.GRAY + "" + ChatColor.BOLD + "Sabre de l'archer";
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.STONE_SWORD, 1);

		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);

		return item;
	}

}
