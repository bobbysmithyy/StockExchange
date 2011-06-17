package me.ChrizC.stockexchange;

import java.io.*;
import java.util.List;
import java.util.Iterator;

import org.bukkit.util.config.Configuration;

public class SEConfig {
    
    private final StockExchange plugin;
    
    Configuration file;
    
    Boolean flucsEnabled;
    Boolean broadcast;
    Boolean checkVersion;
    Boolean verbose;
    
    Integer delay = 15;
    Double max = 4D;
    Double min = 2D;
    
    List<String> stocksLims;
    int numOfStockLims;
    int defaultLimit;
    
    public SEConfig (StockExchange instance) {
        plugin = instance;
    }
    
    public void doConfig() {
        file = new Configuration(new File(plugin.getDataFolder(), "config.yml"));
        file.load();
        
        verbose = file.getBoolean("verbose", false);
        
        if (new File(plugin.getDataFolder(), "config.yml").exists()) {
            if (verbose == true) {
                System.out.println("[StockExchange] Configuration file loaded!");
            }
        } else {
            file.setProperty("naturalFluctuations", true);
            file.setProperty("fluctuationDelay", 15);
            file.setProperty("maxFluctuation", 4);
            file.setProperty("minFluctuation", 2);
            file.setProperty("broadcastOnFluctuation", false);
            file.setProperty("checkVersion", true);
            file.setProperty("verbose", false);
            file.save();
            System.out.println("[StockExchange] Configuration file created with default values!");
        }
        
        //Updated?
        if (file.getProperty("verbose") == null) {
            file.setProperty("verbose", false);
        }
        
        //Get configs
        flucsEnabled = file.getBoolean("naturalFluctuations", true);
        broadcast = file.getBoolean("broadcastOnFluctuation", true);
        checkVersion = file.getBoolean("checkVersion", true);
        
        delay = file.getInt("fluctuationDelay", delay);
        max = file.getDouble("maxFluctuation", max);
        min = file.getDouble("minFluctuation", min);
    }
    
    public void configStocks() {
        file = new Configuration(new File(plugin.getDataFolder(), "stocks.yml"));
        file.load();
        
        if (new File(plugin.getDataFolder(), "stocks.yml").exists()) {
            if (verbose == true) {
                System.out.println("[StockExchange] Stocks configuration file loaded!");
            }
        } else {
            //Do stocks already exist?
            if (plugin.market.size() > 0) {
                Iterator<String> i = plugin.market.keySet().iterator();
                while (i.hasNext()) {
                    String s = i.next();
                    file.setProperty("stocks.limits." + s, 0);
                }
            } else {
                file.setProperty("stocks.limits.example", 250);
            }
            
            file.setProperty("defaultLimit", 0);
            
            file.save();
            System.out.println("[StockExchange] Stocks configuration file created with default values!");
        }
        
        
        
        defaultLimit = file.getInt("defaultLimit", 0);
        stocksLims = file.getKeys("stocks.limits");
        numOfStockLims = stocksLims.size();
    }
    
    public int checkLimit(String stock) {
        if (file.getProperty("stocks.limits." + stock) != null) {
            return file.getInt("stocks.limits." + stock, defaultLimit);
        }
        
        return defaultLimit;
    }
    
}
