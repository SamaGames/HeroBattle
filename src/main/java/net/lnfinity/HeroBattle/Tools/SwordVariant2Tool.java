package net.lnfinity.HeroBattle.Tools;

import net.lnfinity.HeroBattle.HeroBattle;
import net.md_5.bungee.api.ChatColor;
import net.samagames.utils.GlowEffect;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SwordVariant2Tool extends SwordTool {

	public SwordVariant2Tool(HeroBattle plugin) {
		super(plugin);
	}
	
	@Override
	public String getToolID() {
		return "tool.sword.variant2";
	}

	@Override
	public String getName() {
		return ChatColor.DARK_PURPLE + "Baguette Magique";
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);

		GlowEffect.addGlow(item);
		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);

		return item;
	}

}
