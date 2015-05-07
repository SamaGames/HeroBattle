package net.lnfinity.HeroBattle.classes;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.displayers.*;
import net.lnfinity.HeroBattle.game.GamePlayer;
import net.zyuiop.MasterBundle.FastJedis;
import net.zyuiop.MasterBundle.MasterBundle;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import redis.clients.jedis.ShardedJedis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClassManager {

	private final HeroBattle p;
	private List<PlayerClassType> totalClasses = new ArrayList<PlayerClassType>();

	private List<PlayerClass> availableClasses = new ArrayList<PlayerClass>();

	public ClassManager(HeroBattle plugin) {

		p = plugin;

		// TODO Merge these registers
		
		// Registers classes
		registerClass(new BruteClass(p));
		registerClass(new GuerrierClass(p));
		registerClass(new ArcherClass(p));
		registerClass(new MageClass(p));
		registerClass(new DruideClass(p));
		registerClass(new CryogenieClass(p));
		registerClass(new PyrobarbareClass(p));

		totalClasses.add(PlayerClassType.BRUTE);
		totalClasses.add(PlayerClassType.GUERRIER);
		totalClasses.add(PlayerClassType.ARCHER);
		totalClasses.add(PlayerClassType.MAGE);
		totalClasses.add(PlayerClassType.DRUIDE);
		totalClasses.add(PlayerClassType.CRYOGENIE);
		totalClasses.add(PlayerClassType.PYROBARBARE);

	}

	/**
	 * Registers a new player class in the game.
	 * 
	 * @param playerClass
	 *            The class.
	 * @return {@code true} if the class was added (i.e. not already
	 *         registered).
	 */
	public boolean registerClass(PlayerClass playerClass) {
		return availableClasses.add(playerClass);
	}

	/**
	 * Returns the classes currently registered in the game.
	 * 
	 * @return
	 */
	public List<PlayerClass> getAvailableClasses() {
		return availableClasses;
	}

	/**
	 * Returns a player class from its name.
	 * 
	 * @param name
	 *            The name of the class.
	 * @return The class; {@code null} if there isn't any class registered with
	 *         this name.
	 */
	public PlayerClass getClassFromName(Player player, String name) {
		GamePlayer gamePlayer = p.getGamePlayer(player);
		for (PlayerClass theClass : gamePlayer.getAvaibleClasses()) {
			if (theClass != null && theClass.getName().equals(name)) {
				return theClass;
			}
		}
		for (PlayerClass theClass : this.availableClasses) {
			if (theClass != null && theClass.getName().equals(name)) {
				return theClass;
			}
		}
		return null;
	}

	public void addPlayerClasses(final Player player) {
		// TODO Warning, this may cause problems if the request is lost (somehow)
		final GamePlayer gamePlayer = p.getGamePlayer(player);
		final String prefix = "shops:" + HeroBattle.GAME_NAME_WHITE + ":";
		final String sufix = ":" + player.getUniqueId();
		final String currentStr = ":current";
		final String has = ".has";
		final String cooldown = ".cooldown";
		final String power = ".power";
		final String tools = ".tools";
		
		for(PlayerClass theClass : availableClasses) {
			final String className = theClass.getType().toString().toLowerCase();
			final PlayerClass current = theClass;
			p.getServer().getScheduler().runTaskAsynchronously(p, new Runnable() {
				@Override
				public void run() {
					if (MasterBundle.isDbEnabled) {
						String data = FastJedis.get(prefix + className + has + sufix);
						if((data != null && data.equals("1")) || className.equals("brute") || className.equals("guerrier") || className.equals("archer") || className.equals("mage")) {
							try {
							String A = FastJedis.get(prefix + className + cooldown + sufix + currentStr);
							if(A == null || A.equals("")) {
								A = "0";
							}
							String B = FastJedis.get(prefix + className + power + sufix + currentStr);
							if(B == null || B.equals("")) {
								B = "0";
							}
							String C = FastJedis.get(prefix + className + tools + sufix + currentStr);
							if(C == null || C.equals("")) {
								C = "0";
							}
							gamePlayer.addAvaibleClass(constructPlayerClass(current.getType(), Integer.parseInt(A), Integer.parseInt(B), Integer.parseInt(C)));
							} catch(Exception ex) {
								ex.printStackTrace();
							}
						} else {
							// Player doesn't have that class !
						}
					} else {
					// Default
					gamePlayer.addAvaibleClass(new BruteClass(p, 0, 1, 0));
					gamePlayer.addAvaibleClass(new GuerrierClass(p, 0, 0, 0));
					gamePlayer.addAvaibleClass(new ArcherClass(p, 0, 0, 0));
					gamePlayer.addAvaibleClass(new MageClass(p, 0, 0, 0));
					}
				}
			});
		}
	}

	private PlayerClass constructPlayerClass(PlayerClassType type, int arg1, int arg2, int arg3) {
		switch (type) {
		case BRUTE:
			return new BruteClass(p, arg1, 1, arg3);
		case GUERRIER:
			return new GuerrierClass(p, arg1, arg2, arg3);
		case ARCHER:
			return new ArcherClass(p, arg1, arg2, arg3);
		case MAGE:
			return new MageClass(p, arg1, arg2, arg3);
		case DRUIDE:
			return new DruideClass(p, arg1, arg2, arg3);
		case CRYOGENIE:
			return new CryogenieClass(p, arg1, arg2, arg3);
		case PYROBARBARE:
			return new PyrobarbareClass(p, arg1, arg2, arg3);
		default:
			return new BruteClass(p, arg1, arg2, arg3);
		}
	}

	public PlayerClassType getPlayerClassType(String type) {
		return PlayerClassType.valueOf(type.toUpperCase());
	}

	public List<PlayerClassType> getTotalClasses() {
		return totalClasses;
	}

	public boolean playerHasClass(GamePlayer gamePlayer, PlayerClassType type) {
		if (gamePlayer != null) {
			for (int i = 0; i < gamePlayer.getAvaibleClasses().size(); ++i) {
				if(gamePlayer.getAvaibleClasses() != null && gamePlayer.getAvaibleClasses().get(i) != null && gamePlayer.getAvaibleClasses().get(i).getType() == type) {
					return true;
				}
			}
		}
		return false;
	}
}
