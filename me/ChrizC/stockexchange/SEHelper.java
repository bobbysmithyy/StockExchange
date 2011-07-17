package me.ChrizC.stockexchange;

import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class SEHelper {
    
    private final StockExchange plugin;
    
    public SEHelper(StockExchange instance) {
        plugin = instance;
    }
    
    public void helpMe(CommandSender event, String section) {
        Player player = (Player)event;
        PluginDescriptionFile pdfFile = plugin.getDescription();
        event.sendMessage(ChatColor.WHITE + "StockExchange v" + ChatColor.YELLOW + pdfFile.getVersion());
        event.sendMessage(ChatColor.WHITE + "[] Required argument, () Optional Argument");
        event.sendMessage(" ");
        if (section.equals("admin")) {
            if (plugin.permissionHandler != null){ 
                if (plugin.permissionHandler.has(player, "stocks.admin.add")) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "add " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] (" + ChatColor.WHITE + "starting price" + ChatColor.DARK_PURPLE + ") " + ChatColor.YELLOW + "Add a market.");
                }
                if (plugin.permissionHandler.has(player, "stocks.admin.remove")) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "remove " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Remove a market.");
                }
                if (plugin.permissionHandler.has(player, "stocks.admin.modify")) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "increase " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] [" + ChatColor.WHITE + "amount" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Increase a market's share  price.");
                }
                if (plugin.permissionHandler.has(player, "stocks.admin.modify")) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "decrease " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] [" + ChatColor.WHITE + "amount" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Decrease a market's share price.");
                }
                if (plugin.permissionHandler.has(player, "stocks.admin.limit")) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "limit " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] [" + ChatColor.WHITE + "limit" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Change the ownership limit of a");
                    event.sendMessage(ChatColor.YELLOW + "market.");
                }
                if (plugin.permissionHandler.has(player, "stocks.admin.private")) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "private " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Privatise a market.");
                }
                if (plugin.permissionHandler.has(player, "stocks.admin.public")) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "public " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Publicise a market.");
                }
            } else {
                if (player.isOp()) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "add " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] (" + ChatColor.WHITE + "starting price" + ChatColor.DARK_PURPLE + ") " + ChatColor.YELLOW + "Add a market.");
                }
                if (player.isOp()) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "remove " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Remove a market.");
                }
                if (player.isOp()) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "increase " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] [" + ChatColor.WHITE + "amount" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Increase a market's share  price.");
                }
                if (player.isOp()) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "decrease " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] [" + ChatColor.WHITE + "amount" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Decrease a market's share price.");
                }
                if (player.isOp()) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "limit " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] [" + ChatColor.WHITE + "limit" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Change the ownership limit of a");
                    event.sendMessage(ChatColor.YELLOW + "market.");
                }
                if (player.isOp()) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "private " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Privatise a market.");
                }
                if (player.isOp()) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "public " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Publicise a market.");
                }
            }
            event.sendMessage(" ");
        } else if (section.equals("user")) {
            event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "help " + ChatColor.YELLOW + "Shows this help dialog.");
            if (plugin.permissionHandler != null) {
                if (plugin.permissionHandler.has(player, "stocks.users.top5")) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "top5 " + ChatColor.YELLOW + "Show the top 5 highest priced markets.");
                }
                if (plugin.permissionHandler.has(player, "stocks.users.lookup")) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "lookup " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Lookup a market's price and ownership  limit.");
                }
                if (plugin.permissionHandler.has(player, "stocks.users.trade")) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "buy " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] [" + ChatColor.WHITE + "amount/'max'" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Buy shares of a market.");
                }
                if (plugin.permissionHandler.has(player, "stocks.users.trade")) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "sell " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] [" + ChatColor.WHITE + "amount/'all'" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Sell shares of a market.");
                }
                if (plugin.permissionHandler.has(player, "stocks.users.gift")) {
                    event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "gift " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "player name" + ChatColor.DARK_PURPLE + "] [" + ChatColor.WHITE + "market name" + ChatColor.DARK_PURPLE + "] [" + ChatColor.WHITE + "amount" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Give shares of a market to a user.");
                }
            } else {
                event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "top5 " + ChatColor.YELLOW + "Show the top 5 highest priced markets.");
                event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "lookup " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Lookup a market's price and ownership  limit.");
                event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "buy " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] [" + ChatColor.WHITE + "amount/'max'" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Buy shares of a market.");
                event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "sell " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "name" + ChatColor.DARK_PURPLE + "] [" + ChatColor.WHITE + "amount/'all'" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Sell shares of a market.");
                event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "gift " + ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "player name" + ChatColor.DARK_PURPLE + "] [" + ChatColor.WHITE + "market name" + ChatColor.DARK_PURPLE + "] [" + ChatColor.WHITE + "amount" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + "Give shares of a market to a user.");
            }
            event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "portfolio " + ChatColor.YELLOW + "Display all the stocks you own.");
            event.sendMessage(ChatColor.DARK_PURPLE + "/stocks " + ChatColor.LIGHT_PURPLE + "list " + ChatColor.DARK_PURPLE + "(" + ChatColor.WHITE + "amount" + ChatColor.DARK_PURPLE + ") " + ChatColor.YELLOW + "Display a list of the available public markets, in alphabetical order.");
            event.sendMessage(" ");
        }
    }
    
    public void consoleHelpMe(CommandSender event) {
        PluginDescriptionFile pdfFile = plugin.getDescription();
        event.sendMessage("StockExchange v" + pdfFile.getVersion());
        event.sendMessage("[] Required argument, () Optional Argument");
        event.sendMessage(" ");
        event.sendMessage("stocks add [name] (starting price)   Add a market.");
        event.sendMessage("stocks remove [name]   Remove a market.");
        event.sendMessage("stocks increase [name] [amount]   Increase a market's share price.");
        event.sendMessage("stocks decrease [name] [amount]   Decrease a market's share price.");
        event.sendMessage("stocks limit [name] [limit]   Change the ownership limit of a market.");
        event.sendMessage("stocks private [name]   Privatise a market.");
        event.sendMessage("stocks public [name]   Publicise a market.");
        event.sendMessage("stocks help   Shows this help dialog.");
        event.sendMessage("stocks top5   Show the top 5 highest priced markets.");
        event.sendMessage("stocks lookup [name]   Lookup a market's price and ownership limit.");
        event.sendMessage("stocks list (amount)   Display a list of the available public markets, in alphabetical order.");
        event.sendMessage("stocks top5   Show the top 5 highest priced markets.");
    }
}
