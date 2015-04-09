package net.lnfinity.HeroBattle.classes;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class PlayerClass {

	protected HeroBattle p;
	protected List<PlayerTool> tools = new ArrayList<PlayerTool>();

	public PlayerClass(HeroBattle plugin) {
		p = plugin;
	}
	
	/**
	 * Returns the name of the class.
	 *
	 * @return
	 */
	public abstract String getName();

	/**
	 * Returns the icon of the class: an ItemStack displayed in the classes selector GUI.
	 *
	 * <p>
	 *     The display name of this item stack is ignored.
	 * </p>
	 *
	 * @return
	 */
	public abstract ItemStack getIcon();

	/**
	 * Returns the description of this class, displayed as a lore in the selector.
	 *
	 * @return A list of lines displayed in the lore.
	 */
	
	
	/**
	 * Returns the hat the player will have.
	 *
	 * <p>
	 *     The display name of this item stack is ignored.
	 * </p>
	 *
	 * @return
	 */
	public abstract ItemStack getHat();
	
	
	public abstract List<String> getDescription();

	/**
	 * Returns the minimum damages a player with this class can inflate.
	 *
	 * @return
	 */
	public abstract int getMinDamages();

	/**
	 * Returns the maximum damages a player with this class can inflate.
	 *
	 * @return
	 */
	public abstract int getMaxDamages();

	/**
	 * Returns the maximum damages a player can bear.
	 *
	 * @return
	 */
	public abstract int getMaxResistance();
	/**
	 * Returns the lives a player with this class have.
	 *
	 * @return
	 */
	public abstract int getLives();


	/**
	 * Returns a list of the tools in this class.
	 *
	 * @return
	 */
	public List<PlayerTool> getTools() {
		return tools;
	}
	
	public PlayerTool getTool(int i) {
		return tools.get(i);
	}

	/**
	 * Adds a tool for this class.
	 *
	 * @param tool The tool.
	 * @return {@code true} if already in the class.
	 */
	public boolean addTool(PlayerTool tool) {
		return tools.add(tool);
	}

	/**
	 * Removes a tool from this class.
	 *
	 * @param tool The tool.
	 * @return {@code true} if the tool wasn't in the class.
	 */
	public boolean removeTool(PlayerTool tool) {
		return tools.remove(tool);
	}
	
	public abstract PlayerClassType getType();


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PlayerClass)) return false;

		PlayerClass that = (PlayerClass) o;

		return this.getName().equals(that.getName());
	}

	@Override
	public int hashCode() {
		return getName() != null ? getName().hashCode() : 0;
	}
}
