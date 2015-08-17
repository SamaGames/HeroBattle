package net.lnfinity.HeroBattle.game;

import org.bukkit.*;


public enum DeathType {

	/**
	 * The player logged out during the game.
	 */
	QUIT
			(
					"{player} a quitté la partie",
					"{player} a fuit la partie devant la supprématie de {aggressor}",
					null,
					null,
					"Un fuyard !"
			),

	/**
	 * The player fell under the bottom limit.
	 */
	FALL
			(
					"{player} est tombé dans le vide",
					"{player} a été poussé dans le vide par {aggressor}",
					"Vous êtes tombé dans le vide !",
					"Vous avez été poussé par {aggressor} !",
					"Un joueur poussé !"
			),

	/**
	 * The player's percentage was higher or equal to his
	 * class' maximum percentage.
	 */
	KO
			(
					"{player} est K.O. !",
					"{player} a été mis K.O. par {aggressor} !",
					"{player} s'est suicidé !",
					"Vous êtes K.O. !",
					"{aggressor} vous a mis K.O. !",
					"Vous vous êtes suicidé !",
					"Un joueur K.O. !"
			),

	/**
	 * The player entered into a block of water.
	 *
	 * Only if enabled in the map's configuration file.
	 */
	WATER
			(
					"{player} est tombé à l'eau !",
					"{player} a été poussé dans l'eau par {aggressor} !",
					"Vous êtes tombé à l'eau !",
					"{aggressor} vous a poussé dans l'eau !",
					"Un joueur mouillé !"
			),

	/**
	 * The player entered into a block of lava.
	 *
	 * Only if enabled in the map's configuration file.
	 */
	LAVA
			(
					"{player} s'est consummé dans la lave !",
					"{player} a été poussé dans la lave par {aggressor} !",
					"Vous vous êtes consummé !",
					"{aggressor} vous a poussé dans la lave !",
					"Un joueur brûlé !"
			);


	private final ChatColor baseChatColor = ChatColor.YELLOW;
	private final ChatColor baseTitleColor = ChatColor.RED;

	private final String messageSoloDeath;
	private final String messageCausedDeath;
	private final String messageSuicide;
	private final String titleSoloDeath;
	private final String titleCausedDeath;
	private final String titleSuicide;
	private final String coinsMessage;


	/**
	 * Messages can use these tags: - {@code {player}}: the player who just loose a life; - {@code
	 * {aggressor}}: the player who caused the death. Only in messageCausedDeath.
	 *
	 * No tags in the coins message.
	 *
	 * @param messageSoloDeath   The message displayed when a player loose a life with this death
	 *                           type by himself.
	 * @param messageCausedDeath The message displayed when a player loose a life with this death
	 *                           type helped by another player.
	 * @param messageSuicide     The message displayed when a player commits suicide.
	 * @param titleSoloDeath     The title displayed to the player when he loose a life by himself.
	 * @param titleCausedDeath   The title displayed to the player when he loose a life helped by
	 *                           someone.
	 * @param titleSuicide       The title displayed when a player commits suicide.
	 * @param coinsMessage       The reason of the coins rewards for the killer.
	 */
	DeathType(String messageSoloDeath, String messageCausedDeath, String messageSuicide, String titleSoloDeath, String titleCausedDeath, String titleSuicide, String coinsMessage)
	{
		this.messageSoloDeath = messageSoloDeath;
		this.messageCausedDeath = messageCausedDeath;
		this.messageSuicide = messageSuicide;
		this.titleSoloDeath = titleSoloDeath;
		this.titleCausedDeath = titleCausedDeath;
		this.titleSuicide = titleSuicide;
		this.coinsMessage = coinsMessage;
	}

	/**
	 * Messages can use these tags (when applicable): - {@code {player}}: the player who just loose
	 * a life; - {@code {aggressor}}: the player who caused the death.
	 *
	 * No tags in the coins message.
	 *
	 * @param messageSoloDeath   The message displayed when a player loose a life with this death
	 *                           type by himself.
	 * @param messageCausedDeath The message displayed when a player loose a life with this death
	 *                           type helped by another player.
	 * @param titleSoloDeath     The title displayed to the player when he loose a life by himself.
	 * @param titleCausedDeath   The title displayed to the player when he loose a life helped by
	 *                           someone.
	 * @param coinsMessage       The reason of the coins rewards for the killer.
	 */
	DeathType(String messageSoloDeath, String messageCausedDeath, String titleSoloDeath, String titleCausedDeath, String coinsMessage)
	{
		this(messageSoloDeath, messageCausedDeath, null, titleSoloDeath, titleCausedDeath, null, coinsMessage);
	}


	public String getMessageSoloDeath(String deadPlayerName)
	{
		if (messageSoloDeath == null) return "";

		return baseChatColor + replace(messageSoloDeath, "player", ChatColor.RED + deadPlayerName, baseChatColor);
	}

	public String getMessageCausedDeath(String deadPlayerName, String aggressorName)
	{
		if (messageCausedDeath == null) return "";

		String message = baseChatColor + replace(messageCausedDeath, "player", ChatColor.RED + deadPlayerName, baseChatColor);
		message = replace(message, "aggressor", ChatColor.GREEN + aggressorName, baseChatColor);

		return message;
	}

	public String getMessageSuicide(String deadPlayerName)
	{
		if (messageSuicide == null) return getMessageSoloDeath(deadPlayerName);

		return baseChatColor + replace(messageSuicide, "player", ChatColor.RED + deadPlayerName, baseChatColor);
	}


	public String getTitleSoloDeath()
	{
		if (titleSoloDeath == null) return "";

		return baseTitleColor + titleSoloDeath;
	}

	public String getTitleCausedDeath(String aggressorName)
	{
		if (titleCausedDeath == null) return "";

		return baseTitleColor + replace(titleCausedDeath, "aggressor", aggressorName, baseTitleColor);
	}

	public String getTitleSuicide()
	{
		if (titleSuicide == null) return getTitleSoloDeath();

		return baseTitleColor + titleSuicide;
	}


	public String getCoinsMessage()
	{
		return coinsMessage != null ? coinsMessage : "";
	}


	/**
	 * Replaces the {key} by the given value, keeping the base color of the string.
	 *
	 * @param original The original text.
	 * @param key      The key. Will be searched as {key} in the text.
	 * @param value    The value.
	 *
	 * @return The new string.
	 */
	private String replace(final String original, final String key, final String value, final ChatColor baseColor)
	{
		return original.replace("{" + key + "}", value + baseColor);
	}
}
