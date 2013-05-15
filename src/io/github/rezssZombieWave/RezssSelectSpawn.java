package io.github.rezssZombieWave;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RezssSelectSpawn extends BukkitRunnable 
  implements Listener
{
  @SuppressWarnings("unused")
  private RezssMain plugin;
  private Player sender;
  private Thread host;

  public RezssSelectSpawn (RezssMain plugin, 
    Player sender)
  {
    this.plugin = plugin;
    this.sender = sender;
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    
    host = new Thread(this);
    host.start();
  }

  @Override
  public void run ()
  {
    setSpawn();
  }

  private synchronized void setSpawn ()
  {
    sender.sendMessage("Right Click on a Block to select the new Spawn");
  }

  @EventHandler
  public void onPlayerInteractBlock (PlayerInteractEvent evt)
  {
  }

}
