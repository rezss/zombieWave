package io.github.rezssZombieWave;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RezssLobby implements Runnable
{
  private RezssMain plugin;
  private ArrayList<Player>   playerList;
  private Location            arenaLoc;
  private int                 idleTime;

  public RezssLobby(RezssMain plugin,
      ArrayList<Player> playerList, int idleTime)
  {
    this.plugin = plugin;
    this.playerList = playerList;
    this.idleTime = idleTime;

    arenaLoc = plugin.location.stageOneLoc;    
  }

  @Override
  public void run ()
  {
  	teleportPlayer();
  }

  public void teleportPlayer ()
  {
    if (idleTime > 0)
    {
      for (int i = 0; i < 2; i++)
      {
        plugin.getServer().broadcastMessage(
            "Game starts in: " + idleTime + " sec");

        try
        {
          Thread.sleep((idleTime * 1000) / 2);
        } catch (InterruptedException e)
        {
        }

        idleTime = idleTime / 2;
      }
    }

    for (int i = 5; i >= 0; i--)
    {
      plugin.getServer().broadcastMessage("Game Starts in: " + i);
      try
      {
        Thread.sleep(1000);
      } catch (InterruptedException e)
      {
      }
    }

    for (Player p1 : playerList)
    {
      p1.teleport(arenaLoc);
    }

    plugin.setIsEventRunning(true);
  }
}
