package me.ChrizC.stockexchange;

import java.util.HashMap;

public class StockExchangeHandler {
    
    StockExchange plugin;
    SEScheduleHandler schedule;
    SEConfig config;
    SEFileHandler fileHandle;
    
    public StockExchangeHandler(StockExchange instance) {
        plugin = instance;
        schedule = plugin.scheduleHandler;
        config = plugin.config;
        fileHandle = plugin.fileHandler;
    }
    
    //Check if fluctuations are happening.
    public boolean isFluctating() { return schedule.isFluctuating; }
    
    //Do we broadcast?
    public boolean broadcastOnFluctuate() { return schedule.broadcasting; }
    
    //What's the maximum fluctuation?
    public double getMaxFluctuation() { return schedule.maximum; }
    
    //What's the minimum fluctuation?
    public double getMinFluctuation() { return schedule.minimum; }
    
    //What's the stock ownership limit?
    public int getStockLimit(String marketName) { return config.checkLimit(marketName); }
    
    //What's the default limit?
    public int getDefaultLimit() { return config.defaultLimit; }
    
    //Is the plugin in verbose mode?
    public boolean isVerbose() { return config.verbose; }
    
    //Does the specified backup exist?
    public boolean backupExists(int num) { return fileHandle.checkBackup(num); }
    
    //Get a stock's value.
    public double getStockValue(String marketName) { return plugin.market.get(marketName); }
    
    //Get the amount of stocks a player owns.
    public int getStockOwnership(String playerName, String marketName) {
        if (plugin.stockOwnership.containsKey(playerName + "_" + marketName)) {
            return plugin.stockOwnership.get(playerName + "_" + marketName);
        }
        return 0;
    }
    
    //Get all stocks.
    public HashMap<String, Double> getStocks() { return plugin.market; }
    
    //Is stock private?
    public boolean isPrivate(String marketName) { return config.privateStocks.contains(marketName); }
    
}
