package net.lnfinity.HeroBattle.Powerups;


import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class ActivePowerup {

	HeroBattle p;

	// Location
	private Location location;

	// Powerup
	private Powerup powerup;

	// Entities of the powerup
	private Item entityItem;
	private ArmorStand entityBase;
	private ArmorStand entityTitle;

	// Alive?
	private boolean alive = false;

	public ActivePowerup(HeroBattle plugin, Location location, Powerup powerup) {
		this.p = plugin;
		this.location = location;
		this.powerup = powerup;
	}

	public void spawn() {

		/*** ***  ITEM AND HOLOGRAM  *** ***/

		World world = location.getWorld();

		ItemStack powerupItem = powerup.getItem().clone();
		ItemMeta powerupItemMeta = powerupItem.getItemMeta();
			powerupItemMeta.setDisplayName(powerup.getName());
		powerupItem.setItemMeta(powerupItemMeta);


		entityBase = world.spawn(location.clone().add(0, -0.5, 0), ArmorStand.class);
		entityBase.setVisible(false);
		entityBase.setSmall(true);
		entityBase.setGravity(false);

		entityItem = world.dropItem(location, powerupItem);
		entityItem.setPickupDelay(0);

		entityTitle = world.spawn(location, ArmorStand.class);
		entityTitle.setGravity(false);
		entityTitle.setVisible(false);
		entityTitle.setSmall(true);
		entityTitle.setCustomName(powerup.getName());
		entityTitle.setCustomNameVisible(true);
		entityTitle.setCanPickupItems(false);


		entityBase.setPassenger(entityItem);
		entityItem.setPassenger(entityTitle);



		/*** ***  EFFECTS AND BROADCAST  *** ***/

		p.getServer().broadcastMessage(HeroBattle.GAME_TAG + ChatColor.GREEN + "Un bonus vient de faire son apparition !");

		for (Player player : p.getServer().getOnlinePlayers()) {
			player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
		}

		Color fwColor;
		if(powerup instanceof PositivePowerup) fwColor = Color.GREEN.mixColors(Color.YELLOW);
		else                                   fwColor = Color.RED.mixColors(Color.YELLOW);

		final Firework fw = location.getWorld().spawn(Utils.blockLocation(location), Firework.class);
		FireworkMeta fwm = fw.getFireworkMeta();
		FireworkEffect effect = FireworkEffect.builder()
				.withColor(fwColor).with(FireworkEffect.Type.BALL)
				.withFade(Color.YELLOW).build();
		fwm.addEffects(effect);
		fwm.setPower(0);
		fw.setFireworkMeta(fwm);

		Bukkit.getScheduler().runTaskLater(p, new Runnable() {
			@Override
			public void run() {
				fw.detonate();
			}
		}, 1l);

		alive = true;
	}

	/**
	 * Removes a powerup.
	 *
	 * @param got If true the powerup is removed because someone picked-up it.
	 */
	public void remove(boolean got) {

		/*** ***  ITEM AND HOLOGRAM  *** ***/

		entityTitle.remove();
		entityItem.remove();
		entityBase.remove();


		/*** ***  EFFECTS AND BROADCAST  *** ***/

		Color fwColor = got ? Color.BLUE : Color.RED;

		final Firework fw = location.getWorld().spawn(Utils.blockLocation(location), Firework.class);
		FireworkMeta fwm = fw.getFireworkMeta();
		FireworkEffect effect = FireworkEffect.builder()
				.withColor(fwColor).with(FireworkEffect.Type.BALL).build();
		fwm.addEffects(effect);
		fwm.setPower(0);
		fw.setFireworkMeta(fwm);

		Bukkit.getScheduler().runTaskLater(p, new Runnable() {
			@Override
			public void run() {
				fw.detonate();
			}
		}, 1l);


		alive = false;
	}


	public boolean isAlive() {
		return alive;
	}

	public Powerup getPowerup() {
		return powerup;
	}

	public Location getLocation() {
		return location;
	}
}
