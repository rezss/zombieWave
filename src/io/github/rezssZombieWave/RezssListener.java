package io.github.rezssZombieWave;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

public class RezssListener implements Listener
{
  private final RezssMain plugin;
  private int timesJumped;

  public RezssListener(RezssMain plugin)
  {
    this.plugin = plugin;
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onPlayerInteractEvent (PlayerInteractEvent evt)
  {
    if (evt.getPlayer().getItemInHand().hasItemMeta())
    {
      if (evt.getPlayer().getItemInHand().getItemMeta().hasDisplayName())
      {
        if (evt.getPlayer().getItemInHand().getItemMeta().getDisplayName()
            .equalsIgnoreCase("MG"))
        {
          if (Action.RIGHT_CLICK_AIR == evt.getAction()
              || Action.RIGHT_CLICK_BLOCK == evt.getAction())
          {
            // Location loc = evt.getPlayer().getLocation();
            // Vector direction =
            // evt.getPlayer().getLocation().getDirection().normalize();

            // loc.setY(loc.getY() + 1.6);

            // evt.getPlayer().getWorld().spawnArrow(loc.add(direction),
            // evt.getPlayer().getLocation().getDirection(), (float) 2,
            // 0).setShooter(evt.getPlayer());
            evt.getPlayer().launchProjectile(Snowball.class)
                .setShooter(evt.getPlayer());

          } else if (Action.PHYSICAL == evt.getAction())
          {
            System.out.println("PRESSURE!");
          }
        }
      }
    }
  }

  @EventHandler
  public void onEntityDeathEvent (EntityDeathEvent evt)
  {
    evt.setDroppedExp(0);

    if (plugin.getIsEventRunning())
    {
      if (plugin.entityList.contains(evt.getEntity()))
      {
        plugin.entityList.remove(evt.getEntity());

        for (Player p1 : plugin.playerList)
        {
          p1.setLevel(plugin.entityList.size());
        }
      }
      if (plugin.entityList.isEmpty())
      {
        new Thread(new RezssScheduling(plugin,
            plugin.incWaveCount() * 10, 10)).start();
      }
    }
  }

  @EventHandler
  public void onItemSpawnEvent (ItemSpawnEvent evt)
  {
    // evt.getEntity().remove();
  }

  @EventHandler
  public void onCreatureSpawnEvent (CreatureSpawnEvent evt)
  {
  }

  @EventHandler
  public void onEntityDamageByEntityEvent (EntityDamageByEntityEvent evt)
  {
    if (evt.getEntity() instanceof Player)
    {
      if (((Damageable) evt.getEntity()).getHealth() <= 4)
      {
        if (plugin.playerList.contains(evt.getEntity()))
        {
          plugin.setIsEventRunning(false);
          plugin.playerPostProduction((Player) evt.getEntity());
        }
      }
    } if (evt.getDamager() instanceof Snowball)
    {
      ((Damageable) evt.getEntity()).damage(10, ((Projectile) evt.getDamager()).getShooter());
    }
  }

  @EventHandler
  public void onPlayerToggleFlightEvent (PlayerToggleFlightEvent evt)
  {
    Player player = evt.getPlayer();
    Vector jump = player.getLocation().getDirection().multiply(0.2).setY(1);
//    Location loc = player.getLocation();
//    Block block = loc.add(0, -2, 0).getBlock();

    if (evt.isFlying() && evt.getPlayer().getGameMode() != GameMode.CREATIVE)
    {
      // if (timesJumped != 2)
      // {
        player.setFlying(false);
        player.setVelocity(player.getVelocity().add(jump));
         new Thread(new RezssDoubleJump(plugin, evt.getPlayer())).start();
//        evt.getPlayer().setExp((float) 0.1);
//        timesJumped++;
      // }
      // else if (timesJumped == 2)
      // {
      //   if (block.getType() != Material.AIR)
      //   {
      //     player.setAllowFlight(true);
      //     timesJumped = 0;
      //   }
      //   else
      //   {
      //     player.setFlying(false);
      //     player.setAllowFlight(true);
      //   }
      // }
      evt.setCancelled(true);
//      System.out.println(timesJumped);
    }
    // evt.getPlayer().setVelocity(new Vector(0.0, 0.0, 0.0).add((evt.getPlayer()).getLocation().getDirection().normalize().setY(0.5)));
  }
}
