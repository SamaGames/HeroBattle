package net.lnfinity.HeroBattle.classes;

public enum PlayerClassType {

	BRUTE("brute"),
	GUERRIER("guerrier"),
	ARCHER("archer"),
	MAGE("mage"),
	DRUIDE("druide"),
	CRYOGENIE("cryogenie"),
	
	MAITE("maite");
	
	private final String id;
	
	private PlayerClassType(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
}
