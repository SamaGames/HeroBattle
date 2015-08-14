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

package net.lnfinity.HeroBattle.gui;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.classes.*;
import net.lnfinity.HeroBattle.classes.displayers.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.gui.core.*;
import net.samagames.utils.*;
import org.bukkit.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;
import java.util.logging.*;


public class ClassSelectorGui extends ActionGui
{

	/**
	 * The fake classes displayed in the selector, to tease the players.
	 */
	private final int COMING_SOON_CLASSES_COUNT = 3;


	private GamePlayer gPlayer = null;
	private String typedOnKeyboard = "";


	/* **  Display part  ** */

	@Override
	protected void onUpdate()
	{
		gPlayer = HeroBattle.getInstance().getGamePlayer(getPlayer().getUniqueId());
		if(gPlayer == null)
			throw new IllegalStateException("Cannot open the selector GUI of a non-player! - UUID: " + getPlayer().getUniqueId());


		List<PlayerClass> classList = new ArrayList<>(HeroBattle.getInstance().getClassManager().getAvailableClasses());
		for (int i = 0; i < COMING_SOON_CLASSES_COUNT; i++)
			classList.add(new NotYetAvailableClass(HeroBattle.getInstance()));

		Integer classCount = classList.size();


		setTitle(ChatColor.BLACK + "Sélection de la classe");
		setSize(classCount + ((classCount / 9) * 2) + 27);


		action("random", 4, getRandomButton());


		int slot = 10;
		int displayedClasses = 0;
		Iterator<PlayerClass> iterClass = classList.iterator();
		for (; ; slot++)
		{
			if(!iterClass.hasNext()) break;

			PlayerClass classToDisplay = iterClass.next();
			action("class_" + classToDisplay.getName(), slot, getClassButton(classToDisplay));

			displayedClasses++;

			if(slot % 9 == 7)
			{
				slot += 2;

				// On the last line?
				if(slot >= 9 * (Math.ceil(((double) classCount) / 7) - 1))
				{
					slot += GuiUtils.calculateShiftNeededToCenter(classCount - displayedClasses) - 1;
				}
			}
		}


		// Keyboard
		slot += 9 - slot % 9;
		for (int key = 1 ; key <= 9 ; slot++, key++)
		{
			action("keyboard_" + key, slot);
		}

		action("keyboard_0", slot + 4);


		// Exit
		action("exit", getSize() - 1, getExitButton());
	}

	private ItemStack getRandomButton()
	{
		Boolean isEnabled = (gPlayer.getPlayerClass() == null);


		ItemStack randomClass = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta randomMeta = (SkullMeta) randomClass.getItemMeta();

		randomMeta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Classe aléatoire");

		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Indécis ? Laissez le Destin choisir votre");
		lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "classe !");
		lore.add("");
		lore.add(ChatColor.LIGHT_PURPLE + "Choisir explicitement une classe aléatoire");
		lore.add(ChatColor.LIGHT_PURPLE + "vous permet de multiplier vos gains !");
		lore.add("");
		lore.add(ChatColor.GRAY + "• Clic gauche pour une classe aléatoire");

		if (isEnabled)
		{
			lore.add("");
			lore.add(ChatColor.LIGHT_PURPLE + "Sélectionné");
		}

		randomMeta.setLore(lore);

		GuiUtils.hideItemAttributes(randomMeta);

		randomMeta.setOwner("MHF_Question");
		randomClass.setItemMeta(randomMeta);

		if (isEnabled) {
			GlowEffect.addGlow(randomClass);
		}

		return randomClass;
	}

	/**
	 * Returns the item which, when clicked, selects or displays a class.
	 *
	 * @param classToDisplay The class to display.
	 * @return The item.
	 */
	private ItemStack getClassButton(PlayerClass classToDisplay)
	{
		ItemStack item = new ItemStack(classToDisplay.getIcon());
		ItemMeta meta = item.getItemMeta();

		Boolean isEnabled = gPlayer.getPlayerClass() != null && gPlayer.getPlayerClass().equals(classToDisplay);
		Boolean available = HeroBattle.getInstance().getClassManager().playerHasClass(gPlayer, classToDisplay.getType());


		if (isEnabled)
			meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "" + classToDisplay.getName());
		else if (!available)
			meta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + classToDisplay.getName());
		else
			meta.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + classToDisplay.getName());

		ArrayList<String> lore = new ArrayList<>();

		for (String descriptionLine : classToDisplay.getDescription()) {
			lore.add(ChatColor.DARK_PURPLE + descriptionLine);
		}


		if (!(classToDisplay instanceof NotYetAvailableClass))
		{
			lore.add("");

			lore.addAll(classToDisplay.getClassDetailsLore());

			lore.add("");
			lore.add("");

			if (available)
			{
				lore.add(ChatColor.GRAY + "• Clic gauche pour jouer avec cette classe");
			}

			lore.add(ChatColor.GRAY + "• Clic droit pour voir ses caractéristiques");

			if (isEnabled)
			{
				lore.add("");
				lore.add(ChatColor.LIGHT_PURPLE + "Sélectionné");
			}
			if (!available)
			{
				lore.add("");
				lore.add(ChatColor.RED + "Non débloqué(e)");
			}
		}

		meta.setLore(lore);

		GuiUtils.hideItemAttributes(meta);

		item.setItemMeta(meta);

		if (isEnabled) {
			GlowEffect.addGlow(item);
		}

		return item;
	}

	/**
	 * Returns the item (door) which, when clicked, closes the selector.
	 *
	 * @return The item.
	 */
	public ItemStack getExitButton() {
		ItemStack door = new ItemStack(Material.WOOD_DOOR);

		ItemMeta meta = door.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Fermer");
		door.setItemMeta(meta);

		return door;
	}



	/* **  Actions  ** */

	protected void action_random()
	{
		HeroBattle.getInstance().getClassManager().setPlayerClass(((Player) getPlayer()), null, true);
		close();
	}

	protected void action_exit()
	{
		close();
	}

	@Override
	protected void unknown_action(String action, int slot, ItemStack item, InventoryClickEvent event)
	{
		if(action.startsWith("class_")) // Class selection
		{
			action_class(action.split("_")[1], event.isRightClick());
		}
		else if(action.startsWith("keyboard_")) // Keyboard type
		{
			try
			{
				action_keyboard(Integer.valueOf(action.split("_")[1]));
			}
			catch(NumberFormatException e)
			{
				HeroBattle.getInstance().getLogger().log(Level.WARNING, "Invalid keyboard number. Don't write code while drunk.", e);
			}
		}
	}

	protected void action_class(String className, boolean isRightClick)
	{
		PlayerClass clickedClass = HeroBattle.getInstance().getClassManager().getClassFromName(((Player) getPlayer()), className);

		if(clickedClass == null)
		{
			close();
			return;
		}


		if(isRightClick)
		{
			Gui.open(getPlayer(), new ClassDetailsGui(clickedClass, true));
		}
		else
		{
			if (HeroBattle.getInstance().getClassManager().playerHasClass(gPlayer, clickedClass.getType()))
			{
				HeroBattle.getInstance().getClassManager().setPlayerClass(((Player) getPlayer()), clickedClass, true);
			}
			else
			{
				getPlayer().sendMessage(ChatColor.RED + "Vous ne possédez pas cette classe. Vous pouvez acheter des classes et les améliorer depuis la boutique.");
			}

			close();
		}
	}

	protected void action_keyboard(int key)
	{
		typedOnKeyboard += "" + key;


		final String sixLastDigits = getLastNDigits(6);

		if (sixLastDigits.equals(gPlayer.getElo() + "42"))
		{
			HeroBattle.getInstance().getClassManager().setPlayerClass(((Player) getPlayer()), new MaiteClass(HeroBattle.getInstance()), true);
			close();
		}
		else if (sixLastDigits.equals("401710"))
		{
			HeroBattle.getInstance().getClassManager().getDewoitineUnlocks().add(getPlayer().getUniqueId());
		}
	}

	private String getLastNDigits(int n)
	{
		if (typedOnKeyboard.length() <= n)
		{
			return typedOnKeyboard;
		}
		else
		{
			return typedOnKeyboard.substring(typedOnKeyboard.length() - n);
		}
	}
}
