package net.lnfinity.HeroBattle.Tools;

/**
 * An enum to store the IDs of the tools (better access).
 */
public enum Tool {

	SWORD("tool.sword"),
	SPEED("tool.speed"),
	POWER("tool.power"),
	EARTHQUAKE("tool.earthquake"),
	INK("tool.ink"),
	NAUSEA("tool.nausea"),
	THUNDER("tool.thunder"),
	SMOKE("tool.smoke");

	private final String toolID;

	Tool(String toolID) {
		this.toolID = toolID;
	}

	public String getId() {
		return toolID;
	}

	@Override
	public String toString() {
		return toolID;
	}
}
