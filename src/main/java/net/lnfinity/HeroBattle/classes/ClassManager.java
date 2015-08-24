package net.lnfinity.HeroBattle.classes;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.displayers.eastereggs.*;
import net.lnfinity.HeroBattle.classes.displayers.free.*;
import net.lnfinity.HeroBattle.classes.displayers.paid.CryogenieClass;
import net.lnfinity.HeroBattle.classes.displayers.paid.DruideClass;
import net.lnfinity.HeroBattle.classes.displayers.paid.PyrobarbareClass;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import net.lnfinity.HeroBattle.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;


public class ClassManager
{

	private final HeroBattle p;
	private List<PlayerClassType> totalClasses = new ArrayList<>();

	private List<PlayerClass> availableClasses = new ArrayList<>();

	private Set<UUID> dewoitineUnlocks = new HashSet<>();
	private Set<UUID> pommeUnlocks = new HashSet<>();
	private Set<UUID> pikachuUnlocks = new HashSet<>();

	public ClassManager(HeroBattle plugin)
	{

		p = plugin;

		// TODO Merge these registers

		// Registers classes
		registerClass(new BruteClass(p));
		registerClass(new GuerrierClass(p));
		registerClass(new ArcherClass(p));
		registerClass(new MageClass(p));
		registerClass(new MinerClass(p));
		registerClass(new DruideClass(p));
		registerClass(new CryogenieClass(p));
		registerClass(new PyrobarbareClass(p));
	}

	/**
	 * Registers a new player class in the game.
	 *
	 * @param playerClass The class.
	 *
	 * @return {@code true} if the class was added (i.e. not already
	 * registered).
	 */
	public boolean registerClass(PlayerClass playerClass)
	{
		totalClasses.add(playerClass.getType());
		return availableClasses.add(playerClass);
	}

	/**
	 * Returns the classes currently registered in the game.
	 *
	 * @return The registered classes.
	 */
	public List<PlayerClass> getAvailableClasses()
	{
		return availableClasses;
	}

	/**
	 * Returns a player class from its name.
	 *
	 * @param name The name of the class.
	 *
	 * @return The class; {@code null} if there isn't any class registered with
	 * this name.
	 */
	public PlayerClass getClassFromName(Player player, String name)
	{
		HeroBattlePlayer heroBattlePlayer = p.getGamePlayer(player);
		return getClassFromName(heroBattlePlayer, name);
	}

	public PlayerClass getClassFromName(HeroBattlePlayer heroBattlePlayer, String name)
	{
		for (PlayerClass theClass : heroBattlePlayer.getAvailableClasses())
		{
			if (theClass != null && theClass.getName().equalsIgnoreCase(name))
			{
				return theClass;
			}
		}

		for (PlayerClass theClass : this.availableClasses)
		{
			if (theClass != null && theClass.getName().equalsIgnoreCase(name))
			{
				return theClass;
			}
		}

		return null;
	}

	public void loadPlayerClasses(final Player player)
	{
		// TODO Warning, this may cause problems if the request is lost (somehow)
		final HeroBattlePlayer heroBattlePlayer = p.getGamePlayer(player);

		final String unlockedKey = ".unlocked";
		final String cooldownKey = ".cooldown";
		final String powerKey = ".power";
		final String toolsKey = ".tools";

		p.getServer().getScheduler().runTaskAsynchronously(p, () ->
		{
			for (PlayerClass clazz : availableClasses)
			{
				final String className = clazz.getType().toString().toLowerCase();


				String data = p.getShopManager().getItemLevelForPlayer(player.getUniqueId(), className + unlockedKey);

				if ((data != null && data.equals("1")) || clazz.getType().getPrice() == PlayerClassPrice.FREE)
				{
					try
					{
						String cooldownUnlock = Utils.toStringIfNotEmpty(p.getShopManager().getItemLevelForPlayer(player.getUniqueId(), className + cooldownKey), "0");
						String powerUnlock = Utils.toStringIfNotEmpty(p.getShopManager().getItemLevelForPlayer(player.getUniqueId(), className + powerKey), "0");
						String toolsUnlock = Utils.toStringIfNotEmpty(p.getShopManager().getItemLevelForPlayer(player.getUniqueId(), className + toolsKey), "0");

						heroBattlePlayer.addAvailableClass(constructPlayerClass(clazz.getType(), Integer.parseInt(cooldownUnlock), Integer.parseInt(powerUnlock), Integer.parseInt(toolsUnlock)));
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
		});
	}

	private PlayerClass constructPlayerClass(PlayerClassType type, int cooldown, int power, int tools)
	{
		switch (type)
		{
			case BRUTE:
				return new BruteClass(p, cooldown, power, tools);
			case GUERRIER:
				return new GuerrierClass(p, cooldown, power, tools);
			case ARCHER:
				return new ArcherClass(p, cooldown, power, tools);
			case MAGE:
				return new MageClass(p, cooldown, power, tools);
			case MINEUR:
				return new MinerClass(p, cooldown, power, tools);
			case DRUIDE:
				return new DruideClass(p, cooldown, power, tools);
			case PYROBARBARE:
				return new PyrobarbareClass(p, cooldown, power, tools);
			case CRYOGENIE:
				return new CryogenieClass(p, cooldown, power, tools);
			default:
				return new BruteClass(p, cooldown, power, tools);
		}
	}

	public boolean playerHasClass(HeroBattlePlayer heroBattlePlayer, PlayerClassType type)
	{
		if(heroBattlePlayer == null || heroBattlePlayer.getAvailableClasses() == null) return false;

		return heroBattlePlayer.getAvailableClasses().stream()
				.filter(clazz -> clazz != null && clazz.getType() == type)
				.findFirst()
				.isPresent();
	}

	public void setPlayerClass(Player player, PlayerClass theClass, boolean notify)
	{
		if (player == null) return;

		p.getGamePlayer(player).setPlayerClass(theClass);


		if (notify)
		{
			if (theClass != null)
			{
				player.sendMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Vous avez choisi la classe "
						+ ChatColor.DARK_GREEN + theClass.getName() + ChatColor.GREEN + " !");
			}
			else
			{
				player.sendMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Vous avez choisi une classe "
						+ ChatColor.DARK_GREEN + "aléatoire" + ChatColor.GREEN + " !");
			}
		}
	}

	public PlayerClass getAnyClassByFriendlyName(String friendlyName, HeroBattlePlayer target)
	{
		switch (friendlyName.toLowerCase())
		{
			case "maite":
			case "maïte":
			case "maité":
			case "maïté":
				return new MaiteClass(p);

			case "dewoitine":
				return new DewoitineClass(p, 0, 0, 0);

			case "dewoitined550":
				return new DewoitineD550Class(p, 0, 0, 0);

			case "pooomme":
				return new PommeClass();

			case "pikachu":
				return new PikachuClass();

			default:
				PlayerClass playerClass = p.getClassManager().getClassFromName(target, friendlyName);

				if (playerClass != null)
				{
					return playerClass;
				}

				return null;
		}
	}


	public Set<UUID> getDewoitineUnlocks()
	{
		return dewoitineUnlocks;
	}

	public Set<UUID> getPommeUnlocks()
	{
		return pommeUnlocks;
	}

	public Set<UUID> getPikachuUnlocks()
	{
		return pikachuUnlocks;
	}
}
