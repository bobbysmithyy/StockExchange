package me.ChrizC.stockexchange;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.TreeSet;
import java.util.Iterator;
import org.bukkit.ChatColor;

public class SEMarketHandler {
    
    private final StockExchange plugin;
    
    public SEMarketHandler(StockExchange instance) {
        plugin = instance;
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
                sender.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] Added stock " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + " with stock price " + ChatColor.YELLOW + plugin.Method.format(amount));
            }
        }
    }
    
    public void remove(CommandSender sender, String marketName) {
        if (!plugin.market.containsKey(marketName)) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] That stock doesn't exist!");
        } else {
            plugin.market.remove(marketName);
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
    
    public void buymax(Player player, String marketName) {
        if (!plugin.market.containsKey(marketName)) {
            player.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] That stock doesn't exist!");
        } else {
            Double calc = plugin.Method.getAccount(player.getName()).balance() / plugin.market.get(marketName);
            int amount = (int)Math.floor(calc);
            if (plugin.Method.getAccount(player.getName()).hasEnough(amount * plugin.market.get(marketName))) {
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
                sender.sendMessage(ChatColor.DARK_PURPLE + "[Stocks] Removed " + ChatColor.YELLOW + plugin.Method.format(amount) + ChatColor.DARK_PURPLE + " to the " + ChatColor.YELLOW + marketName + ChatColor.DARK_PURPLE + " stock price.");
                lookup(sender, marketName);
            }
        }
    }
    
}
