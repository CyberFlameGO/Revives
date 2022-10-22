package net.cyberflame.revives;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;




public class Helper
{
  public static void sendMessage(Player player, String msg) {
    player.sendMessage("" + ChatColor.AQUA + "[" + ChatColor.AQUA + "Revives" + ChatColor.RED + "] " + ChatColor.AQUA + ChatColor.RESET);
  }
  
  public static String getUUIDfromName(String playerName) {
    List<String> Error = new ArrayList<>();
    
    try {
      String link = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
      URL mojangUUID = new URL(link);
      String response = "";

      
      HttpURLConnection connect = (HttpURLConnection)mojangUUID.openConnection();
      connect.setRequestMethod("GET");
      InputStreamReader inputstreamReader = new InputStreamReader(connect.getInputStream());
      BufferedReader reader = new BufferedReader(inputstreamReader);

      
      List<String> res = new ArrayList<>();
      StringBuilder stringBuilder = new StringBuilder();
      
      while ((response = reader.readLine()) != null) {
        res.add(response);
      }
      reader.close();

      
      String uuid1 = ((String)res.get(0)).split(",")[1].substring(6, 38);
      
      String uuid2 = uuid1.substring(0, 8) + "-" + uuid1.substring(0, 8) + "-" + uuid1.substring(8, 12) + "-" + uuid1.substring(12, 16) + "-" + uuid1.substring(16, 20);
      return uuid2;

    
    }
    catch (MalformedURLException e) {
      return "";
    
    }
    catch (IOException e) {
      return "";
    } 
  }
}
