package io.github.rezssZombieWave;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class RezssMain extends JavaPlugin implements Listener
{
  private boolean isEventRunning = false, isEventIdling = false;
  private int waveCount;

  public ArrayList<Entity> entityList = new ArrayList<Entity>();
  public ArrayList<Player> playerList = new ArrayList<Player>();

  private RezssScoreboard scoreboard = new RezssScoreboard(this);
  public RezssLocations location;

  @Override
  public void onEnable ()
  {
    new RezssListener(this);
    location = new RezssLocations(this);
    waveCount = 1;
  }

  @Override
  public void onDisable ()
  {

  }

  @Override
  public boolean onCommand (CommandSender sender, Command cmd,
      String label, String[] args)
  {
    if (cmd.getName().equalsIgnoreCase("rezss"))
    {
      if (args[0].equalsIgnoreCase("set"))
      {
        if (args[1].equalsIgnoreCase("spawn"))
        {
          new RezssSelectSpawn(this, (Player) sender);
          return true;
        }
      }
      else if (args[0].equalsIgnoreCase("start"))
      {
        if (!isEventRunning && playerList.size() <= 4)
        {
          startGame((Player) sender);
        }
        else if (isEventRunning)
        {
          sender.sendMessage("YOU CAN'T START THE GAME TWICE!");
        }
        else if (playerList.size() == 4)
        {
          sender.sendMessage("Game is full");
        }
        return true;
      }
      else if (args[0].equalsIgnoreCase("stop"))
      {
        for (Player p: playerList)
        {
          abortGame(p);
        }
        return true;
      }
      else if (args[0].equalsIgnoreCase("clear"))
      {
        clearAllEnemys((Player) sender);
        return true;
      }
      else if (args[0].equalsIgnoreCase("stats"))
      {
        System.out.println("EntityList: " + entityList.size());
        for (Player p1: playerList)
        {
          System.out.println("Player " + playerList.indexOf(p1) + ": " + p1.getPlayerListName());
        }
        ((Player)sender).setVelocity(new Vector(0.0, 0.0, 0.0).add(((Player)sender).getLocation().getDirection().normalize().setY(0.5)));
        System.out.println(((Player)sender).getLocation().getDirection().normalize());
        return true;
      }
      else if (args[0].equalsIgnoreCase("score"))
      {
        scoreboard.enableScoreboard((Player) sender);
        return true;
      }
      else if (args[0].equalsIgnoreCase("clear_score"))
      {
        scoreboard.clearObjectives();
        return true;
      }
      else if (args[0].equalsIgnoreCase("item"))
      {
        System.out.println(((Player) sender).getItemInHand());
        System.out.println(((Player) sender).getItemInHand().getItemMeta());

        ItemMeta weaponMeta = ((Player) sender).getItemInHand().getItemMeta();
        System.out.println(weaponMeta.getDisplayName());

        return true;
      }
      else if (args[0].equalsIgnoreCase("go"))
      {
        ((Player)sender).setAllowFlight(true);
        return true;
      }
    }
    return false;
  }

  public void preparePlayer (Player player)
  {
    player.setHealth(player.getMaxHealth());
    player.setLevel(0);
    player.setExp(0);
    player.getInventory().clear();
    player.getInventory().setArmorContents(new ItemStack[4]);
  }
  
  public void playerPreProduction (Player player)
  {
    if (!playerList.contains(player))
    {
      playerList.add(player);
    }

    player.teleport(location.lobbyLoc);

    player.setItemInHand(new ItemStack(276));
  }

  public void playerPostProduction (Player player)
  {
    if (playerList.contains(player))
    {
      playerList.remove(player);
      player.teleport(location.deadZoneLoc);
      
      return;
    }

    player.getServer().broadcastMessage("ERROR OCCURED: PLAYER " +
      "NOT IN LIST");
  }

  public void clearAllEnemys (Player player)
  {
    for (Entity ent : player.getNearbyEntities(200, 200, 200))
    {
      if ((ent instanceof Damageable) && !(ent instanceof Player))
      {
        ((Damageable) ent).damage(((Damageable)ent).getMaxHealth());
      }
    }
  }

  public void startGame (Player player)
  {
    preparePlayer(player);
    playerPreProduction(player);
    
    if (!isEventIdling)
    {
      new Thread(new RezssLobby(this, playerList, 30)).start();
      new Thread(new RezssScheduling(this, 10, 50)).start();  
    }

    setIsEventIdling(true);
  }

  public void abortGame (Player player)
  {
    preparePlayer(player);
    playerPostProduction(player);
    
    setIsEventRunning(false);
    setIsEventIdling(false);
    
    waveCount = 1;
  }

  public void pauseGame ()
  {
  }

  public void setIsEventRunning (boolean isRunning)
  {
    if (isRunning)
    {
      getServer().broadcastMessage("Game is running!");
      isEventRunning = true;
    }
    else
    {
      getServer().broadcastMessage("Game is not running!");

      isEventRunning = false;

      for (int i = 0; i < entityList.size(); i++)
      {
        ((Damageable)entityList.get(i)).damage(
          ((Damageable)entityList.get(i)).getMaxHealth());
      }
      entityList.clear();
    }
  }

  public void setIsEventIdling (boolean isIdling)
  {
    if (isIdling)
    {
      isEventIdling = true;
    }
    else
    {
      isEventIdling = false;
    }
  }

  public boolean getIsEventRunning ()
  {
    return isEventRunning;
  }

  public int getWaveCount ()
  {
    return waveCount;
  }

  public int incWaveCount ()
  {
    return ++waveCount;
  }
}
