package net.lnfinity.HeroBattle.utils;

public class TripleParameters {

	private final int min;
	private final int max;
	private final int other;
	
	public TripleParameters(int min, int max) {
		this(min, max, 0);
	}
	
	public TripleParameters(int min, int max, int other) {
		this.min = min;
		this.max = max;
		this.other = other;
	}

	public int getMinDamages() {
		return min;
	}

	public int getMaxDamages() {
		return max;
	}

	public int getOtherParam() {
		return other;
	}
}
