package me.ChrizC.stockexchange;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

public class StockExchangeListener {
    
    protected StockExchange plugin;
    
    public StockExchangeListener() {
        plugin = (StockExchange) Bukkit.getServer().getPluginManager().getPlugin("StockExchange");
        plugin.listeners.add(this);
    }
    
    public void onStockDecrease(String marketName, double amount) {}
    public void onStockIncrease(String marketName, double amount) {}
    public void onStockAddition(String marketName, double initialPrice) {}
    public void onStockRemoval(String marketName, double finalPrice) {}
    public void onStockPurchase(Player player, String marketName, int amount, double price) {}
    public void onStockSale(Player player, String marketName, int amount, double price) {}
    public void onStockLimitChange(String marketName, int newLimit) {}
    public void onPluginRollback(int number) {}
    public void onPluginUndo() {}
    public void onPlayerGifting(Player player, String receiver, String marketName, int amount) {}
    public void onPlayerRefund(String playerName, String marketName, int amount, double price) {}
    public void onStockPrivate(String marketName) {}
    public void onStockPublic(String marketName) {}
    
}
