package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;


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
