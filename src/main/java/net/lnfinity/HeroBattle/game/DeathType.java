package net.lnfinity.HeroBattle.game;

public enum DeathType {

	/**
	 * The player logged out during the game.
	 */
	QUIT,

	/**
	 * The player fall under the bottom limit.
	 */
	FALL,

	/**
	 * The player's percentage was higher or equal to his
	 * class' maximum percentage.
	 */
	KO,

	/**
	 * The player entered into a block of water.
	 *
	 * Only if enabled in the map's configuration file.
	 */
	WATER,

	/**
	 * The player entered into a block of lava.
	 *
	 * Only if enabled in the map's configuration file.
	 */
	LAVA
}
