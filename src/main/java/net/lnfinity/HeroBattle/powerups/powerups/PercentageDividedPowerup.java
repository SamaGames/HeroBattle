package net.lnfinity.HeroBattle.powerups.powerups;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import net.lnfinity.HeroBattle.powerups.PositivePowerup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

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
public class PercentageDividedPowerup implements PositivePowerup
{
	private final HeroBattle p;

	public PercentageDividedPowerup(final HeroBattle plugin)
	{
		p = plugin;
	}

	@Override
	public void onPickup(final Player player, final ItemStack pickupItem)
	{
		final HeroBattlePlayer gPlayer = p.getGamePlayer(player);
		gPlayer.setPercentage(gPlayer.getPercentage() / 2, null);

		player.sendMessage(ChatColor.GREEN + "Votre pourcentage a été divisé par deux !");
	}

	@Override
	public ItemStack getItem()
	{
		return new Potion(PotionType.INSTANT_HEAL).toItemStack(1);
	}

	@Override
	public String getName()
	{
		return ChatColor.AQUA + "" + ChatColor.BOLD + "POURCENTAGE COUPÉ EN DEUX";
	}

	@Override
	public double getWeight()
	{
		return 10;
	}
}
