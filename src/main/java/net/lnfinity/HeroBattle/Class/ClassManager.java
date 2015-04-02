package net.lnfinity.HeroBattle.Class;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Game.GamePlayer;
import net.zyuiop.MasterBundle.MasterBundle;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClassManager {

	private HeroBattle p;
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

		totalClasses.add(PlayerClassType.BRUTE);
		totalClasses.add(PlayerClassType.GUERRIER);
		totalClasses.add(PlayerClassType.ARCHER);
		totalClasses.add(PlayerClassType.MAGE);
		totalClasses.add(PlayerClassType.DRUIDE);
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
			if (theClass.getName().equals(name)) {
				return theClass;
			}
		}
		for (PlayerClass theClass : this.availableClasses) {
			if (theClass.getName().equals(name)) {
				return theClass;
			}
		}
		return null;
	}

	public void addPlayerClasses(final Player player) {
		// TODO Warning, this may cause problems if the request is lost (somehow)
		p.getServer().getScheduler().runTaskAsynchronously(p, new Runnable() {
			private final String DEF = "{\"brute\":[\"0\",\"0\",\"0\"],\"guerrier\":[\"0\",\"0\",\"0\"],\"archer\":[\"0\",\"0\",\"0\"],\"mage\":[\"0\",\"0\",\"0\"]}";
			@Override
			public void run() {
				GamePlayer gamePlayer = p.getGamePlayer(player);
				String json;
				if (MasterBundle.isDbEnabled) {
					json = MasterBundle.jedis().hget("herobattle:playerdatas", player.getUniqueId().toString());
					if(json == null || json == "" || json == "0") {
						MasterBundle.jedis().set("herobattle:playerdatas", DEF);
					}
				} else {
					// Default
					json = DEF;
				}

				JSONParser parser = new JSONParser();
				try {
					JSONObject obj = (JSONObject) parser.parse(json);
					for (int i = 0; i < totalClasses.size(); i++) {
						JSONArray values = (JSONArray) obj.get(totalClasses.get(i).getId().toLowerCase());
						if (values != null) {
							Iterator<String> iterator = values.iterator();
							int[] vals = { 0, 0, 0 };
							int k = 0;
							while (iterator.hasNext()) {
								vals[k] = Integer.parseInt(iterator.next());
								k++;
							}
							gamePlayer.addAvaibleClass(constructPlayerClass(getPlayerClassType(totalClasses.get(i).getId()),
									vals[0], vals[1], vals[2]));
						}
					}

				} catch (ParseException ex) {
					p.getLogger().warning("Can't decode Json data from player " + player.getUniqueId().toString());
					ex.printStackTrace();
				}
			}
		});
	}

	private PlayerClass constructPlayerClass(PlayerClassType type, int arg1, int arg2, int arg3) {
		switch (type) {
		case BRUTE:
			return new BruteClass(p, arg1, arg2, arg3);
		case GUERRIER:
			return new GuerrierClass(p, arg1, arg2, arg3);
		case ARCHER:
			return new ArcherClass(p, arg1, arg2, arg3);
		case MAGE:
			return new MageClass(p, arg1, arg2, arg3);
		case DRUIDE:
			return new DruideClass(p, arg1, arg2, arg3);
		}
		return null;
	}

	public PlayerClassType getPlayerClassType(String type) {
		return PlayerClassType.valueOf(type.toUpperCase());
	}

	public List<PlayerClassType> getTotalClasses() {
		return totalClasses;
	}

	public boolean playerHasClass(GamePlayer gamePlayer, PlayerClassType type) {
		for (int i = 0; i < gamePlayer.getAvaibleClasses().size(); ++i) {
			if (gamePlayer.getAvaibleClasses() != null && gamePlayer.getAvaibleClasses().get(i).getType() == type) {
				return true;
			}
		}
		return false;
	}
}
