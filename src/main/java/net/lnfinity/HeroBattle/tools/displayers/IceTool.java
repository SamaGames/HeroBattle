package net.lnfinity.HeroBattle.tools.displayers;

import net.lnfinity.HeroBattle.*;
import net.lnfinity.HeroBattle.game.*;
import net.lnfinity.HeroBattle.tools.*;
import net.lnfinity.HeroBattle.utils.*;
import net.samagames.utils.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;

import java.util.*;
public class IceTool extends PlayerTool {
	
	private final int COOLDOWN; // seconds
	private final int DURATION; // seconds
	private Random random = new Random();

	public IceTool(HeroBattle plugin, int cooldown, int duration) {
		super(plugin);
		COOLDOWN = cooldown;
		DURATION = duration;
	}

	@Override
	public String getToolID() {
		return "tool.ice";
	}

	@Override
	public String getName() {
		return ChatColor.AQUA + "" + ChatColor.BOLD + "Bloc de glace";
	}

	@Override
	public List<String> getDescription() {
		return Utils.getToolDescription(ChatColor.GRAY + "Construit un bloc de glace dans la direction visée qui reste pendant " + ChatColor.GOLD + DURATION + " " + ChatColor.GRAY + "secondes. Ne peut être utilisé que toutes les " + ChatColor.GOLD + COOLDOWN + " " + ChatColor.GRAY + "secondes.");
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.ICE);
		GlowEffect.addGlow(item);
		return item;
	}

	@Override
	public void onRightClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		if (ToolsUtils.isToolAvailable(tool)) {
			final List<Location> toClean = new ArrayList<Location>();
			Block b = null;
			try {
				b = p.getGame().getTargetBlock(player, 20).getLocation().clone().add(0, 1, 0).getBlock();
			} catch(Exception ex) {
				
			}
			
			if (b != null) {

				new ItemCooldown(p, player, this, COOLDOWN);

				for (HeroBattlePlayer heroBattlePlayer : p.getGamePlayers().values())
				{
					Player target = p.getServer().getPlayer(heroBattlePlayer.getPlayerUniqueID());
					if(target != null) {
						if(target.getLocation().distanceSquared(b.getLocation()) <= 9) {
							target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, DURATION * 20, 3));
						}
					}
				}
				
				buildBlock(b.getLocation().add(0, -1, -1), toClean);
				buildBlock(b.getLocation().add(0, 0, -1), toClean);
				buildBlock(b.getLocation().add(0, 1, -1), toClean);
				buildBlock(b.getLocation().add(0, 2, -1), toClean);
				
				buildBlock(b.getLocation().add(-1, 0, 0), toClean);
				buildBlock(b.getLocation().add(1, 0, 0), toClean);
				buildBlock(b.getLocation().add(1, 1, 0), toClean);
				
				buildBlock(b.getLocation().add(-1, 0, 1), toClean);
				buildBlock(b.getLocation().add(0, 0, 1), toClean);
				buildBlock(b.getLocation().add(0, 1, 1), toClean);
				buildBlock(b.getLocation().add(0, 2, 1), toClean);
				
				buildBlock(b.getLocation().add(-1, -1, 1), toClean);
				buildBlock(b.getLocation().add(-1, 0, 1), toClean);
				
				buildBlock(b.getLocation().add(-1, -1, 0), toClean);
				buildBlock(b.getLocation().add(-1, 0, 0), toClean);
				buildBlock(b.getLocation().add(-1, 1, 0), toClean);
				buildBlock(b.getLocation().add(-1, 2, 0), toClean);
				buildBlock(b.getLocation().add(-1, 3, 0), toClean);
				
				buildBlock(b.getLocation().add(0, 2, 0), toClean);
				buildBlock(b.getLocation().add(0, 3, 0), toClean);
				buildBlock(b.getLocation().add(0, 4, 0), toClean);
				
				p.getServer().getScheduler().runTaskLater(p, new Runnable() {
					@Override
					public void run() {
						for(Location loc : toClean) {
							loc.getBlock().setType(Material.AIR);
							loc.getWorld().spigot().playEffect(loc, Effect.TILE_BREAK, 79, 0, 0.0F, 0.0F, 0.0F, 0.0F, 32, 5);
							loc.getWorld().playSound(loc, Sound.GLASS, 1, 1);
						}
					}
				}, DURATION * 20L);
			} else {
				player.sendMessage(ChatColor.RED + "Vous ne gelez rien !");
			}
		} else {
			player.sendMessage(ChatColor.RED + "Vous êtes trop fatigué pour réutiliser ça maintenant");
		}
	}

	@Override
	public void onLeftClick(Player player, ItemStack tool, PlayerInteractEvent event) {
		
	}
	
	private void buildBlock(Location loc, List<Location> locs) {
		if(loc.getBlock().getType() == Material.AIR) {
			Material material;
			if(random.nextBoolean()) {
				material = Material.ICE;
			} else {
				material = Material.PACKED_ICE;
			}
			loc.getBlock().setType(material);
			locs.add(loc);
		}
	}

}
