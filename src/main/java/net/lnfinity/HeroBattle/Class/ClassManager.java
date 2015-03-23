package net.lnfinity.HeroBattle.Class;

import java.util.HashSet;
import java.util.Set;

import net.lnfinity.HeroBattle.HeroBattle;

public class ClassManager {

	private HeroBattle p;
	private Set<PlayerClass> availableClasses = new HashSet<PlayerClass>();

	public ClassManager(HeroBattle plugin) {

		p = plugin;

		// Registers classes
		registerClass(new BruteClass(p));
		registerClass(new GuerrierClass(p));
		registerClass(new MageClass(p));
	}

	/**
	 * Registers a new player class in the game.
	 *
	 * @param playerClass The class.
	 * @return {@code true} if the class was added (i.e. not already registered).
	 */
	public boolean registerClass(PlayerClass playerClass) {
		return availableClasses.add(playerClass);
	}

	/**
	 * Returns the classes currently registered in the game.
	 * @return
	 */
	public Set<PlayerClass> getAvailableClasses() {
		return availableClasses;
	}

	/**
	 * Returns a player class from its name.
	 *
	 * @param name The name of the class.
	 * @return The class; {@code null} if there isn't any class registered with this name.
	 */
	public PlayerClass getClassFromName(String name) {
		for(PlayerClass theClass : availableClasses) {
			if(theClass.getName().equals(name)) {
				return theClass;
			}
		}

		return null;
	}
}
