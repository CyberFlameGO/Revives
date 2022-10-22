package me.tl0x.awakensmp.awakensmp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cyberflame.revives.commands.DeadPlayersCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Revives
  extends JavaPlugin
  implements Listener
{
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(new Events(this), (Plugin)this);
    ((PluginCommand)Objects.<PluginCommand>requireNonNull(getCommand("deadplayers"))).setExecutor((CommandExecutor)new DeadPlayersCommand(this));
    
    FileConfiguration config = getConfig();
    List<String> lst = new ArrayList<>();
    List<String> tbrDeadPlayer = new ArrayList<>();
    List<Location> deadLocations = new ArrayList<>();
    config.addDefault("Dead", lst);
    config.addDefault("tbRevived", tbrDeadPlayer);
    config.addDefault("deadLocs", deadLocations);
    config.addDefault("shouldFreezeDead", Boolean.valueOf(true));
    config.options().copyDefaults(true);
    saveConfig();
    makeHeadCraftable();
  }

  
  public boolean shouldFreeze() {
    return getConfig().getBoolean("shouldFreezeDead");
  }



  
  public void onDisable() {
    saveConfig();
    System.out.println("Disabling pluign!");
  }
  
  public void makeHeadCraftable() {
    ItemStack item = new ItemStack(Material.PLAYER_HEAD);
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName("" + ChatColor.RED + "Custom Player Head");
    item.setItemMeta(meta);
    item.addEnchantment(Enchantment.VANISHING_CURSE, 1);
    
    NamespacedKey key = new NamespacedKey((Plugin)this, "Head");
    
    ShapedRecipe recipe = new ShapedRecipe(key, item);
    recipe.shape(new String[] { "ODO", "DND", "ODO" });
    recipe.setIngredient('O', Material.OBSIDIAN);
    recipe.setIngredient('D', Material.DIAMOND_BLOCK);
    recipe.setIngredient('N', Material.NETHER_STAR);
    
    Bukkit.addRecipe((Recipe)recipe);
  }
}
