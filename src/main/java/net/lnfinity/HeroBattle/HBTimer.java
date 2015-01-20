package net.lnfinity.HeroBattle;


public class HBTimer {

	private HeroBattle p;
	private int seconds = 30;
	private int task = -1;

	public HBTimer(HeroBattle plugin) {
		p = plugin;
	}

	public void cancelTimer() {
		p.getServer().getScheduler().cancelTask(task);
	}

	public void restartTimer() {
		p.getServer().getScheduler().cancelTask(task);
		seconds = 10;
		task = p.getServer().getScheduler().runTaskTimer(p, new Runnable() {
			public void run() {
				p.getServer().broadcastMessage(
						"Le jeu commence dans " + seconds + " secondes.");
				if (seconds <= 1) {

					p.getServer().getScheduler().cancelTask(task);
					p.getListener().setWaiting(false);
					// Attendre encore une seconde puis commencer
					p.getGame().start();
				}
				seconds--;
			}
		}, 30L, 20L).getTaskId();
	}

}