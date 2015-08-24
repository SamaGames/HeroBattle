package net.lnfinity.HeroBattle.classes;

public enum PlayerClassType
{

	// ** Free Classes **
	BRUTE("brute", PlayerClassPrice.FREE),
	GUERRIER("guerrier", PlayerClassPrice.FREE),
	ARCHER("archer", PlayerClassPrice.FREE),
	MAGE("mage", PlayerClassPrice.FREE),
	MINEUR("mineur", PlayerClassPrice.FREE),

	// ** Payable Classes **
	DRUIDE("druide", PlayerClassPrice.PAID),
	CRYOGENIE("cryogenie", PlayerClassPrice.PAID),
	PYROBARBARE("pyrobarbare", PlayerClassPrice.PAID),

	// ** Hidden Classes **
	MAITE("maite", PlayerClassPrice.EASTER_EGG),
	PIKACHU("pikachu", PlayerClassPrice.EASTER_EGG),

	// ** Very Hidden / Reserved Classes **
	DEWOITINE("Dewoitine", PlayerClassPrice.EASTER_EGG),
	DEWOITINE_D550("DewoitineD", PlayerClassPrice.EASTER_EGG),
	POMME("Pomme", PlayerClassPrice.EASTER_EGG);


	private final String id;
	private final PlayerClassPrice price;

	PlayerClassType(String id, PlayerClassPrice price)
	{
		this.id = id;
		this.price = price;
	}

	public String getId()
	{
		return this.id;
	}

	public PlayerClassPrice getPrice()
	{
		return price;
	}
}
