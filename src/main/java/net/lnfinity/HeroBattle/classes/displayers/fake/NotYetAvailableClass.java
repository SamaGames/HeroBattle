package net.lnfinity.HeroBattle.classes.displayers.fake;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.classes.PlayerClass;
import net.lnfinity.HeroBattle.classes.PlayerClassType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;


/**
 * A fake player class to display a “not yet available” class in the selector.
 */
public class NotYetAvailableClass extends PlayerClass
{

	public NotYetAvailableClass(HeroBattle plugin)
	{
		super(plugin, 0, 0, 0);
	}

	@Override
	public String getName()
	{
		return "? ¿ ?";
	}

	@Override
	public ItemStack getIcon()
	{
		return new ItemStack(Material.BARRIER);
	}

	@Override
	public List<String> getDescription()
	{
		return Collections.singletonList("Bientôt disponible !");
	}

	@Override
	public int getMinDamages()
	{
		return 0;
	}

	@Override
	public int getMaxDamages()
	{
		return 0;
	}

	@Override
	public int getLives()
	{
		return 0;
	}

	@Override
	public ItemStack getHat()
	{
		return new ItemStack(Material.BARRIER);
	}

	@Override
	public int getMaxResistance()
	{
		return 0;
	}

	@Override
	public PlayerClassType getType()
	{
		return null;
	}
}
