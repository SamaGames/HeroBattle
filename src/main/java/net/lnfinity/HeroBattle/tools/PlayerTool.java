package net.lnfinity.HeroBattle.tools;

import net.lnfinity.HeroBattle.*;
import net.samagames.tools.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;

/**
 * Represents a player tool given by the classes or a powerup and
 * displayed in the inventory of the player.
 */
public abstract class PlayerTool {

	/**
	 * The main class of the plugin.
	 */
	protected HeroBattle p;

	public PlayerTool(HeroBattle plugin) {
		p = plugin;
	}

	/**
	 * Returns an unique identifier for this tool, used internally.
	 * @return
	 */
	public abstract String getToolID();

	/**
	 * Returns the name of the tool. May be formatted. Must be unique.
	 *
	 * @return The name.
	 */
	public abstract String getName();

	/**
	 * Returns the description of the tool.
	 *
	 * <p>Displayed as the lore of the tool, one item for each line of the lore.</p>
	 *
	 * @return The description.
	 */
	public abstract List<String> getDescription();

	/**
	 * Returns the representation of the initial state of this tool in the player inventory.
	 *
	 * <p>
	 * This ItemStack will be cloned when added to the inventory.
	 * Use {@link #getInventoryItem(org.bukkit.entity.Player)} to get the item for a specific player later.
	 * </p>
	 * <p>
	 * Don't include here the name or the description. They will be automatically included when added to
	 * the player inventory.
	 * </p>
	 * @return The item.
	 */
	public abstract ItemStack getItem();

	/**
	 * Called when the player right-clicks with this tool.
	 *
	 * @param player The player who right-clicked.
	 * @param tool The item representing this tool in the player's inventory.
	 * @param event The PlayerInteractEvent fired.
	 */
	public abstract void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event);

	/**
	 * Called when the player left-clicks with this tool.
	 *
	 * @param player The player who left-clicked.
	 * @param tool The item representing this tool in the player's inventory.
	 * @param event The PlayerInteractEvent fired.
	 */
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		onRightClick(player, tool, event);
	}


	/**
	 * Returns the item inside the inventory of the given player representing this tool.
	 *
	 * @param player The player.
	 * @return The ItemStack representing the tool. {@code null} if the player don't have this tool.
	 */
	public ItemStack getInventoryItem(Player player) {
		for(ItemStack tool : player.getInventory()) {
			if(this.isRepresentation(tool)) {
				return tool;
			}
		}

		return null;
	}

	/**
	 * Returns the item inside the inventory of the given player representing this tool.
	 *
	 * @param player The player.
	 * @return The ItemStack representing the tool. {@code -1} if the player don't have this tool.
	 */
	public int getInventoryItemSlot(Player player) {
		for(int slot = 0 ; slot < player.getInventory().getSize(); slot++) {
			ItemStack tool = player.getInventory().getItem(slot);
			if(this.isRepresentation(tool)) {
				return slot;
			}
		}

		return -1;
	}

	/**
	 * Generates the item to be placed in a player inventory for this tool.
	 *
	 * @return An unique ItemStack object (understand: cloned) representing the tool.
	 */
	public ItemStack generateCompleteItem() {
		ItemStack item = getItem().clone();

		boolean glow = item.containsEnchantment(GlowEffect.getGlow());

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getName());
		meta.setLore(getDescription());
		item.setItemMeta(meta);

		if(glow) {
			// We need to re-add the glow effect, because a modification of the item removes it.
			GlowEffect.addGlow(item);
		}

		return item;
	}

	/**
	 * Checks of the given ItemStack is a representation of this tool.
	 *
	 * @param item The ItemStack.
	 * @return {@code true} if this item represents this tool in an inventory.
	 */
	public boolean isRepresentation(ItemStack item) {
		return item != null
				&& item.hasItemMeta()
				&& item.getItemMeta().hasDisplayName()
				&& item.getItemMeta().getDisplayName().equals(getName());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PlayerTool)) return false;

		PlayerTool that = (PlayerTool) o;

		return this.getName().equals(that.getName());
	}

	@Override
	public int hashCode() {
		return getName() != null ? getName().hashCode() : 0;
	}
}