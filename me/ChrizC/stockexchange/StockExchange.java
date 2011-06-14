package me.ChrizC.stockexchange;

import org.bukkit.event.Event.*;
import org.bukkit.event.*;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

import com.nijikokun.register.payment.Method;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import org.bukkit.Bukkit;

public class StockExchange extends JavaPlugin {
    
    public static PermissionHandler permissionHandler;
    private final SEConfig config = new SEConfig(this);
    private final SEScheduleHandler scheduleHandler = new SEScheduleHandler(this);
    private final SEMarketHandler marketHandler = new SEMarketHandler(this);
    private final SEPluginListener pluginListener = new SEPluginListener(this);
    private final SECommandListener cmdHandler = new SECommandListener(this, marketHandler, scheduleHandler, config);
    private final SEFileHandler fileHandler = new SEFileHandler(this);
    private final SEUpdater updater = new SEUpdater(this, config);
    public Method Method = null;
    
    HashMap<String, Double> market = new HashMap<String, Double>();
    HashMap<String, Integer> stockOwnership = new HashMap<String, Integer>();
    
    @Override
    public void onDisable() {
        if (scheduleHandler.taskId != 0) {
            Bukkit.getServer().getScheduler().cancelTask(scheduleHandler.taskId);
        }
        fileHandler.saveMarket();
        fileHandler.saveOwnership();
        System.out.println("[StockExchange] disabled.");
    }
    
    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, pluginListener, Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLUGIN_DISABLE, pluginListener, Priority.Monitor, this);
        config.doConfig();
        updater.update(true);
        cmdHandler.setupCommands();
        setupPermissions();
        fileHandler.loadMarket();
        fileHandler.loadOwnership();
        if (config.flucsEnabled == true) {
            scheduleHandler.fluctuate(config.min, config.max, config.delay, config.broadcast);
        }
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println("[StockExchange] version v" + pdfFile.getVersion() + " is enabled.");
    }
    
    private void setupPermissions() {
      Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");

      if (this.permissionHandler == null) {
            if (permissionsPlugin != null) {
                this.permissionHandler = ((Permissions) permissionsPlugin).getHandler();
                System.out.println("[StockExchange] hooked into Permissions.");
            } else {
                System.out.println("[StockExchange] Permissions not found, defaulting to ops.txt");
            }
        }
    }    
}
