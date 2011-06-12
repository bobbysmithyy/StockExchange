package me.ChrizC.stockexchange;

import org.bukkit.Bukkit;

import java.util.Random;
import java.util.Iterator;
import org.bukkit.ChatColor;

public class SEScheduleHandler {
    
    private final StockExchange plugin;
    
    int taskId = 0;
    int size;
    Double maximum;
    Double minimum;
    Boolean broadcasting;
    
    public SEScheduleHandler (StockExchange instance) {
        plugin = instance;
    }
    
    public void fluctuate(Double min, Double max, int delay, Boolean broadcast) {
        maximum = max;
        minimum = min;
        broadcasting = broadcast;
        taskId = Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                size = plugin.market.size();
                if (size != 0) {
                    Random random = new Random();
                    int randomInt = random.nextInt(size);
                    int negOrPos = random.nextInt(2);
                    double randomFluc = minimum + (random.nextDouble() * (maximum - minimum));
                    int i = 0;
                    Iterator<String> iterator = plugin.market.keySet().iterator();
                    while (iterator.hasNext()) { 
                        String s = iterator.next();
                        if (i == randomInt) {
                            if (negOrPos == 1) {
                                plugin.market.put(s, plugin.market.get(s) + randomFluc);
                                if (broadcasting == true) {
                                    Bukkit.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "[Stocks] The " + ChatColor.YELLOW + s + ChatColor.DARK_PURPLE + " stock has risen by " + ChatColor.YELLOW + economyManager.economy.format(randomFluc));
                                }
                            } else {
                                plugin.market.put(s, plugin.market.get(s) - randomFluc);
                                if (broadcasting == true) {
                                    Bukkit.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "[Stocks] The " + ChatColor.YELLOW + s + ChatColor.DARK_PURPLE + " stock has fallen by " + ChatColor.YELLOW + economyManager.economy.format(randomFluc));
                                }
                            }
                        }
                    
                        i++;
                    }
                }
            }
        }, delay * 20, delay * 20);
    }
    
}
