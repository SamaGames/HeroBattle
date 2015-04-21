package net.lnfinity.HeroBattle.game;

import net.lnfinity.HeroBattle.HeroBattle;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
			gamePlayer.setDoubleDamages(true);
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration * 20, 0));
			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					gamePlayer.setDoubleDamages(false);
				}
			}, duration * 20L);
			break;
		case INVULNERABLE :
			gamePlayer.setInvulnerable(true);
			player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration * 20, 0));
			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					gamePlayer.setInvulnerable(false);
				}
			}, duration * 20L);
		case RESPAWN :
			gamePlayer.setRespawning(true);
			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					gamePlayer.setRespawning(false);
				}
			}, duration * 20L);
		case JUMP :
			gamePlayer.setMaxJumps(3);
			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					gamePlayer.setMaxJumps(2);
				}
			}, duration * 20L);
		case INVISIBLE :
			gamePlayer.setInvisible(true);
			p.getGame().updatePlayerArmor(player);
			p.getServer().getScheduler().runTaskLater(p, new Runnable() {
				@Override
				public void run() {
					gamePlayer.setInvisible(false);
					p.getGame().updatePlayerArmor(player);
				}
			}, duration * 20L);
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
