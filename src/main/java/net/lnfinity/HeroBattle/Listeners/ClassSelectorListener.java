package net.lnfinity.HeroBattle.Listeners;

import java.util.ArrayList;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Class.BruteClass;
import net.lnfinity.HeroBattle.Utils.GlowEffect;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClassSelectorListener implements Listener {

	private HeroBattle p;

	public ClassSelectorListener(HeroBattle plugin) {
		p = plugin;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			Player player = (Player) e.getWhoClicked();
			if (e.getInventory().getName().equals("Sélection de la classe")) {
				if (e.getClick().isLeftClick()) {

					player.closeInventory();
				} else if (e.getClick().isRightClick()) {
					switch (e.getSlot()) {
					case 0:
						createDetails(player, new BruteClass());
						break;
					}
				}
				e.setCancelled(true);
			} else if (e.getInventory().getName().startsWith("Détails de la classe ")) {
				if (e.getSlot() == 8) {
					createSelector(player);
				}
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getItem().getType() == Material.NETHER_STAR) {
			createSelector(e.getPlayer());
		}
	}

	public void createSelector(Player player) {
		Inventory inv = p.getServer().createInventory(player, 9, "Sélection de la classe");
		inv.addItem(createItem(Material.DIAMOND_CHESTPLATE, "Brute", "Pour le plaisir de faire des dégâts.", true));
		inv.addItem(createItem(Material.IRON_SWORD, "Vaillant guerrier", "Réservé aux connaisseurs.", false));
		inv.addItem(createItem(Material.STONE_AXE, "Bûcheron sournois", "Surveillez vos arrières.", false));
		inv.addItem(createItem(Material.GOLD_HOE, "Faucheur maudit", "Il a tendance à disparaître la nuit...", false));
		inv.addItem(createItem(Material.FISHING_ROD, "Pêcheur adroit", "Ne le sous-estimez pas !", false));
		inv.addItem(createItem(Material.IRON_PICKAXE, "Mineur fou", "Le déranger pourrait l'enrager.", false));
		inv.addItem(createItem(Material.FLINT_AND_STEEL, "Pyrobarbare", "Le feu, il maitrise. Ou pas.", false));
		inv.setItem(7, createItem(Material.BARRIER, "???", "Bientôt disponible", false));
		inv.setItem(8, createItem(Material.BARRIER, "???", "Bientôt disponible", false));

		player.openInventory(inv);
	}

	public void createDetails(Player player, BruteClass classe) {
		Inventory inv = p.getServer().createInventory(player, 9, "Détails de la classe " + classe.getName());
		for (int i = 0; i <= 8; i++) {
			if (classe.getItem(i) != null) {
				inv.setItem(i, classe.getItem(i));
			}
		}
		ItemStack item = new ItemStack(Material.MAGMA_CREAM);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + classe.getName());
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("");

		lore.add(getBar("Vies", classe.getHearths(), 6));
		lore.add(getBar("Dégâts min.", classe.getMinDamages(), 6));
		lore.add(getBar("Dégâts max.", classe.getMaxDamages() - 6, 6));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(7, item);

		item = new ItemStack(Material.WOOD_DOOR);
		meta = item.getItemMeta();
		ArrayList<String> loreLine = new ArrayList<String>();
		loreLine.add(0, ChatColor.GRAY + "Clic droit pour revenir");
		meta.setLore(loreLine);
		meta.setDisplayName(ChatColor.RESET + "Revenir au choix des classes");
		item.setItemMeta(meta);
		inv.setItem(8, item);
		player.openInventory(inv);
	}

	public ItemStack createItem(Material material, String name, String desc, boolean glow) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		if (glow) {
			meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN + ChatColor.BOLD + "" + name);
		} else {
			meta.setDisplayName(ChatColor.RESET + name);
		}
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + desc);
		lore.add("");
		lore.add(ChatColor.GRAY + "• Clic gauche pour jouer avec cette classe");
		lore.add(ChatColor.GRAY + "• Clic droit pour voir ses caractéristiques");
		meta.setLore(lore);
		item.setItemMeta(meta);
		if (glow) {
			GlowEffect.addGlow(item);
		}
		return item;
	}

	public String getBar(String name, int qty, int total) {
		String str = ChatColor.RED + "" + ChatColor.BOLD;
		for (int i = 1; i <= total; i++) {
			str += "||";
			if (qty == i) {
				str += ChatColor.GRAY + "" + ChatColor.BOLD;
			}
		}
		str += ChatColor.RESET + "" + ChatColor.GRAY + " - " + name;
		return str;
	}

}
