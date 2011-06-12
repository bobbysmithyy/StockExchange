package me.ChrizC.stockexchange;

import java.io.*;

import org.bukkit.util.config.Configuration;

public class SEConfig {
    
    private final StockExchange plugin;
    
    Configuration file;
    
    Boolean flucsEnabled;
    Boolean broadcast;
    Boolean checkVersion;
    
    Integer delay = 15;
    Double max = 4D;
    Double min = 2D;
    
    public SEConfig (StockExchange instance) {
        plugin = instance;
    }
    
    public void doConfig() {
        file = new Configuration(new File(plugin.getDataFolder(), "config.yml"));
        file.load();
        if (new File(plugin.getDataFolder(),"config.yml").exists()) {
            System.out.println("[StockExchange] Configuration file loaded!");
        } else {
            file.setProperty("naturalFluctuations", true);
            file.setProperty("fluctuationDelay", 15);
            file.setProperty("maxFluctuation", 4);
            file.setProperty("minFluctuation", 2);
            file.setProperty("broadcastOnFluctuation", false);
            file.setProperty("checkVersion", true);
            file.save();
            System.out.println("[StockExchange] Configuration file created with default values!");
        }
        
        //Get configs
        flucsEnabled = file.getBoolean("naturalFluctuations", true);
        broadcast = file.getBoolean("broadcastOnFluctuation", true);
        checkVersion = file.getBoolean("checkVersion", true);
        
        delay = file.getInt("fluctuationDelay", delay);
        max = file.getDouble("maxFluctuation", max);
        min = file.getDouble("minFluctuation", min);
    }
    
}
