package io.github.rezssZombieWave;

import org.bukkit.entity.Player;

public class RezssDoubleJump implements Runnable
{

  private final RezssMain plugin;
  private final Player    player;

  public RezssDoubleJump(RezssMain plugin, Player player)
  {
    this.plugin = plugin;
    this.player = player;
  }

  @Override
  public void run ()
  {
    // TODO Auto-generated method stub
    setExperience();

  }

  public void setExperience ()
  {
    for (float i = (float) 0.1; i <= 1.1; i += 0.1)
    {
      System.out.println("Adding XP to player " + player.getDisplayName() + i);
      player.setExp(i);
      try
      {
        Thread.sleep(500);
      } catch (InterruptedException e)
      {
      }
    }
  }

}
