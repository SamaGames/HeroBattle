package net.lnfinity.HeroBattle.Class;

public enum PlayerClassType {

	BRUTE("brute"),
	GUERRIER("guerrier"),
	ARCHER("archer"),
	MAGE("mage"),
	DRUIDE("druide"),
	
	MAITE("maite");
	
	private final String id;
	
	private PlayerClassType(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
}
