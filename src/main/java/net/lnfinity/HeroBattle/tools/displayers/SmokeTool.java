package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.tools.*;
import net.lnfinity.HeroBattle.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;

import java.util.*;


public class SmokeTool extends PlayerTool
{

	private final int COOLDOWN;
	private final int EFFECT_DURATION;

	public SmokeTool(HeroBattle p, int cooldown, int duration)
	{
		super(p);
		COOLDOWN = cooldown;
		EFFECT_DURATION = duration;
	}

	@Override
	public String getToolID()
	{
		return "tool.smoke";
	}

	@Override
	public String getName()
	{
		return ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Bombe fumigène";
	}

	@Override
	public List<String> getDescription()
	{
		return Utils.getToolDescription(ChatColor.GRAY + "Lance une bombe fumigène à vos pieds et vous fait disparaitre pendant " + ChatColor.GOLD + EFFECT_DURATION + " " + ChatColor.GRAY + "secondes. Seul l'object sélectionné est alors visible des autres joueurs. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.FIREWORK_CHARGE);
		ToolsUtils.resetTool(item);

		return item;
	}

	@Override
	public void onRightClick(final Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (ToolsUtils.isToolAvailable(tool))
		{
			new ItemCooldown(p, player, this, COOLDOWN);

			for (int i = 0; i <= 100; i++)
			{
				Location loc = player.getLocation();

				loc.setX(loc.getX() + 5 - ((int) (Math.random() * 10)));
				loc.setY(loc.getY() + 5 - ((int) (Math.random() * 10)));
				loc.setZ(loc.getZ() + 5 - ((int) (Math.random() * 10)));

				player.getWorld().playEffect(loc, Effect.EXPLOSION_HUGE, 10, 10);
			}

			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, EFFECT_DURATION * 20, 0, false, false));
			player.getWorld().playSound(player.getLocation(), Sound.BAT_LOOP, 1, 2);

			p.getGamePlayer(player).addRemainingInvisibility(EFFECT_DURATION);
		}
		else
		{
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}
}
