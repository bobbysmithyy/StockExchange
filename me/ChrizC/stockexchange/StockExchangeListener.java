package me.ChrizC.stockexchange;

import org.bukkit.entity.Player;

public class StockExchangeListener {
    
    protected StockExchange plugin;
    
    public StockExchangeListener(StockExchange instance) {
        plugin = instance;
        plugin.listeners.add(this);
    }
    
    public void onStockFluctuate(String marketName, double amount, int negOrPos) {}
    public void onStockDecrease(String marketName, double amount) {}
    public void onStockIncrease(String marketName, double amount) {}
    public void onStockAddition(String marketName, double initialPrice) {}
    public void onStockRemoval(String marketName, double finalPrice) {}
    public void onStockPurchase(Player player, String marketName, int amount, double price) {}
    public void onStockSale(Player player, String marketName, int amount, double price) {}
    public void onStockLimitChange(String marketName, int newLimit) {}
    public void onPluginRollback(int number) {}
    public void onPluginUndo() {}
    
}
