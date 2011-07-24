package me.chrizc.stockexchange;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

import me.chrizc.stockexchange.exceptions.*;

public class StockExchangeHandler {
    
    StockExchange plugin;
    SEConfig config;
    
    public StockExchangeHandler(StockExchange instance) {
        plugin = instance;
        config = plugin.config;
    }
    
    /*What's the stock ownership limit?
    //public int getStockLimit(String marketName) { return config.checkLimit(marketName); }
    
    //What's the default limit?
    //public int getDefaultLimit() { return config.defaultLimit; }
    
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
    public boolean isPrivate(String marketName) { return config.privateStocks.contains(marketName); }*/
    
    public void decreaseStockValue(CommandSender sender, String marketName, double amount) throws NonExistantMarketException, AttemptedNegativeException, QueryException {
      String checkQuery = "SELECT COUNT(*) AS 'Count', price FROM market WHERE name = '" + marketName + "' LIMIT 1;";
        ResultSet checkResult = plugin.doQuery(checkQuery);
        
        try {
            if (checkResult.next() && checkResult.getInt("Count") == 1) {
                if (amount <= checkResult.getDouble("price")) {
                    String query = "UPDATE market SET price = price - " + amount + " WHERE name = '" + marketName + "' LIMIT 1;";
                    if (plugin.updateQuery(query) == false) {
                        throw new QueryException("Unspecified error with SQL update! Alert the server admin.");
                    }
                } else {
                    throw new AttemptedNegativeException("You can't send a market into negatives!");
                }
            } else {
                throw new NonExistantMarketException("That market does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*for (StockExchangeListener m : plugin.listeners) {
            m.onStockDecrease(marketName, amount);
        }*/
    }
    
    public void increaseStockValue(CommandSender sender, String marketName, double amount) throws NonExistantMarketException, QueryException {
        String checkQuery = "SELECT COUNT(*) AS 'Count', price FROM market WHERE name = '" + marketName + "' LIMIT 1;";
        ResultSet checkResult = plugin.doQuery(checkQuery);
        
        try {
            if (checkResult.next() && checkResult.getInt("Count") == 1) {
                String query = "UPDATE market SET price = price + " + amount + " WHERE name = '" + marketName + "' LIMIT 1;";
                if (plugin.updateQuery(query) == false) {
                    throw new QueryException("Unspecified error with SQL update! Alert the server admin.");
                }
            } else {
                throw new NonExistantMarketException("That market does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*for (StockExchangeListener m : plugin.listeners) {
            m.onStockIncrease(marketName, amount);
        }  */ 
    }
    
    /*public void addMarket(String marketName, double initAmount) throws ExistantMarketException, AttemptedNegativeException {
        if (initAmount < 0) {
            throw new AttemptedNegativeException("You cannot define a market with negative stock prices.");
        }
        if (plugin.market.containsKey(marketName)) {
            throw new ExistantMarketException("That market already exists.");
        }
        
        plugin.market.put(marketName, initAmount);
        config.file.load();
        config.file.setProperty("stocks.limits." + marketName, 0);
        config.file.save();
        for (StockExchangeListener m : plugin.listeners) {
            m.onStockAddition(marketName, initAmount);
        }
    }
    
    public void removeMarket(String marketName, double initAmount) throws NonExistantMarketException {
        if (plugin.market.containsKey(marketName)) {
            throw new NonExistantMarketException("That market doesn't exist.");
        }
        
        List<String> forRemoval = new ArrayList<String>();
        
        double amount = plugin.market.get(marketName);
        Iterator<String> i = plugin.stockOwnership.keySet().iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.contains("_" + marketName)) {
                if (config.refundOnRemoval == true) {
                    String playerName = s.replace("_" + marketName, "");
                    int num = plugin.stockOwnership.get(s);
                    double refund = num * plugin.market.get(marketName);
                    plugin.Method.getAccount(playerName).add(refund);
                    for (StockExchangeListener m : plugin.listeners) {
                        m.onPlayerRefund(playerName, marketName, num, plugin.market.get(marketName));
                    }
                }
                forRemoval.add(s);
            }
        }
        Iterator<String> i2 = forRemoval.iterator();
        while (i2.hasNext()) {
            String s2 = i2.next();
            plugin.stockOwnership.remove(s2);
        }

        plugin.market.remove(marketName);
        config.file.load();
        if (config.file.getProperty("stocks.limits." + marketName) != null) {
            config.file.removeProperty("stocks.limits." + marketName);
        }
        config.file.save();
        for (StockExchangeListener m : plugin.listeners) {
            m.onStockRemoval(marketName, amount);
        }
    }
    
    public void makePrivate(String marketName) throws NonExistantMarketException {
        if (!plugin.market.containsKey(marketName)) {
            throw new NonExistantMarketException("This market doesn't exist.");
        }
        
        config.file.load();
        config.file.setProperty("stocks.private." + marketName, true);
        config.file.save();
        config.privateStocks = config.file.getKeys("stocks.private");
        config.numOfPrivateStocks = config.privateStocks.size();
        for (StockExchangeListener m : plugin.listeners) {
            m.onStockPrivate(marketName);
        }
    }
    
    public void makePublic(String marketName) throws NonExistantMarketException {
        if (!plugin.market.containsKey(marketName)) {
            throw new NonExistantMarketException("This market doesn't exist.");
        }
        
        config.file.load();
        if (config.file.getProperty("stocks.private." + marketName) != null) {
            config.file.removeProperty("stocks.private." + marketName);
        }
        config.file.save();
        config.privateStocks = config.file.getKeys("stocks.private");
        config.numOfPrivateStocks = config.privateStocks.size();
        for (StockExchangeListener m : plugin.listeners) {
            m.onStockPublic(marketName);
        }
    }
    
    public void buyStocks(Player player, String marketName, int amount) throws NonExistantMarketException, OwnershipException {
        if (!plugin.market.containsKey(marketName)) {
            throw new NonExistantMarketException("That market does not exist.");
        }
        
        int i;
        if (plugin.stockOwnership.get(player.getName() + "_" + marketName) == null) {
            i = 0;
        } else {
            i = plugin.stockOwnership.get(player.getName() + "_" + marketName);
        }
        int limitCheck = i + amount;
        if (config.checkLimit(marketName) != 0 && (limitCheck > config.checkLimit(marketName)) == true) {
            throw new OwnershipException("You can't buy that many stocks.");
        } else {
            if (plugin.stockOwnership.containsKey(player.getName() + "_" + marketName)) {
                plugin.stockOwnership.put(player.getName() + "_" + marketName, plugin.stockOwnership.get(player.getName() + "_" + marketName) + amount);
                plugin.Method.getAccount(player.getName()).subtract(amount * plugin.market.get(marketName));
                for (StockExchangeListener m : plugin.listeners) {
                    m.onStockPurchase(player, marketName, amount, plugin.market.get(marketName));
                }
            } else {
                plugin.stockOwnership.put(player.getName() + "_" + marketName, amount);
                plugin.Method.getAccount(player.getName()).subtract(amount * plugin.market.get(marketName));
                for (StockExchangeListener m : plugin.listeners) {
                    m.onStockPurchase(player, marketName, amount, plugin.market.get(marketName));
                }
            }
        }
    }
    
    public void sellStocks(Player player, String marketName, int amount) throws NonExistantMarketException, OwnershipException {
        if (!plugin.stockOwnership.containsKey(player.getName() + "_" + marketName)) {
            throw new OwnershipException("You don't have any stocks to sell.");
        } else {
            if (amount > plugin.stockOwnership.get(player.getName() + "_" + marketName)) {
                throw new OwnershipException("You don't have enough stocks to sell.");
            } else if (amount == plugin.stockOwnership.get(player.getName() + "_" + marketName)) {
                plugin.stockOwnership.remove(player.getName() + "_" + marketName);
                plugin.Method.getAccount(player.getName()).add(amount * plugin.market.get(marketName));
                for (StockExchangeListener m : plugin.listeners) {
                    m.onStockSale(player, marketName, amount, plugin.market.get(marketName));
                }
            } else {
                plugin.stockOwnership.put(player.getName() + "_" + marketName, plugin.stockOwnership.get(player.getName() + "_" + marketName) - amount);
                plugin.Method.getAccount(player.getName()).add(amount * plugin.market.get(marketName));
                for (StockExchangeListener m : plugin.listeners) {
                    m.onStockSale(player, marketName, amount, plugin.market.get(marketName));
                }
            }
        }
    }*/
    
}
