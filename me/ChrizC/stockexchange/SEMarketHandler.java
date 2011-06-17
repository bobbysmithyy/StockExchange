package me.ChrizC.stockexchange;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.util.TreeSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class SEMarketHandler {
    
    private final StockExchange plugin;
    SEConfig config;
    
    public SEMarketHandler(StockExchange instance, SEConfig config) {
        plugin = instance;
        this.config = config;
    }
    
    String name;
    Double price;
    
    public void top5(CommandSender event) {
        TreeSet tree = new TreeSet(plugin.market.values());
        event.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] Top 5 stock prices:");
        for (int i = 0; i < 5; i++) {
            price = (Double)tree.pollLast();
            Iterator<String> iterator = plugin.market.keySet().iterator();
            while (iterator.hasNext()) {
                String thisName = iterator.next();
                if (plugin.market.get(thisName).equals(price)) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] " + (i + 1) + ") " + ChatColor.YELLOW + thisName + ChatColor.DARK_PURPLE + " at " + ChatColor.YELLOW + plugin.Method.format(price));
                }
            }
        }
    }
    
    public void add(CommandSender sender, String marketName, Double amount) {
        if (plugin.market.containsKey(marketName)) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] That stock already exists!");
        } else {
            if (amount != null) {
                plugin.market.put(marketName, amount);
                config.file.load();
                config.file.setProperty("stocks.limits." + marketName, 0);
                config.file.save();
                sender.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] Added stock " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + " with stock price " + ChatColor.YELLOW + plugin.Method.format(amount));
            }
        }
    }
    
    public void remove(CommandSender sender, String marketName) {
        if (!plugin.market.containsKey(marketName)) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] That stock doesn't exist!");
        } else {
            plugin.market.remove(marketName);
            config.file.load();
            if (config.file.getProperty("stocks.limits." + marketName) != null) {
                config.file.removeProperty("stocks.limits." + marketName);
            }
            config.file.save();
            sender.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] Removed stock " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + "!");
        }
    }
    
    public void lookup(CommandSender sender, String marketName) {
        if (!plugin.market.containsKey(marketName)) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] That stock doesn't exist!");
        } else {
            sender.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] Current stock price for " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + " is " + ChatColor.YELLOW + plugin.Method.format(plugin.market.get(marketName)) + ChatColor.DARK_PURPLE + "!");
        }
    }
    
    public void buy(Player player, String marketName, Integer amount) {
        if (!plugin.market.containsKey(marketName)) {
            player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] That stock doesn't exist!");
        } else {
            if (plugin.Method.getAccount(player.getName()).hasEnough(amount * plugin.market.get(marketName))) {
                int i;
                if (plugin.stockOwnership.get(player.getName() + "_" + marketName) == null) {
                    i = 0;
                } else {
                    i = plugin.stockOwnership.get(player.getName() + "_" + marketName);
                }
                int limitCheck = i + amount;
                if (config.checkLimit(marketName) != 0 && (limitCheck > config.checkLimit(marketName)) == true) {
                    player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] You can't buy that many stocks! The limit for " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + " is " + ChatColor.YELLOW + config.checkLimit(marketName) + ChatColor.DARK_PURPLE + " stocks.");
                } else {
                    if (plugin.stockOwnership.containsKey(player.getName() + "_" + marketName)) {
                        plugin.stockOwnership.put(player.getName() + "_" + marketName, plugin.stockOwnership.get(player.getName() + "_" + marketName) + amount);
                        plugin.Method.getAccount(player.getName()).subtract(amount * plugin.market.get(marketName));
                        player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] Bought " + ChatColor.YELLOW + amount + ChatColor.DARK_PURPLE + " of " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + " for " + ChatColor.YELLOW + plugin.Method.format(amount * plugin.market.get(marketName)) + ChatColor.DARK_PURPLE + "!");
                    } else {
                        plugin.stockOwnership.put(player.getName() + "_" + marketName, amount);
                        plugin.Method.getAccount(player.getName()).subtract(amount * plugin.market.get(marketName));
                        player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] Bought " + ChatColor.YELLOW + amount + ChatColor.DARK_PURPLE + " of " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + " for " + ChatColor.YELLOW + plugin.Method.format(amount * plugin.market.get(marketName)) + ChatColor.DARK_PURPLE + "!");
                    }
                }
            } else {
                player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] You don't have enough money!");
            }
        }
    }
    
    public void buymax(Player player, String marketName) {
        if (!plugin.market.containsKey(marketName)) {
            player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] That stock doesn't exist!");
        } else {
            Double calc = plugin.Method.getAccount(player.getName()).balance() / plugin.market.get(marketName);
            int amount = (int)Math.floor(calc);
            if (plugin.Method.getAccount(player.getName()).hasEnough(amount * plugin.market.get(marketName))) {
                if (config.checkLimit(marketName) != 0) {
                    if (plugin.stockOwnership.get(player.getName() + "_" + marketName) == null) {
                        amount = config.checkLimit(marketName);
                    } else if ((amount + plugin.stockOwnership.get(player.getName() + "_" + marketName)) > config.checkLimit(marketName)) {
                        amount = config.checkLimit(marketName) - plugin.stockOwnership.get(player.getName() + "_" + marketName);
                    }
                }
                if (plugin.stockOwnership.containsKey(player.getName() + "_" + marketName)) {
                    plugin.stockOwnership.put(player.getName() + "_" + marketName, plugin.stockOwnership.get(player.getName() + "_" + marketName) + amount);
                    plugin.Method.getAccount(player.getName()).subtract(amount * plugin.market.get(marketName));
                    player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] Bought " + ChatColor.YELLOW + amount + ChatColor.DARK_PURPLE + " of " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + " for " + ChatColor.YELLOW + plugin.Method.format(amount * plugin.market.get(marketName)) + ChatColor.DARK_PURPLE + "!");
                } else {
                    plugin.stockOwnership.put(player.getName() + "_" + marketName, amount);
                    plugin.Method.getAccount(player.getName()).subtract(amount * plugin.market.get(marketName));
                    player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] Bought " + ChatColor.YELLOW + amount + ChatColor.DARK_PURPLE + " of " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + " for " + ChatColor.YELLOW + plugin.Method.format(amount * plugin.market.get(marketName)) + ChatColor.DARK_PURPLE + "!");
                }
            }
        }
    }
    
    public void sell(Player player, String marketName, Integer amount) {
        if (!plugin.market.containsKey(marketName)) {
            player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] That stock doesn't exist!");
        } else {
            if (!plugin.stockOwnership.containsKey(player.getName() + "_" + marketName)) {
                player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] You don't have any " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + " stock to sell.");
            } else {
                if (amount > plugin.stockOwnership.get(player.getName() + "_" + marketName)) {
                    player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] You don't have that much " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + " stock to sell.");
                } else if (amount.equals(plugin.stockOwnership.get(player.getName() + "_" + marketName))) {
                    plugin.stockOwnership.remove(player.getName() + "_" + marketName);
                    plugin.Method.getAccount(player.getName()).add(amount * plugin.market.get(marketName));
                    player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] You sold " + ChatColor.YELLOW + amount + ChatColor.DARK_PURPLE + " of " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + " stock for " + ChatColor.YELLOW + plugin.Method.format(amount * plugin.market.get(marketName)) + ChatColor.DARK_PURPLE + ".");
                } else {
                    plugin.stockOwnership.put(player.getName() + "_" + marketName, plugin.stockOwnership.get(player.getName() + "_" + marketName) - amount);
                    plugin.Method.getAccount(player.getName()).add(amount * plugin.market.get(marketName));
                    player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] You sold " + ChatColor.YELLOW + amount + ChatColor.DARK_PURPLE + " of " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + " stock for " + ChatColor.YELLOW + plugin.Method.format(amount * plugin.market.get(marketName)) + ChatColor.DARK_PURPLE + ".");
                }
            }
        }
    }
    
    public void sellall(Player player, String marketName) {
        if (!plugin.market.containsKey(marketName)) {
            player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] That stock doesn't exist!");
        } else {
            if (!plugin.stockOwnership.containsKey(player.getName() + "_" + marketName)) {
                player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] You don't have any " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + " stock to sell.");
            } else {
                Integer amount = plugin.stockOwnership.get(player.getName() + "_" + marketName);
                plugin.stockOwnership.remove(player.getName() + "_" + marketName);
                plugin.Method.getAccount(player.getName()).add(amount * plugin.market.get(marketName));
                player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] You sold " + ChatColor.YELLOW + amount + ChatColor.DARK_PURPLE + " of " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + " stock for " + ChatColor.YELLOW + plugin.Method.format(amount * plugin.market.get(marketName)) + ChatColor.DARK_PURPLE + ".");
            }
        }
    }
    
    public void increase(CommandSender sender, String marketName, Double amount) {
        if (!plugin.market.containsKey(marketName)) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] That stock doesn't exist!");
        } else {
            plugin.market.put(marketName, plugin.market.get(marketName) + amount);
            sender.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] Added " + ChatColor.YELLOW + plugin.Method.format(amount) + ChatColor.DARK_PURPLE + " to the " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + " stock price.");
            lookup(sender, marketName);
        }
    }
    
    public void decrease(CommandSender sender, String marketName, Double amount) {
        if (!plugin.market.containsKey(marketName)) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] That stock doesn't exist!");
        } else {
            if (amount > plugin.market.get(marketName)) {
                sender.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] You can't send a stock into negatives!");
            } else {
                plugin.market.put(marketName, plugin.market.get(marketName) - amount);
                sender.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] Removed " + ChatColor.YELLOW + plugin.Method.format(amount) + ChatColor.DARK_PURPLE + " from the " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + " stock price.");
                lookup(sender, marketName);
            }
        }
    }
    
    public void portfolio(Player player) {
        Iterator<String> i = plugin.stockOwnership.keySet().iterator();
        int x = 0;
        List<String> array = new ArrayList<String>();
        while (i.hasNext()) {
            String s = i.next();
            if (s.startsWith(player.getName())) {
                array.add(s);
                x++;
            }
        }
        
        if (x == 0) {
            player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] You don't own any stocks.");
        } else {
            player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] Your stock portfolio:");
            Iterator<String> i2 = array.iterator();
            while (i2.hasNext()) {
                String s2 = i2.next();
                player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] You own " + ChatColor.YELLOW + plugin.stockOwnership.get(s2) + ChatColor.DARK_PURPLE + " stocks in " + ChatColor.YELLOW + s2.replace(player.getName() + "_", ""));
                x--;
            }
        }
    }
    
}
