package net.lnfinity.HeroBattle.classes;

public enum PlayerClassType {

	// ** Free Classes **
	BRUTE("brute"),
	GUERRIER("guerrier"),
	ARCHER("archer"),
	MAGE("mage"),
	MINEUR("mineur"),
	
	// ** Payable Classes **
	DRUIDE("druide"),
	CRYOGENIE("cryogenie"),
	PYROBARBARE("pyrobarbare"),
	
	// ** Hidden Classes **
	MAITE("maite"),
	PIKACHU("pikachu"),

	// ** Very Hidden / Reserved Classes **
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
