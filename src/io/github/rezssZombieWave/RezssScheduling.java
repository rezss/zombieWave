package io.github.rezssZombieWave;

import java.util.Random;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class RezssScheduling extends BukkitRunnable
{
  private final RezssMain plugin;
  private int zombieCount, idleTime;

  public RezssScheduling (RezssMain plugin)
  {
    this(plugin, 10, 0);
  }

  public RezssScheduling (RezssMain plugin, 
    int zombieCount)
  {
    this(plugin, 10, 10);
  }

  public RezssScheduling (RezssMain plugin, 
    int zombieCount, int idleTime)
  {
    this.plugin = plugin;
    this.zombieCount = zombieCount;
    this.idleTime = idleTime;
  }

  @Override
  public void run ()
  {
    printCountdown();
    spawnMobs();
  }

  private void printCountdown ()
  {
    if (idleTime > 0)
    {
      plugin.getServer().broadcastMessage("Wave " + 
        plugin.getWaveCount() + " in: " + idleTime + "sec");

      try
      {
        Thread.sleep((idleTime * 1000) - 5000);
      } catch (InterruptedException e) {}   
    }

    for (int i = 5; i >= 0; i--)
    {
      plugin.getServer().broadcastMessage("Zombies Incoming in: " + i);
      try
      {
        Thread.sleep(1000);
      } catch (InterruptedException e) {}
    }
  }

  private void spawnMobs()
  {
    Random random = new Random();

    for (int i = 0; i < zombieCount; i++)
    {
      Location loc = new Location(plugin.getServer().getWorld("world"), 
        -random.nextInt(4) - 21, 31, random.nextInt(6) + 330, -90, 0);
      
      plugin.entityList.add(
          plugin.getServer().getWorld("world").spawnEntity(
              loc, EntityType.ZOMBIE));
    }

    for (Player p1: plugin.playerList)
    {
      p1.setLevel(zombieCount);
    }
  }
}
