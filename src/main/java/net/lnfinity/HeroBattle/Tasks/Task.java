package net.lnfinity.HeroBattle.Tasks;

import net.lnfinity.HeroBattle.HeroBattle;

import org.bukkit.entity.Player;

public abstract class Task {

	protected Player player;
	protected HeroBattle p;

	public Task(HeroBattle p, Player player) {
		this.player = player;
		this.p = p;
	}

	public abstract void playTask();

}
