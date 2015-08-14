package net.lnfinity.HeroBattle.game;

import net.lnfinity.HeroBattle.HeroBattle;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Deprecated
public class EffectsManager {

	private HeroBattle p;

	public EffectsManager(HeroBattle plugin) {
		p = plugin;
	}
	
	public void addPlayerEffect(final Player player, EffectType type, int duration, int amplifier) {
		if(player == null) return;
		final GamePlayer gamePlayer = p.getGamePlayer(player);
		if(gamePlayer == null) return;
		switch(type) {
            case STRENGH :
                gamePlayer.addRemainingDoubleDamages(duration);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration * 20, 0));
                break;

            case INVULNERABLE :
                gamePlayer.addRemainingReducedIncomingDamages(duration);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration * 20, 0));
                break;

            case RESPAWN :
                gamePlayer.setRespawning();
                break;

            case JUMP :
                gamePlayer.setMaxJumps(3, duration);
                break;

            case INVISIBLE :
                gamePlayer.addRemainingInvisibility(duration);
                break;
		}
	}
	
	public void addPlayerEffect(Player player, EffectType type, int duration) {
		addPlayerEffect(player, type, duration, 0);
	}
	
	public void addPlayersEffects(EffectType type, int distance, int duration, Player ignored) {
		for(GamePlayer gamePlayer : p.getGamePlayers().values()) {
			Player player = p.getServer().getPlayer(gamePlayer.getPlayerUniqueID());
			if(player != null) {
				if(!player.getUniqueId().equals(ignored.getUniqueId())) {
					addPlayerEffect(player, type, duration);
				}
			} else {
				addPlayerEffect(player, type, duration);
			}
		}
	}
	
	public void addPlayersEffects(EffectType type, int distance, int duration) {
		addPlayersEffects(type, distance, duration, null);
	}
}
