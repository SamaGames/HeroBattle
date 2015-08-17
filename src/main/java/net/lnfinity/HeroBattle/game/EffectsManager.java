package net.lnfinity.HeroBattle.game;

import net.lnfinity.HeroBattle.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;

@Deprecated
public class EffectsManager {

	private HeroBattle p;

	public EffectsManager(HeroBattle plugin) {
		p = plugin;
	}
	
	public void addPlayerEffect(final Player player, EffectType type, int duration, int amplifier) {
		if(player == null) return;

		final HeroBattlePlayer heroBattlePlayer = p.getGame().getPlayer(player.getUniqueId());
		if (heroBattlePlayer == null) return;

		switch(type) {
            case STRENGH :
	            heroBattlePlayer.addRemainingDoubleDamages(duration);
	            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration * 20, 0));
                break;

            case INVULNERABLE :
	            heroBattlePlayer.addRemainingReducedIncomingDamages(duration);
	            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration * 20, 0));
                break;

            case RESPAWN :
	            heroBattlePlayer.setRespawning();
	            break;

            case JUMP :
	            heroBattlePlayer.setMaxJumps(3, duration);
	            break;

            case INVISIBLE :
	            heroBattlePlayer.addRemainingInvisibility(duration);
	            break;
		}
	}
	
	public void addPlayerEffect(Player player, EffectType type, int duration) {
		addPlayerEffect(player, type, duration, 0);
	}
	
	public void addPlayersEffects(EffectType type, int distance, int duration, Player ignored) {
		for (HeroBattlePlayer heroBattlePlayer : p.getGame().getInGamePlayers().values())
		{
			Player player = p.getServer().getPlayer(heroBattlePlayer.getUUID());

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
