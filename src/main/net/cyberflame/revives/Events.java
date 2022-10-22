package net.cyberflame.revives;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Events implements Listener {
  public Events(Revives main) {
    this.main = main;
  }
  private final Revives main;
  
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent e) {
    FileConfiguration config = this.main.getConfig();
    List<String> lst = config.getStringList("Dead");

    
    Player player = e.getEntity();
    if (player.getKiller() != null) {
      Location deathloc = player.getLocation();
      World w = Bukkit.getWorld("world");
      ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
      SkullMeta s = (SkullMeta)skull.getItemMeta();


      
      assert s != null;
      s.setOwningPlayer((OfflinePlayer)player);
      skull.setItemMeta((ItemMeta)s);

      
      w.dropItem(deathloc, skull);


      
      if (!lst.contains(player.getUniqueId().toString()))
      {
        lst.add(player.getUniqueId().toString());
      }
      
      player.setGameMode(GameMode.SPECTATOR);
      config.set("Dead", lst);
      this.main.saveConfig();
    } 
  }
  
  @EventHandler
  public void onBlockPlace(BlockPlaceEvent e) {
    FileConfiguration config = this.main.getConfig();
    List<String> lst = config.getStringList("Dead");
    List<String> DeadPlayers = (List<String>)config.getObject("tbRevived", List.class);
    List<Location> Deadloc = (List<Location>)config.getObject("deadLocs", List.class);
    
    Block block = e.getBlock();
    Location loc = block.getLocation();
    Material mat = block.getType();
    ItemStack item = e.getItemInHand();
    ItemMeta meta = item.getItemMeta();
    Player player = e.getPlayer();
    
    if (mat == Material.PLAYER_HEAD) {
      try {
        String playerName = meta.getDisplayName();
        String uuid = Helper.getUUIDfromName(playerName);
        if (lst.contains(uuid)) {
          
          block.setType(Material.AIR);
          OfflinePlayer revived = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
          Helper.sendMessage(player, "Revived " + revived.getName());
          
          if (revived.isOnline()) {
            Player revivedp = (Player)revived;
            revivedp.setGameMode(GameMode.SURVIVAL);
            revivedp.teleport(loc);
            // Helper.sendMessage(revivedp, "You have been revived by " + player.getName() + "!");
          } else {
            
            Helper.sendMessage(player, "Player is offline, they will respawn when they join back.");
            assert DeadPlayers != null;
            DeadPlayers.add(revived.getName());
            Deadloc.add(loc);
            config.set("tbRevived", DeadPlayers);
            config.set("deadLocs", Deadloc);
            this.main.saveConfig();
          } 

          
          lst.remove(uuid);
          config.set("Dead", lst);
          this.main.saveConfig();
        } else {
          Helper.sendMessage(player, "Player not found! Did you put in a valid dead player?");
        } 
      } catch (Exception error) {
        e.getPlayer().sendMessage("Failed to revive!");
      } 
    }
  }



  
  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    Player player = e.getPlayer();
    FileConfiguration config = this.main.getConfig();
    List<String> lst = config.getStringList("Dead");
    List<String> DeadPlayers = (List<String>)config.getObject("tbRevived", List.class);
    List<Location> Deadloc = (List<Location>)config.getObject("deadLocs", List.class);

    
    if (!lst.contains(Helper.getUUIDfromName(player.getName())) && player.getGameMode() == GameMode.SPECTATOR && DeadPlayers
      .contains(player.getName())) {
      int index = DeadPlayers.indexOf(player.getName());
      Location loc = ((List<Location>)Objects.<List<Location>>requireNonNull(Deadloc)).get(index);

      
      player.teleport(loc);
      player.setGameMode(GameMode.SURVIVAL);

      
      Deadloc.remove(loc);
      DeadPlayers.remove(DeadPlayers.get(index));
      config.set("tbRevived", DeadPlayers);
      config.set("deadLocs", Deadloc);
      this.main.saveConfig();
    } 
  }
  
  @EventHandler
  public void onSpectate(PlayerTeleportEvent event) {
    if (event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE && this.main.shouldFreeze()) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    FileConfiguration config = this.main.getConfig();
    List<String> lst = config.getStringList("Dead");
    if (lst.contains(event.getPlayer().getUniqueId()) && event.getPlayer().getGameMode() == GameMode.SPECTATOR && this.main.shouldFreeze())
      event.setCancelled(true); 
  }
}
