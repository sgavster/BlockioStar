package me.sgavster.blockiostar;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{

	public void onEnable()
	{
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	private ArrayList<String> star = new ArrayList<String>();

	@EventHandler
	public void onInteract(PlayerInteractEvent e)
	{
		final Player p = e.getPlayer();
		Action a = e.getAction();
		if(a.equals(Action.RIGHT_CLICK_AIR) || a.equals(Action.RIGHT_CLICK_BLOCK))
		{
			if(p.hasPermission("blockio.star"))
			{
				if(p.isSneaking())
				{
					if(p.getItemInHand().getType().equals(Material.NETHER_STAR))
					{
						if(star.contains(p.getName()))
						{
							p.sendMessage("§4You already have the star effect!");
						}
						else
						{
							p.sendMessage("§eBum bum bum bum bum dum dum!");
							star.add(p.getName());
							if(p.getItemInHand().getAmount() == 1)
							{
								p.setItemInHand(null);
							}
							else
							{
								p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
							}
							Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
							{
								public void run()
								{
									star.remove(p.getName());
									p.sendMessage("§eYour star power has run out!");
								}
							}, 500L);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		Player p = e.getPlayer();
		if(star.contains(p.getName()))
		{
			World w = p.getWorld();
			Location l = p.getLocation();
			Location u = new Location(w, l.getX(), l.getY() + 1, l.getZ());
			w.playEffect(l, Effect.STEP_SOUND, Material.GOLD_BLOCK.getId());
			w.playEffect(u, Effect.STEP_SOUND, Material.GOLD_BLOCK.getId());
			for(Entity le : p.getNearbyEntities(1, 1, 1))
			{
				if(le instanceof LivingEntity)
				{
					((LivingEntity) le).damage(80);
				}
			}
		}
	}
	@EventHandler
	public void onDamage(EntityDamageEvent e)
	{
		if(e.getEntity() instanceof Player && star.contains(((Player) e.getEntity()).getName()))
		{
			e.setCancelled(true);
		}
	}
}