package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.tools.*;
import net.lnfinity.HeroBattle.utils.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

import java.util.*;


public class TripleJumpTool extends PlayerTool
{
	private final int COOLDOWN; // seconds
	private final int TRIPLE_JUMP_DURATION;

	public TripleJumpTool(HeroBattle plugin, int cooldown, int tripleJumpDuration)
	{
		super(plugin);

		COOLDOWN = cooldown;
		TRIPLE_JUMP_DURATION = tripleJumpDuration;
	}

	@Override
	public String getToolID()
	{
		return "tool.tripleJump";
	}

	@Override
	public String getName()
	{
		return ChatColor.RED + "" + ChatColor.BOLD + "Triple saut";
	}

	@Override
	public List<String> getDescription()
	{
		return Utils.getToolDescription(ChatColor.GRAY + "Vous permet de faire des triple sauts pendant " + ChatColor.GOLD + 4 + ChatColor.GRAY + " secondes. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + ChatColor.GRAY + " secondes.");
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.FEATHER);
		ToolsUtils.resetTool(item);

		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (ToolsUtils.isToolAvailable(tool))
		{
			final HeroBattlePlayer gPlayer = p.getGamePlayer(player);
			if (gPlayer == null) return;

			gPlayer.setMaxJumps(3, TRIPLE_JUMP_DURATION);

			new ItemCooldown(p, player, this, COOLDOWN);
		}
		else
		{
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}
}
