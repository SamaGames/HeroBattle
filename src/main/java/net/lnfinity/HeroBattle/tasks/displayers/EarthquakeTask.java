package net.lnfinity.HeroBattle.tasks.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tasks.Task;
import net.lnfinity.HeroBattle.utils.Utils;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EarthquakeTask extends Task {
	
	private final int MIN_DAMAGES;
	private final int MAX_DAMAGES;

	public EarthquakeTask(HeroBattle p, Player player, int min, int max) {
		super(p, player);
		MIN_DAMAGES = min;
		MAX_DAMAGES = max;
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
				p.getGamePlayer(damaged).setPercentage(p.getGamePlayer(damaged).getPercentage() + Utils.randomNumber(MIN_DAMAGES, MAX_DAMAGES), p.getGamePlayer(player));
				damaged.damage(0);
			}
		}
	}

}
