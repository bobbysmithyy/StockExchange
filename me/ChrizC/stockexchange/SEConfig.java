package me.ChrizC.stockexchange;

import java.io.*;
import java.util.List;
import java.util.Iterator;

import org.bukkit.util.config.Configuration;

public class SEConfig {
    
    private final StockExchange plugin;
    
    Configuration file;
    
    Boolean checkVersion;
    Boolean verbose;
    Boolean refundOnRemoval;
    
    List<String> stocksLims;
    List<String> privateStocks;
    int numOfPrivateStocks;
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
            file.setProperty("checkVersion", true);
            file.setProperty("verbose", false);
            file.setProperty("refundOnRemoval", true);
            file.save();
            System.out.println("[StockExchange] Configuration file created with default values!");
        }
        
        //Updated?
        if (file.getProperty("verbose") == null) {
            file.setProperty("verbose", false);
        }
        if (file.getProperty("refundOnRemoval") == null) {
            file.setProperty("refundOnRemoval", true);
        }
        file.save();
        
        //Get configs
        checkVersion = file.getBoolean("checkVersion", true);
        refundOnRemoval = file.getBoolean("refundOnRemoval", true);
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
            
            file.setProperty("stocks.private.example", true);
            
            file.setProperty("defaultLimit", 0);
            
            file.save();
            System.out.println("[StockExchange] Stocks configuration file created with default values!");
        }
        
        //Updated?
        if (file.getKeys("stocks.private") == null) {
            file.setProperty("stocks.private.example", true);
        }
        
        defaultLimit = file.getInt("defaultLimit", 0);
        privateStocks = file.getKeys("stocks.private");
        numOfPrivateStocks = privateStocks.size();
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
