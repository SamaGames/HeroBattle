package net.lnfinity.HeroBattle.classes;

/*
 * This file is part of HeroBattle.
 *
 * HeroBattle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HeroBattle is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HeroBattle.  If not, see <http://www.gnu.org/licenses/>.
 */
public enum PlayerClassType
{

	// ** Free Classes **
	BRUTE("brute", PlayerClassPrice.FREE),
	GUERRIER("guerrier", PlayerClassPrice.FREE),
	ARCHER("archer", PlayerClassPrice.FREE),
	MAGE("mage", PlayerClassPrice.FREE),
	MINEUR("mineur", PlayerClassPrice.FREE),
	GARDIEN("gardien", PlayerClassPrice.FREE),

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
