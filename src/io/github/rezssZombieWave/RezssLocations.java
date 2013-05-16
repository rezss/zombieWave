package io.github.rezssZombieWave;

import org.bukkit.Location;
import org.bukkit.World;

public class RezssLocations
{
  private RezssMain plugin;
  private World currentWorld;

  public Location lobbyLoc, stageOneLoc, deadZoneLoc;
  
  public RezssLocations (RezssMain newPlugin)
  {
    plugin = newPlugin;
    currentWorld = plugin.getServer().getWorld("world");

    lobbyLoc = new Location(currentWorld, -53, 32, 243);
    stageOneLoc = new Location(currentWorld, -31.5, 32, 334);
    deadZoneLoc = new Location(currentWorld, 6, 21, 310, 2, 0);
  }

}
