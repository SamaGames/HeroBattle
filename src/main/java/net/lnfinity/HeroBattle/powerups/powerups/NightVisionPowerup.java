package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.powerups.PositivePowerup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


/**
 * TODO remove blinks when the effect ends (or keep them three instead of ten seconds).
 */
public class NightVisionPowerup implements PositivePowerup
{

	@Override
	public void onPickup(final Player player, final ItemStack pickupItem)
	{
		player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 30, 0));
		player.sendMessage(ChatColor.GREEN + "Cadeau ! Un peu de claireté pour une trentaine de secondes.");
	}

	@Override
	public ItemStack getItem()
	{
		return new ItemStack(Material.REDSTONE_LAMP_OFF);
	}

	@Override
	public String getName()
	{
		return ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "VISION NOCTURNE";
	}

	@Override
	public double getWeight()
	{
		return 15;
	}
}
