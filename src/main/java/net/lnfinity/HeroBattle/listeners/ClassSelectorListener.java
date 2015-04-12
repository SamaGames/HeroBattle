package net.lnfinity.HeroBattle.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.MaiteClass;
import net.lnfinity.HeroBattle.classes.NotYetAvailableClass;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.md_5.bungee.api.ChatColor;
import net.samagames.utils.GlowEffect;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ClassSelectorListener implements Listener {

	private HeroBattle p;

	private final String TITLE_CLASS_SELECTOR = "Sélection de la classe";
	private final String TITLE_CLASS_DETAILS = "Détails de la classe ";

	private final int COMING_SOON_CLASSES_COUNT = 2;

	public ClassSelectorListener(HeroBattle plugin) {
		p = plugin;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {

		if (e.getWhoClicked() instanceof Player && e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()) {
			Player player = (Player) e.getWhoClicked();
			GamePlayer gamePlayer = p.getGamePlayer(player);
			if (e.getInventory().getName().equals(TITLE_CLASS_SELECTOR)) {
				if (e.getCurrentItem().equals(createExitItem())) {
					player.closeInventory();
					return;
				}
				GamePlayer gPlayer = p.getGamePlayer(player);
				
				PlayerClass clickedClass = p.getClassManager().getClassFromName(player, 
						ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));

				if (clickedClass != null) {
					if (e.getClick().isLeftClick()) {
						if(p.getClassManager().playerHasClass(gamePlayer, clickedClass.getType())) {
							selectClass(player, clickedClass);
						} else {
							player.sendMessage(ChatColor.RED + "Vous ne possédez pas cette classe. Vous pouvez acheter des classes et les améliorer depuis la boutique.");
						}
						player.closeInventory();

					} else if (e.getClick().isRightClick()) {
						createDetails(player, clickedClass);
					}
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equals(createItemRandom(gPlayer.getPlayerClass() == null).getItemMeta().getDisplayName())) {

					selectClass(player, null);
					player.closeInventory();
				} else {
					player.closeInventory();
				}

				e.setCancelled(true);
			}

			else if (e.getInventory().getName().startsWith(TITLE_CLASS_DETAILS)) {
				if (e.getCurrentItem().equals(createBackToListItem())) {
					// Go back to the menu
					createSelector(player);
				}

				else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equals(createUseThisClassItem(null).getItemMeta().getDisplayName())) {

					String className = e.getInventory().getName().replace(TITLE_CLASS_DETAILS, "");

					if(className.equals("Maïté")) {
						selectClass(player, new MaiteClass(p));
					}

					else if(p.getClassManager().playerHasClass(gamePlayer, p.getClassManager().getClassFromName(player, className).getType())) {
						selectClass(player, p.getClassManager().getClassFromName(player, className));
					}

					else {
						player.sendMessage(ChatColor.RED + "Vous ne possédez pas cette classe. Vous pouvez acheter des classes et les améliorer depuis la boutique.");
					}

					player.closeInventory();
				}

				e.setCancelled(true);
			}
		}

		// Easter egg
		else if(e.getWhoClicked() instanceof Player && e.getRawSlot() == e.getInventory().getSize() - 8 && e.getSlotType() == InventoryType.SlotType.CONTAINER) {
			MaiteClass maite = new MaiteClass(p);
			if (e.getClick().isLeftClick()) {
				selectClass(((Player) e.getWhoClicked()), maite);
				e.getWhoClicked().closeInventory();
			} else if (e.getClick().isRightClick()) {
				createDetails(((Player) e.getWhoClicked()), maite);
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.hasItem()) {
			if (e.getItem().getType() == Material.NETHER_STAR) {
				createSelector(e.getPlayer());
			}
			else if(e.getItem().getType() == Material.BOOK) {
				p.getTutorialDisplayer().start(e.getPlayer().getUniqueId());
			}
		}
	}

	public void createSelector(Player player) {
		GamePlayer gamePlayer = p.getGamePlayer(player);
		List<PlayerClass> classes = p.getClassManager().getAvailableClasses();
		Integer inventorySize = (int) (Math.ceil(classes.size() / 9d) * 9) + 27;

		Inventory inv = p.getServer().createInventory(player, inventorySize, TITLE_CLASS_SELECTOR);

		int classesCount = classes.size() + COMING_SOON_CLASSES_COUNT, shift = calculateShiftNeededToCenter(classesCount);

		// Random

		inv.setItem(4, createItemRandom(p.getGamePlayer(player).getPlayerClass() == null));

		// Available classes
		Integer i = 9;
		for (PlayerClass theClass : classes) {
			if(p.getClassManager().playerHasClass(gamePlayer, theClass.getType())) {
				inv.setItem(isOnLastLine(i - 9, classesCount) ? shift + i : i,
						createItem(theClass, p.getGamePlayer(player).getPlayerClass() != null
								&& p.getGamePlayer(player).getPlayerClass().equals(theClass)));
			} else {
				inv.setItem(isOnLastLine(i - 9, classesCount) ? shift + i : i,
						createItem(theClass, p.getGamePlayer(player).getPlayerClass() != null
								&& p.getGamePlayer(player).getPlayerClass().equals(theClass), false));
			}
			

			i++;
		}

		// Placeholder for the other cases
		Integer notYetFinalIndex = i + COMING_SOON_CLASSES_COUNT;
		for (; i < notYetFinalIndex; i++) {
			inv.setItem(isOnLastLine(i - 9, classesCount) ? shift + i : i,
					createItem(new NotYetAvailableClass(p), false, false));
		}

		inv.setItem(inv.getSize() - 1, createExitItem());

		// Contenu conservé pour son contenu (classes)
		// inv.addItem(createItem(Material.DIAMOND_CHESTPLATE, "Brute",
		// "Pour le plaisir de faire des dégâts.", true));
		// inv.addItem(createItem(Material.IRON_SWORD, "Vaillant guerrier",
		// "Réservé aux connaisseurs.", false));
		// inv.addItem(createItem(Material.STONE_AXE, "Bûcheron sournois",
		// "Surveillez vos arrières.", false));
		// inv.addItem(createItem(Material.GOLD_HOE, "Faucheur maudit",
		// "Il a tendance à disparaître la nuit...", false));
		// inv.addItem(createItem(Material.FISHING_ROD, "Pêcheur adroit",
		// "Ne le sous-estimez pas !", false));
		// inv.addItem(createItem(Material.IRON_PICKAXE, "Mineur fou",
		// "Le déranger pourrait l'enrager.", false));
		// inv.addItem(createItem(Material.FLINT_AND_STEEL, "Pyrobarbare",
		// "Le feu, il maitrise. Ou pas.", false));
		// inv.setItem(7, createItem(Material.BARRIER, "???",
		// "Bientôt disponible", false));
		// inv.setItem(8, createItem(Material.BARRIER, "???",
		// "Bientôt disponible", false));

		player.openInventory(inv);
	}

	public void createDetails(Player player, PlayerClass classe) {
		Inventory inv = p.getServer().createInventory(player, 36, TITLE_CLASS_DETAILS + classe.getName());
		int i = 0, toolsCount = classe.getTools().size(), shift = calculateShiftNeededToCenter(toolsCount);

		for (PlayerTool tool : classe.getTools()) {
			inv.setItem(isOnLastLine(i, toolsCount) ? shift + i : i, tool.generateCompleteItem());
			i++;
		}

		ItemStack item = new ItemStack(classe.getHat().getType());
		item.setDurability(classe.getHat().getDurability());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Classe " + classe.getName());
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("");

		lore.add(getBar("Vies", classe.getLives(), 6));
		lore.add(getBar("Dégâts min.", classe.getMinDamages(), 6));
		lore.add(getBar("Dégâts max.", classe.getMaxDamages() - 3, 6));
		lore.add(getBar("Résistance", (classe.getMaxResistance() - 150) / 25, 6));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(20, item);

		inv.setItem(22, createUseThisClassItem(classe));

		inv.setItem(24, createBackToListItem());

		player.openInventory(inv);
	}

	public ItemStack createItem(PlayerClass theClass, boolean isEnabled) {
		return createItem(theClass, isEnabled, true);	
	}
	
	public ItemStack createItem(PlayerClass theClass, boolean isEnabled, boolean avaible) {
		ItemStack item = new ItemStack(theClass.getIcon());
		ItemMeta meta = item.getItemMeta();

		if (isEnabled) {
			meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN + ChatColor.BOLD + "" + theClass.getName());
		} else if(!avaible){
			meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + theClass.getName());
		} else {
			meta.setDisplayName(ChatColor.RESET + theClass.getName());
		}

		ArrayList<String> lore = new ArrayList<String>();

		for (String descriptionLine : theClass.getDescription()) {
			lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + descriptionLine);
		}

		lore.add("");

		lore.add(ChatColor.GRAY + "• Clic gauche pour jouer avec cette classe");
		lore.add(ChatColor.GRAY + "• Clic droit pour voir ses caractéristiques");

		if (isEnabled) {
			lore.add("");
			lore.add(ChatColor.LIGHT_PURPLE + "Sélectionné");
		}

		meta.setLore(lore);
		item.setItemMeta(meta);

		if (isEnabled) {
			GlowEffect.addGlow(item);
		}

		return item;
	}

	public ItemStack createItemRandom(boolean isEnabled) {
		ItemStack randomClass = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta randomMeta = (SkullMeta) randomClass.getItemMeta();

		randomMeta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Classe aléatoire");

		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Indécis ? Laissez le Destin choisir");
		lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "votre classe !");
		lore.add("");
		lore.add(ChatColor.GRAY + "• Clic gauche pour une classe aléatoire");

		if (isEnabled) {
			lore.add("");
			lore.add(ChatColor.LIGHT_PURPLE + "Sélectionné");
		}

		randomMeta.setLore(lore);

		randomMeta.setOwner("MHF_Question");
		randomClass.setItemMeta(randomMeta);

		if (isEnabled) {
			GlowEffect.addGlow(randomClass);
		}

		return randomClass;
	}

	public String getBar(String name, int qty, int total) {
		String str = ChatColor.RED + "" + ChatColor.BOLD;
		for (int i = 1; i <= total; i++) {
			str += "|| ";
			if (qty == i) {
				str += ChatColor.GRAY + "" + ChatColor.BOLD;
			}
		}
		str += ChatColor.RESET + "" + ChatColor.GRAY + " " + name;
		return str;
	}

	/**
	 * Returns the item (door) which, when clicked, displays back the list of
	 * the classes.
	 * 
	 * @return The item.
	 */
	private ItemStack createBackToListItem() {
		ItemStack item = new ItemStack(Material.WOOD_DOOR);
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList("", ChatColor.GRAY + "Clic droit pour revenir"));
		meta.setDisplayName(ChatColor.RESET + "Revenir au choix des classes");
		item.setItemMeta(meta);

		return item;
	}

	/**
	 * Returns the item (door) which, when clicked, displays back the list of
	 * the classes.
	 * 
	 * @param theClass
	 *            The class applied when this is clicked.
	 * 
	 * @return The item.
	 */
	private ItemStack createUseThisClassItem(PlayerClass theClass) {
		ItemStack item = new ItemStack(Material.EMERALD);
		ItemMeta meta = item.getItemMeta();
		if (theClass != null) {
			meta.setLore(Arrays.asList(
					"",
					ChatColor.GRAY + "Clic droit pour sélectionner",
					ChatColor.GRAY + "la classe " + theClass.getName()
			));
		}
		meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Utiliser cette classe");
		item.setItemMeta(meta);

		return item;
	}

	/**
	 * Returns the item (door) which, when clicked, closes the selector.
	 * 
	 * @return The item.
	 */
	public ItemStack createExitItem() {
		ItemStack door = new ItemStack(Material.WOOD_DOOR);

		ItemMeta meta = door.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Fermer");
		door.setItemMeta(meta);

		return door;
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
	public int calculateShiftNeededToCenter(int itemsCount) {
		itemsCount %= 9;

		switch (itemsCount) {
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

	private boolean isOnLastLine(int index, int count) {
		return index >= 9 * (Math.ceil(((double) count) / 9) - 1);
	}

	public void selectClass(Player player, PlayerClass theClass) {
		p.getGamePlayer(player).setPlayerClass(theClass);

		if (theClass != null) {
			player.sendMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Vous avez choisi la classe "
					+ ChatColor.DARK_GREEN + theClass.getName() + ChatColor.GREEN + " !");
		} else {
			player.sendMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Vous avez choisi une classe "
					+ ChatColor.DARK_GREEN + "aléatoire" + ChatColor.GREEN + " !");
		}
	}
}
