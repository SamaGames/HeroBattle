package net.lnfinity.HeroBattle.Class;

import java.util.HashSet;
import java.util.Set;

public class ClassManager {

	private Set<PlayerClass> availableClasses = new HashSet<PlayerClass>();

	public ClassManager() {
		// Registers classes
		registerClass(new BruteClass());
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
}
