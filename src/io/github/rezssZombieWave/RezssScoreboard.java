package io.github.rezssZombieWave;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class RezssScoreboard
{
  RezssMain plugin;

  ScoreboardManager manager = Bukkit.getScoreboardManager();
  Scoreboard board = manager.getMainScoreboard();

  Objective obj, obj1;
  
  public RezssScoreboard (RezssMain plugin)
  {
    this.plugin = plugin;
    setObjectives();
  }

  public void setObjectives ()
  {
    if (board.getObjective("Credits") == null)
    {
      obj = board.registerNewObjective("Credits", "totalKillCount");
      obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }
    if (board.getObjective("showhealth") == null)
    {
      obj1 = board.registerNewObjective("showhealth", "health");
      obj1.setDisplaySlot(DisplaySlot.BELOW_NAME);
      obj1.setDisplayName("/ 20"); 
    }
  }

  public void enableScoreboard (Player player)
  {
    for (Player p1: Bukkit.getOnlinePlayers())
    {
      obj.getScore(p1).setScore(1);
      obj.getScore(p1).setScore(0);
      p1.setHealth(p1.getHealth());
    }
  }

  public void disableScoreboard (Player player)
  {
    board.clearSlot(DisplaySlot.SIDEBAR);
  }

  public void addPlayer (Player player)
  {
    // team.addPlayer(player);
  }

  public Scoreboard getBoard ()
  {
    return board;
  }

  public void clearObjectives()
  {
    for (Objective o1: board.getObjectives())
    {
      o1.unregister();
    }
  }
}
