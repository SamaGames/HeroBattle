package net.lnfinity.HeroBattle.Tools;

/**
 * An enum to store the IDs of the tools (better access).
 */
public enum Tool {

	SWORD("tool.sword"),
	SPEED("tool.speed"),
	POWER("tool.power");

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
