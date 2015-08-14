package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.tools.*;
import net.lnfinity.HeroBattle.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;

import java.util.*;


public class MinerSpecialTool extends PlayerTool
{

	private final int COOLDOWN;
	private final int DURATION;

	public MinerSpecialTool(HeroBattle plugin, int cooldown, int duration)
	{
		super(plugin);

		COOLDOWN = cooldown;
		DURATION = duration;
	}

	@Override
	public String getToolID()
	{
		return "tool.minerspecial";
	}

	@Override
	public String getName()
	{
		return ChatColor.GREEN + "Chant du mineur";
	}

	@Override
	public List<String> getDescription()
	{
		return Utils.getToolDescription(ChatColor.GRAY + "Le mineur chante tel qu'il le fait à la mine, augmentant ainsi son efficacité. Il court plus vite et frappe plus fort pendant " + ChatColor.GOLD + DURATION + " " + ChatColor.GRAY + "secondes. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem()
	{
		final ItemStack item = new ItemStack(Material.INK_SACK, 1, DyeColor.CYAN.getData());
		ToolsUtils.resetTool(item);

		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (ToolsUtils.isToolAvailable(tool))
		{
			new ItemCooldown(p, player, this, COOLDOWN);
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1.5F);

			GamePlayer gamePlayer = p.getGamePlayer(player);
			gamePlayer.addRemainingDoubleDamages(DURATION);
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, DURATION * 20, 0));


		}
		else
		{
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		onRightClick(player, tool, event);
	}

}
