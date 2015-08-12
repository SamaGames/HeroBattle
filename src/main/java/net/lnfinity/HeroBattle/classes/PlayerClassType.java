package net.lnfinity.HeroBattle.classes;

public enum PlayerClassType {

	BRUTE("brute"),
	GUERRIER("guerrier"),
	ARCHER("archer"),
	MAGE("mage"),
	DRUIDE("druide"),
	CRYOGENIE("cryogenie"),
	PYROBARBARE("pyrobarbare"),
	
	MAITE("maite"),

	DEWOITINE("Dewoitine"),
	DEWOITINE_D550("DewoitineD"),
	POMME("Pomme");


	private final String id;
	
	PlayerClassType(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
}
