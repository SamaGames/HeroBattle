package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.game.HeroBattlePlayer;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

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
public class PikachuThunderWaveTool extends PlayerTool
{

	private final Integer THUNDER_WAVE_DURATION = 5;

	private final Integer COOLDOWN = 30;
	private final Integer BOX_SIZE = 10;


	public PikachuThunderWaveTool()
	{
		super(HeroBattle.get());
	}

	@Override
	public String getToolID()
	{
		return "tools.thunderWave";
	}

	@Override
	public String getName()
	{
		return ChatColor.YELLOW + "" + ChatColor.BOLD + "Thunder Wave";
	}

	@Override
	public List<String> getDescription()
	{
		return Utils.getToolDescription(ChatColor.GRAY + "Une décharge électrique paralyse partiellement les joueurs cible durant " + ChatColor.GOLD + THUNDER_WAVE_DURATION + ChatColor.GRAY + " secondes.");
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.WEB);
		ToolsUtils.resetTool(item);

		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (!ToolsUtils.isToolAvailable(tool))
		{
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser cela maintenant. Chuu!");
			return;
		}

		HeroBattlePlayer gPlayer = HeroBattle.get().getGamePlayer(player);
		if (gPlayer == null || gPlayer.isSpectator())
		{
			player.sendMessage(ChatColor.RED + "Euh... nop ? [Pas un joueur actif ou gPlayer == null] [wtf this was displayed, plz report to a dev]");
			return;
		}


		player.getNearbyEntities(BOX_SIZE, BOX_SIZE, BOX_SIZE).stream()
				.filter(e -> e instanceof Player && !e.equals(player))
				.forEach(e -> {
							Player pl = (Player) e;

							pl.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, THUNDER_WAVE_DURATION * 20, 17));
							pl.getWorld().strikeLightningEffect(pl.getLocation());
						}
				);


		new ItemCooldown(HeroBattle.get(), player, this, COOLDOWN);
	}
}
