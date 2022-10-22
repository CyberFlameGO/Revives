package net.cyberflame.revives.commands;

import java.util.List;
import java.util.UUID;
import net.cyberflame.revives.Revives;
import net.cyberflame.revives.Helper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class DeadPlayersCommand
  implements CommandExecutor
{
  private Revives main;
  
  public DeadPlayersCommand(Revives main) {
    this.main = main;
  }

  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player)sender;
      FileConfiguration config = this.main.getConfig();
      List<String> lst = config.getStringList("Dead");

      
      for (int i = 0; i < lst.size(); i++) {
        String uuid1 = lst.get(i);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid1));
        Helper.sendMessage(player, offlinePlayer.getName());
      } 
    } 
    return false;
  }
}
