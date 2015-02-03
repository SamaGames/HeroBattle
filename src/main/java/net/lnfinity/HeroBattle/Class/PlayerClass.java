package net.lnfinity.HeroBattle.Class;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class PlayerClass {
	
	protected ItemStack items[] = new ItemStack[9];

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
	 * Returns the lives a player with this class have.
	 *
	 * @return
	 */
	public abstract int getLives();

	public ItemStack getItem(int i) {
		return items[i];
	}

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
