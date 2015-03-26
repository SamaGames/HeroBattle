package net.lnfinity.HeroBattle.Tools;


import net.lnfinity.HeroBattle.HeroBattle;
import org.apache.commons.lang.Validate;

import java.util.HashSet;
import java.util.Set;

public class ToolsManager {

	private HeroBattle p;
	private Set<PlayerTool> toolsAvailable = new HashSet<PlayerTool>();


	public ToolsManager(HeroBattle plugin) {
		p = plugin;

		// Registers the tools
		registerTool(new SwordTool(p));
		registerTool(new SwordVariant1Tool(p));
		registerTool(new SwordVariant2Tool(p));
		registerTool(new SwordVariant3Tool(p));
		registerTool(new SpeedTool(p));
		registerTool(new PowerTool(p));
		registerTool(new EarthquakeTool(p));
		registerTool(new InkTool(p));
		registerTool(new NauseaTool(p));
		registerTool(new ThunderTool(p));
		registerTool(new SmokeTool(p));
		registerTool(new ArrowsTool(p));
	}

	/**
	 * Registers a new player tool in the game.
	 *
	 * @param tool The tool.
	 * @return {@code true} if the tool was added (i.e. not already registered).
	 */
	public boolean registerTool(PlayerTool tool) {
		Validate.notNull(tool, "The tool cannot be null!");

		return toolsAvailable.add(tool);
	}

	/**
	 * Returns the tools registered in the game.
	 *
	 * @return The tools.
	 */
	public Set<PlayerTool> getToolsAvailable() {
		return toolsAvailable;
	}

	/**
	 * Attempts to get a tool from its name.
	 *
	 * <p>Notice: the name includes the formatting.</p>
	 *
	 * @param name The name of the tool.
	 *
	 * @return The tool, if found; {@code null}, else.
	 */
	public PlayerTool getToolByName(String name) {
		for(PlayerTool tool : toolsAvailable) {
			if(tool.getName().equals(name)) {
				return tool;
			}
		}

		return null;
	}

	/**
	 * Attempts to get a tool from its identifier.
	 *
	 * @param id The ID of the tool.
	 *
	 * @return The tool, if found; {@code null}, else.
	 */
	public PlayerTool getToolByID(String id) {
		for(PlayerTool tool : toolsAvailable) {
			if(tool.getToolID().equals(id)) {
				return tool;
			}
		}

		return null;
	}

	/**
	 * Attempts to get a tool from its Tool' enum counterpart.
	 *
	 * @param tool The tool in the enum.
	 *
	 * @return The tool, if found; {@code null}, else.
	 */
	public PlayerTool getTool(Tool tool) {
		for(PlayerTool theTool : toolsAvailable) {
			if(theTool.getToolID().equals(tool.getId())) {
				return theTool;
			}
		}

		return null;
	}
}
