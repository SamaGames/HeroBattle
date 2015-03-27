package net.lnfinity.HeroBattle.Listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Class.NotYetAvailableClass;
import net.lnfinity.HeroBattle.Class.PlayerClass;
import net.lnfinity.HeroBattle.Game.GamePlayer;
import net.lnfinity.HeroBattle.Tools.PlayerTool;
import net.md_5.bungee.api.ChatColor;
import net.samagames.utils.GlowEffect;
import net.samagames.utils.Titles;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ClassSelectorListener implements Listener {

	private HeroBattle p;

	private final String TITLE_CLASS_SELECTOR = "Sélection de la classe";
	private final String TITLE_CLASS_DETAILS = "Détails de la classe ";

	private final int COMING_SOON_CLASSES_COUNT = 3;
	private int tutorialTask = -1;

	public ClassSelectorListener(HeroBattle plugin) {
		p = plugin;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player && e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()) {
			Player player = (Player) e.getWhoClicked();

			if (e.getInventory().getName().equals(TITLE_CLASS_SELECTOR)) {
				if (e.getCurrentItem().equals(createExitItem())) {
					player.closeInventory();
					return;
				}

				PlayerClass clickedClass = p.getClassManager().getClassFromName(
						ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));

				GamePlayer gPlayer = p.getGamePlayer(player);

				if (clickedClass != null) {
					if (e.getClick().isLeftClick()) {
						selectClass(player, clickedClass);
						player.closeInventory();

					} else if (e.getClick().isRightClick()) {
						createDetails(player, clickedClass);
					}
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equals(createItemRandom(gPlayer.getPlayerClass() == null).getItemMeta().getDisplayName())) {

					selectClass(player, null);
					player.closeInventory();
				} else if (e.getCurrentItem().equals(createTutorialItem())) {
					if (!p.getGamePlayer(player).isWatchingTutorial()) {
						playTutorial(player);
					} else {
						player.sendMessage(ChatColor.RED + "Vous assistez déjà à une présentation !");
					}
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
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equals(createUseThisClassItem(null).getItemMeta().getDisplayName())) {
					selectClass(
							player,
							p.getClassManager().getClassFromName(
									e.getInventory().getName().replace(TITLE_CLASS_DETAILS, "")));
					player.closeInventory();
				}

				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.hasItem() && e.getItem().getType() == Material.NETHER_STAR) {
			createSelector(e.getPlayer());
		}
	}

	public void createSelector(Player player) {
		ArrayList<PlayerClass> classes = p.getClassManager().getAvailableClasses();
		Integer inventorySize = (int) (Math.ceil(classes.size() / 9d) * 9) + 27;

		Inventory inv = p.getServer().createInventory(player, inventorySize, TITLE_CLASS_SELECTOR);

		int classesCount = classes.size() + COMING_SOON_CLASSES_COUNT, shift = calculateShiftNeededToCenter(classesCount);

		// Random

		inv.setItem(4, createItemRandom(p.getGamePlayer(player).getPlayerClass() == null));

		// Available classes
		Integer i = 9;
		for (PlayerClass theClass : classes) {
			inv.setItem(
					isOnLastLine(i - 9, classesCount) ? shift + i : i,
					createItem(theClass, p.getGamePlayer(player).getPlayerClass() != null
							&& p.getGamePlayer(player).getPlayerClass().equals(theClass)));

			i++;
		}

		// Placeholder for the other cases
		Integer notYetFinalIndex = i + COMING_SOON_CLASSES_COUNT;
		for (; i < notYetFinalIndex; i++) {
			inv.setItem(isOnLastLine(i - 9, classesCount) ? shift + i : i,
					createItem(new NotYetAvailableClass(p), false));
		}

		inv.setItem(inv.getSize() - 1, createExitItem());
		if (p.getGame().getTutorialLocations() != null) {
			inv.setItem(inv.getSize() - 9, createTutorialItem());
		}

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
		ItemStack item = new ItemStack(theClass.getIcon());
		ItemMeta meta = item.getItemMeta();

		if (isEnabled) {
			meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN + ChatColor.BOLD + "" + theClass.getName());
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
			meta.setLore(Arrays.asList("", ChatColor.GRAY + "Clic droit pour sélectionner", ChatColor.GRAY
					+ "la classe " + theClass.getName()));
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

	public ItemStack createTutorialItem() {
		ItemStack book = new ItemStack(Material.BOOK);

		ItemMeta meta = book.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Voir le Tutoriel");
		meta.setLore(Arrays.asList("", ChatColor.GRAY + "Assistez à un tutoriel interactif !"));
		book.setItemMeta(meta);
		GlowEffect.addGlow(book);

		return book;
	}

	public ItemStack createViewTutorialItem() {
		ItemStack animation = new ItemStack(Material.ITEM_FRAME);
		ItemMeta meta = animation.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Voir le tutoriel animé");
		animation.setItemMeta(meta);
		return animation;
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

	public void playTutorial(final Player player) {
		if (p.getTimer().getSecondsLeft() <= 36) {
			player.sendMessage(ChatColor.RED + "La partie va bientôt commencer !");
			return;
		}
		p.getGamePlayer(player).setWatchingTutorial(true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15), true);
		this.tutorialTask = p.getServer().getScheduler().runTaskTimer(p, new Runnable() {
			private int loop = 0;

			@Override
			public void run() {
				if(!player.isOnline()) {
					return;
				}
				switch (loop) {
				case 0:
					Titles.sendTitle(player, 10, 80, 10, HeroBattle.GAME_NAME_BICOLOR, ChatColor.GOLD
							+ "Comment jouer ?");
					break;
				case 1:
					player.teleport(p.getGame().getTutorialLocations().get(0));
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1L, 2L);
					Titles.sendTitle(player, 10, 80, 10, ChatColor.AQUA + "I. " + ChatColor.GOLD + "Gameplay",
							"Chaque joueur possède une jauge de pourcentage");
					break;
				case 2:
					Titles.sendTitle(player, 0, 80, 0, ChatColor.AQUA + "I. " + ChatColor.GOLD + "Gameplay",
							"Elle définit les dommages du joueur");
					break;
				case 3:
					Titles.sendTitle(player, 0, 80, 0, ChatColor.AQUA + "I. " + ChatColor.GOLD + "Gameplay",
							"Plus il est élevé, plus les dégâts le feront reculer");
					break;
				case 4:
					player.teleport(p.getGame().getTutorialLocations().get(1));
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1L, 2L);
					Titles.sendTitle(player, 10, 80, 0, ChatColor.AQUA + "II. " + ChatColor.GOLD + "But du Jeu",
							"Faites tomber vos adversaires dans le vide ou mettez les K.O.");
					break;
				case 5:
					Titles.sendTitle(player, 0, 80, 0, ChatColor.AQUA + "II. " + ChatColor.GOLD + "But du Jeu",
							"Remportez la partie en étant le dernier en lice");
					break;
				case 6:
					player.teleport(p.getGame().getTutorialLocations().get(2));
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1L, 2L);
					Titles.sendTitle(player, 10, 80, 0, ChatColor.AQUA + "III. " + ChatColor.GOLD + "Classes",
							"Choisissez votre classe au début du jeu");
					break;
				case 7:
					Titles.sendTitle(player, 0, 80, 0, ChatColor.AQUA + "III. " + ChatColor.GOLD + "Classes",
							"Chacune possède ses spécificités");
					break;
				case 8:
					player.teleport(p.getGame().getTutorialLocations().get(3));
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1L, 2L);
					Titles.sendTitle(player, 10, 80, 0, ChatColor.AQUA + "IV. " + ChatColor.GOLD + "Objets Spéciaux",
							"Chaque classe possède des objets différents");
					break;
				case 9:
					Titles.sendTitle(player, 0, 80, 0, ChatColor.AQUA + "IV. " + ChatColor.GOLD + "Objets Spéciaux",
							"Ils permettent d'éxecuter des actions spéciales");
					break;
				case 10:
					Titles.sendTitle(player, 0, 80, 0, ChatColor.AQUA + "IV. " + ChatColor.GOLD + "Objets Spéciaux",
							ChatColor.RED + "Attention" + ChatColor.WHITE
									+ ", ils possèdent un cooldown après chaque utilisation");
					break;
				case 11:
					Titles.sendTitle(player, 10, 80, 10, HeroBattle.GAME_NAME_BICOLOR, ChatColor.GOLD
							+ "Bon jeu et bonne chance !");
					p.getServer().getScheduler().cancelTask(tutorialTask);
					p.getGame().teleportHub(player.getUniqueId());
					p.getGamePlayer(player).setWatchingTutorial(false);
					player.removePotionEffect(PotionEffectType.INVISIBILITY);
					for (Player pl : p.getServer().getOnlinePlayers()) {
						player.showPlayer(pl);
						pl.showPlayer(player);
					}
					break;
				}
				loop++;
			}
		}, 20L, 3 * 20L).getTaskId();

		// Titles.sendTitle(p, 10, 80, 0, HeroBattle.GAME_NAME_BICOLOR,
		// ChatColor.WHITE + "Bienvenue en " + HeroBattle.GAME_NAME);
	}
}
