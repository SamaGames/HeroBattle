package net.lnfinity.HeroBattle.classes;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public abstract class PlayerClass {

	protected HeroBattle p;
	protected List<PlayerTool> tools = new ArrayList<PlayerTool>();

	protected List<String> detailsLore = null;


	public PlayerClass(HeroBattle plugin) {
		p = plugin;
	}
	
	/**
	 * Returns the name of the class.
	 *
	 * @return The name.
	 */
	public abstract String getName();

	/**
	 * Returns the icon of the class: an ItemStack displayed in the classes selector GUI.
	 *
	 * <p>
	 *     The display name of this item stack is ignored.
	 * </p>
	 *
	 * @return The icon, as an ItemStack.
	 */
	public abstract ItemStack getIcon();

	/**
	 * Returns the hat the player will have.
	 *
	 * <p>
	 *     The display name and the lore of this item stack are ignored and will be overwritten.
	 * </p>
	 *
	 * @return The hat.
	 */
	public abstract ItemStack getHat();

	/**
	 * Returns the description of this class, displayed as a lore in the selector.
	 *
	 * @return A list of lines displayed in the lore.
	 */
	public abstract List<String> getDescription();

	/**
	 * Returns the minimum damages a player with this class can inflate.
	 *
	 * @return The minimum damages.
	 */
	public abstract int getMinDamages();

	/**
	 * Returns the maximum damages a player with this class can inflate.
	 *
	 * @return The maximum damages.
	 */
	public abstract int getMaxDamages();

	/**
	 * Returns the maximum damages a player can bear.
	 *
	 * @return The maximum damages a player can bear.
	 */
	public abstract int getMaxResistance();

	/**
	 * Returns the lives a player with this class have.
	 *
	 * @return The lives.
	 */
	public abstract int getLives();

	/**
	 * Returns this class' type in the PlayerClassType enum.
	 *
	 * @return The type.
	 */
	public abstract PlayerClassType getType();




	/**
	 * Returns a list of the tools in this class.
	 *
	 * @return The list.
	 */
	public List<PlayerTool> getTools() {
		return tools;
	}
	
	/**
	 * Returns the tool of the slot-th slot of the player.
	 *
	 * @param slot The slot.
	 *
	 * @return The tool in this slot.
	 */
	public PlayerTool getTool(int slot) {
		if(slot >= tools.size() || slot < 0) return null;
		return tools.get(slot);
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


	/**
	 * Returns the class' details as a list ready to be used somewhere in a lore.
	 *
	 * @return The details.
	 */
	public List<String> getClassDetailsLore() {

		if(detailsLore == null) {

			detailsLore = new ArrayList<>();

			String lives = "";
			for(int k = 0; k < getLives(); k++) {
				lives += "❤";
			}

			detailsLore.add(ChatColor.GRAY + "Total des vies : " + ChatColor.RED + lives);
			detailsLore.add("");

			detailsLore.add(ChatColor.AQUA + "Arme principale");
			detailsLore.add(ChatColor.GRAY + "Dégâts : de " + ChatColor.GOLD + getMinDamages() + ChatColor.GRAY + " à " + ChatColor.GOLD + getMaxDamages() + ChatColor.GRAY + "%");
			detailsLore.add("");

			detailsLore.add(ChatColor.AQUA + "Armure");
			detailsLore.add(ChatColor.GRAY + "Résistance maximale : " + ChatColor.RED + getMaxResistance() + ChatColor.GRAY + "%");
			detailsLore.add("");

			detailsLore.add(ChatColor.AQUA + "Améliorations boutique");
			detailsLore.add(ChatColor.GRAY + "Cooldowns : " + ChatColor.GOLD + "0" + ChatColor.GRAY + "/" + ChatColor.DARK_GRAY + "5");
			detailsLore.add(ChatColor.GRAY + "Puissance des capacités : " + ChatColor.GOLD + "0" + ChatColor.GRAY + "/" + ChatColor.DARK_GRAY + "5");
			detailsLore.add(ChatColor.GRAY + "Nouvelles capacités : " + ChatColor.GOLD + "0" + ChatColor.GRAY + "/" + ChatColor.DARK_GRAY + "2");
		}


		return detailsLore;
	}

	/**
	 * Sets the team for this class to the given player.
	 *
	 * To have a goodly formatted name in the tab.
	 *
	 * @param player The player.
	 */
	public void setClassTeam(Player player) {
		setNamedClassTeam(player, getName());
	}

	/**
	 * Sets the team for a random class to the given player.
	 *
	 * To have a goodly formatted name in the tab.
	 *
	 * @param player The player.
	 */
	public static void setRandomClassTeam(Player player) {
		setNamedClassTeam(player, "Aléatoire");
	}

	/**
	 * Sets the team for a class named “className” to the given player.
	 *
	 * To have a goodly formatted name in the tab.
	 *
	 * @param player The player.
	 */
	private static void setNamedClassTeam(Player player, String className) {

		Team oldTeam = HeroBattle.getInstance().getScoreboardManager().getScoreboard().getPlayerTeam(player);
		if(oldTeam != null) {
			oldTeam.unregister();
		}


		String playerColor = Utils.getPlayerColor(player);

		Team classTeam = HeroBattle.getInstance().getScoreboardManager().getScoreboard().registerNewTeam(Utils.getRandomAvailableTeamName());

		classTeam.setDisplayName(playerColor + player.getName() + ChatColor.GRAY + " \u2042 " + ChatColor.RESET + className);
		classTeam.setSuffix(ChatColor.GRAY + " \u2042 " + className);
		classTeam.setPrefix(playerColor);

		classTeam.setCanSeeFriendlyInvisibles(false);
		classTeam.setAllowFriendlyFire(true);

		classTeam.addPlayer(player);
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
