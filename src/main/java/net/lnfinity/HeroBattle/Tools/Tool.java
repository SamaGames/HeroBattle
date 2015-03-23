package net.lnfinity.HeroBattle.Tools;

/**
 * An enum to store the IDs of the tools (better access).
 */
public enum Tool {

	SWORD("tool.sword"),
	SWORD_VARIANT1("tool.sword.variant1"),
	SWORD_VARIANT2("tool.sword.variant2"),
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