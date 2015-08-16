/*
 * Copyright (C) 2015 Amaury Carrade
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.lnfinity.HeroBattle.gui.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;

import net.lnfinity.HeroBattle.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


/**
 * Various utility methods for GUIs.
 *
 * @author ProkopyL (main) & Amaury Carrade
 */
abstract public class GuiUtils
{
	static private Method addItemFlagsMethod = null;
	static private Object[] itemFlagValues = null;

	/**
	 * Initializes the GUI utilities.
	 * This method must be called on plugin enabling.
	 */
	static public void init()
	{
		try
		{
			Class<?> itemFlagClass = Class.forName("org.bukkit.inventory.ItemFlag");
			Method valuesMethod = itemFlagClass.getDeclaredMethod("values");
			itemFlagValues = (Object[]) valuesMethod.invoke(null);
			addItemFlagsMethod = ItemMeta.class.getMethod("addItemFlags", itemFlagClass);
			addItemFlagsMethod.setAccessible(true);

		} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
			// Not supported :c
		} catch (InvocationTargetException e) {
			HeroBattle.get().getLogger().log(Level.SEVERE, "Exception occurred while looking for the ItemFlag API.", e.getCause());
		}
	}

	static public void hideItemAttributes(ItemMeta meta)
	{
		if(addItemFlagsMethod == null) return;
		try
		{
			addItemFlagsMethod.invoke(meta, itemFlagValues);
		}
		catch (IllegalAccessException | InvocationTargetException ex)
		{
			HeroBattle.get().getLogger().log(Level.SEVERE, "Exception occurred while invoking the ItemMeta.addItemFlags method.", ex);
		}
	}

	/**
	 * Calculates the shift needed to center the items on the grid.
	 *
	 * @param itemsCount
	 *            The count. If > 9, this will calculate the shift needed for
	 *            the last line.
	 *
	 * @return The shift needed.
	 */
	static public int calculateShiftNeededToCenter(int itemsCount) {
		itemsCount %= 9;

		switch (itemsCount)
		{
			case 0:
			case 1:
				return 4;
			case 2:
			case 3:
				return 3;
			case 4:
			case 5:
				return 2;
			case 6:
			case 7:
				return 1;
			default:
				return 0;
		}
	}

	/**
	 * Stores the ItemStack at the given index of a GUI's inventory.
	 * The inventory is only updated the next time the Bukkit Scheduler runs (i.e. next server tick).
	 *
	 * @param gui The GUI to update
	 * @param slot The slot where to put the ItemStack
	 * @param item The ItemStack to set
	 */
	static public void setItemLater(Gui gui, int slot, ItemStack item)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(HeroBattle.get(),
				new CreateDisplayItemTask(gui.getInventory(), item, slot));
	}

	static public ItemStack makeItem(Material material)
	{
		return makeItem(material, null, (List<String>)null);
	}

	static public ItemStack makeItem(Material material, String title)
	{
		return makeItem(material, title, (List<String>)null);
	}

	static public ItemStack makeItem(Material material, String title, String... loreLines)
	{
		return makeItem(material, title, Arrays.asList(loreLines));
	}

	static public ItemStack makeItem(Material material, String title, List<String> loreLines)
	{
		return makeItem(new ItemStack(material), title, loreLines);
	}
	static public ItemStack makeItem(ItemStack itemStack, String title, List<String> loreLines)
	{
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(title);
		meta.setLore(loreLines);
		if(itemStack.getType().equals(Material.MAP))
			hideItemAttributes(meta);
		itemStack.setItemMeta(meta);
		return itemStack;
	}

	/**
	 * Implements a bukkit runnable that updates an inventory slot later.
	 */
	static private class CreateDisplayItemTask implements Runnable
	{
		private final Inventory inventory;
		private final ItemStack item;
		private final int slot;

		public CreateDisplayItemTask(Inventory inventory, ItemStack item, int slot)
		{
			this.inventory = inventory;
			this.item = item;
			this.slot = slot;
		}

		@Override
		public void run()
		{
			inventory.setItem(slot, item);
			for(HumanEntity player : inventory.getViewers())
			{
				((Player)player).updateInventory();
			}
		}

	}
}
