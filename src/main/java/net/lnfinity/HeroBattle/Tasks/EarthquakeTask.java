package net.lnfinity.HeroBattle.Tasks;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.Utils.Utils;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EarthquakeTask extends Task {

	public EarthquakeTask(HeroBattle p, Player player) {
		super(p, player);
	}

	@Override
	public void playTask() {
		player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_LARGE, 1);

		for (int i = 0; i <= 20; i++) {
			player.getWorld().playEffect(player.getLocation(), Effect.TILE_BREAK, 1, 100);
		}
		
		player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1, 1);
		
		for(Entity e : player.getNearbyEntities(4, 4, 4)) {
			if(e instanceof Player) {
				Player damaged = (Player) e;
				p.getGamePlayer(damaged).setPercentage(p.getGamePlayer(damaged).getPercentage() + Utils.randomNumber(20, 50));
				damaged.damage(0);
			}
		}
	}

}
