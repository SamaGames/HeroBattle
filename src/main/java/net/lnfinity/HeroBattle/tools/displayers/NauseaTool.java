package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.HeroBattle;
import net.lnfinity.HeroBattle.tools.PlayerTool;
import net.lnfinity.HeroBattle.utils.ItemCooldown;
import net.lnfinity.HeroBattle.utils.ToolsUtils;
import net.lnfinity.HeroBattle.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;


public class NauseaTool extends PlayerTool
{
	private final int COOLDOWN; // seconds
	private final int EFFECT_DURATION; // seconds
	private final double PROBABILITY_SENDER_HIT;

	private Random random = null;

	public NauseaTool(HeroBattle plugin, int cooldown, int duration, double probability)
	{
		super(plugin);
		COOLDOWN = cooldown;
		EFFECT_DURATION = duration;
		PROBABILITY_SENDER_HIT = probability;

		random = new Random();
	}

	@Override
	public String getToolID()
	{
		return "tool.nausea";
	}

	@Override
	public String getName()
	{
		return ChatColor.GREEN + "" + ChatColor.BOLD + "Crachat de vomi";
	}

	@Override
	public List<String> getDescription()
	{
		return Utils.getToolDescription(ChatColor.GRAY + "Lanche un crachat qui touche les joueurs alentours leur causant la nausée pendant " + ChatColor.GOLD + EFFECT_DURATION + " " + ChatColor.GRAY + "secondes. Attention, vous avez " + ChatColor.RED + (int) (PROBABILITY_SENDER_HIT * 100) + ChatColor.GRAY + "% " + " de chance de vous toucher ! Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem()
	{
		Dye dye = new Dye();
		dye.setColor(DyeColor.GREEN);

		ItemStack item = dye.toItemStack();
		item.setAmount(1);
		ToolsUtils.resetTool(item);

		return item;
	}

	@Override
	public void onRightClick(final Player player, ItemStack tool, PlayerInteractEvent event)
	{
		if (ToolsUtils.isToolAvailable(tool))
		{
			player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, (float) 1, (float) 2);
			p.getServer().getScheduler().runTaskLater(p, () -> player.playSound(player.getLocation(), Sound.SPLASH, (float) 1, (float) 0.5), 10L);

			new ItemCooldown(p, player, this, COOLDOWN);

			player.getNearbyEntities(10, 10, 10).stream()
					.filter(e -> e instanceof Player)
					.forEach(e -> {
								Player pl = (Player) e;
								pl.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, EFFECT_DURATION * 20, 0));
								player.playSound(player.getLocation(), Sound.SPLASH, (float) 1, (float) 0.5);
							}
					);

			if (random.nextDouble() < PROBABILITY_SENDER_HIT)
			{
				player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, ((EFFECT_DURATION * 20) / 2) + random.nextInt(EFFECT_DURATION / 2), 0));
			}
		}
		else
		{
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}
}
