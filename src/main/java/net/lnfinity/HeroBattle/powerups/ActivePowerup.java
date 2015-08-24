package net.lnfinity.HeroBattle.powerups;


import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.utils.ParticleEffect;
import net.lnfinity.HeroBattle.utils.Utils;
import net.samagames.tools.Titles;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;


public class ActivePowerup
{
	HeroBattle p;

	// UUID of this specific active powerup
	private final UUID activePowerupID = UUID.randomUUID();

	// Location
	private final Location location;

	// Powerup
	private final Powerup powerup;

	// Entities of the powerup
	private Item entityItem;
	private ArmorStand entityBase;
	private ArmorStand entityTitle;

	// Async task used to display the particles
	private BukkitTask particlesTask;

	// Alive?
	private boolean alive = false;


	public ActivePowerup(final HeroBattle plugin, final Location location, final Powerup powerup)
	{
		this.p = plugin;
		this.location = location;
		this.powerup = powerup;
	}

	public void spawn()
	{

		/*** ***  ITEM AND HOLOGRAM  *** ***/

		final World world = location.getWorld();

		final ItemStack powerupItem = powerup.getItem().clone();
		final ItemMeta powerupItemMeta = powerupItem.getItemMeta();

		powerupItemMeta.setDisplayName(activePowerupID.toString());
		powerupItem.setItemMeta(powerupItemMeta);


		entityBase = world.spawn(location.clone().add(0, -0.5, 0), ArmorStand.class);
		entityBase.setVisible(false);
		entityBase.setSmall(true);
		entityBase.setMarker(true);
		entityBase.setGravity(false);

		entityItem = world.dropItem(location, powerupItem);
		entityItem.setPickupDelay(0);

		entityTitle = world.spawn(location, ArmorStand.class);
		entityTitle.setGravity(false);
		entityTitle.setVisible(false);
		entityTitle.setSmall(true);
		entityTitle.setMarker(true);
		entityTitle.setCustomName(powerup.getName());
		entityTitle.setCustomNameVisible(true);
		entityTitle.setCanPickupItems(false);


		entityBase.setPassenger(entityItem);
		entityItem.setPassenger(entityTitle);


		/*** ***  EFFECTS AND BROADCAST  *** ***/

		p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Un bonus vient de faire son apparition !");

		for (final Player player : p.getServer().getOnlinePlayers())
		{
			player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
			Titles.sendTitle(player, 5, 30, 5, ChatColor.DARK_GREEN + "\u272F", "");
		}

		final Location itemLocation = Utils.blockLocation(location).add(0, 1, 0);

		final Color fwColor;
		if (powerup instanceof PositivePowerup) fwColor = Color.GREEN.mixColors(Color.YELLOW);
		else fwColor = Color.RED.mixColors(Color.YELLOW);

		final Firework fw = location.getWorld().spawn(itemLocation, Firework.class);
		final FireworkMeta fwm = fw.getFireworkMeta();
		final FireworkEffect effect = FireworkEffect.builder()
				.withColor(fwColor).with(FireworkEffect.Type.BALL)
				.withFade(Color.YELLOW).build();
		fwm.addEffects(effect);
		fwm.setPower(0);
		fw.setFireworkMeta(fwm);

		Bukkit.getScheduler().runTaskLater(p, fw::detonate, 1l);


		particlesTask = Bukkit.getScheduler().runTaskTimerAsynchronously(p, () -> ParticleEffect.SPELL_INSTANT.display(0.5F, 0.5F, 0.5F, 0.1F, 2, itemLocation, 100.0), 1l, 5l);


		alive = true;
	}

	/**
	 * Removes a powerup.
	 *
	 * @param got If true the powerup is removed because someone picked-up it.
	 */
	public void remove(final boolean got)
	{

		/*** ***  ITEM AND HOLOGRAM  *** ***/

		entityTitle.remove();
		entityItem.remove();
		entityBase.remove();


		/*** ***  EFFECTS AND BROADCAST  *** ***/

		final Color fwColor = got ? Color.BLUE : Color.RED;

		final Firework fw = location.getWorld().spawn(Utils.blockLocation(location).add(0, 1, 0), Firework.class);
		final FireworkMeta fwm = fw.getFireworkMeta();
		final FireworkEffect effect = FireworkEffect.builder()
				.withColor(fwColor).with(FireworkEffect.Type.BALL).build();
		fwm.addEffects(effect);
		fwm.setPower(0);
		fw.setFireworkMeta(fwm);

		Bukkit.getScheduler().runTaskLater(p, fw::detonate, 1l);

		particlesTask.cancel();


		alive = false;
	}


	public boolean isAlive()
	{
		return alive;
	}

	public Powerup getPowerup()
	{
		return powerup;
	}

	public Location getLocation()
	{
		return location;
	}

	public UUID getActivePowerupUniqueID()
	{
		return activePowerupID;
	}
}
