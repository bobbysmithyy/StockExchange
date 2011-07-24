package me.chrizc.stockexchange;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;

public class SEMarketHandler {
    
    private final StockExchange plugin;
    SEConfig config;
    
    public ChatColor PURPLE = ChatColor.DARK_PURPLE;
    public ChatColor YELLOW = ChatColor.YELLOW;
    public String PREFIX = PURPLE + "[Stocks] ";
    public String ERR_PREFIX = ChatColor.RED + "[Stocks] ";
    
    public SEMarketHandler(StockExchange instance, SEConfig config) {
        plugin = instance;
        this.config = config;
    }
    
    public void top5(CommandSender event) {
        boolean permission = true;
        
        if (event instanceof Player) {
            permission = plugin.checkPermissions("stocks.users.top5", (Player) event);
        }
        
        if (permission == true) {
            String checkQuery = "SELECT COUNT(*) AS 'Count' FROM market ORDER BY price DESC LIMIT 5;";
            ResultSet checkResult = plugin.doQuery(checkQuery);
            String query = "SELECT name, price, private FROM market ORDER BY price DESC LIMIT 5;";
            ResultSet result = plugin.doQuery(query);

            try {
                if (checkResult.next()) {
                    if (result != null && checkResult.getInt("Count") > 0) {
                        int i = 1;
                        if (event instanceof Player) {
                            event.sendMessage(" ");
                        }
                        event.sendMessage(PREFIX + "Top 5 markets: (sorted by Price)");
                        while (result.next()) {
                            String name = result.getString("name");
                            int priv = result.getInt("private");
                            String price;
                            if (priv == 1) {
                                if (event instanceof Player) {
                                    if (plugin.checkPermissions("stocks.users.private." + name, (Player) event) == true) {
                                        price = plugin.Method.format(result.getDouble("price"));
                                    } else {
                                        price = "PRIVATE";
                                    }
                                } else {
                                    price = plugin.Method.format(result.getDouble("price"));
                                }
                            } else {
                                price = plugin.Method.format(result.getDouble("price"));
                            }

                            event.sendMessage(PREFIX + i + ") " + YELLOW + name + PURPLE + " at " + YELLOW + price);
                            i++;
                        }
                        if (event instanceof Player) {
                            event.sendMessage(" ");
                        }
                    } else {
                        event.sendMessage(ERR_PREFIX + "No markets exist!");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void add(CommandSender sender, String marketName, double amount) {
        String checkQuery = "SELECT COUNT(*) AS 'Count' FROM market WHERE name = '" + marketName + "' LIMIT 1;";
        ResultSet checkResult = plugin.doQuery(checkQuery);

        try {
            if (checkResult.next() && checkResult.getInt("Count") < 1) {
                String insertQuery = "INSERT INTO market (name, price, private, owner, lim) VALUES ('" + marketName + "', " + amount + ", 0, 'Server', 0);";
                if (plugin.insertQuery(insertQuery) == true) {
                    sender.sendMessage(PREFIX + "Added stock " + YELLOW + marketName + PURPLE + " with stock price " + YELLOW + plugin.Method.format(amount));
                } else {
                    sender.sendMessage(ERR_PREFIX + "Unexpected error! Notify the server admin.");
                }
            } else {
                sender.sendMessage(ERR_PREFIX + "That market already exists!");
            }
            checkResult.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void remove(CommandSender sender, String marketName) {
        boolean permission = true;
        if (sender instanceof Player) {
            permission = plugin.checkPermissions("stocks.admin.add", (Player) sender);
        }
        
        if (permission == true) {
            String checkQuery = "SELECT COUNT(*) AS 'Count' FROM market WHERE name = '" + marketName + "' LIMIT 1;";
            ResultSet checkResult = plugin.doQuery(checkQuery);
            
            try {
                if (checkResult.next() && checkResult.getInt("Count") == 1) {
                    String removeQuery = "DELETE FROM market WHERE name = '" + marketName + "' LIMIT 1;";
                    if (plugin.deleteQuery(removeQuery) == true) {

                        if (config.refundOnRemoval == true) {
                            String refundQuery = "SELECT owner, market, amount, buyprice FROM ownership WHERE market = '" + marketName + "';";
                            ResultSet refundResult = plugin.doQuery(refundQuery);
                            String marketPriceQuery = "SELECT price FROM market WHERE name = '" + marketName + "' LIMIT 1;";
                            ResultSet priceResult = plugin.doQuery(marketPriceQuery);
                            double price = 0D;

                            if (priceResult.next()) {
                                price = priceResult.getDouble("price");
                            }

                            while (refundResult.next()) {
                                int amount = refundResult.getInt("amount");
                                String owner = refundResult.getString("owner");
                                plugin.Method.getAccount(owner).add(amount * price);

                                Player[] online = Bukkit.getServer().getOnlinePlayers();
                                for (int i = 0; i < online.length; i++) {
                                    if (online[i].getName().equals(owner)) {
                                        online[i].sendMessage(PREFIX + "The market " + YELLOW + marketName + PURPLE + " just closed! Your " + YELLOW + amount + PURPLE + " stocks have been sold at the current market price, for a total of " + YELLOW + plugin.Method.format(amount * price));
                                    }
                                }
                            }
                        }

                        sender.sendMessage(PREFIX + "The market " + YELLOW + marketName + PURPLE + " has been closed.");

                    } else {
                        sender.sendMessage(ERR_PREFIX + "Unexpected error! Notify the server admin.");
                    }
                } else {
                    sender.sendMessage(ERR_PREFIX + "That market does not exist!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void lookup(CommandSender sender, String marketName) {
        String checkQuery = "SELECT COUNT(*) AS 'Count' FROM market WHERE name = '" + marketName + "' LIMIT 1;";
        ResultSet checkResult = plugin.doQuery(checkQuery);
        try {
            if (checkResult.next() && checkResult.getInt("Count") == 1) {
                String query = "SELECT name, price, private, lim FROM market WHERE name = '" + marketName + "' LIMIT 1;";
                ResultSet result = plugin.doQuery(query);
                
                if (result.next()) {
                    double price = result.getDouble("price");
                    int limit = result.getInt("lim");
                    int priv = result.getInt("private");
                    if (priv == 1) {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            if (plugin.checkPermissions("stocks.users.private." + marketName, player) == true) {
                                sender.sendMessage(PREFIX + "STOCK: " + YELLOW + marketName + PURPLE + " PRICE: " + YELLOW + plugin.Method.format(price) + PURPLE + " OWNERSHIP LIMIT: " + YELLOW + limit);
                            } else {
                                sender.sendMessage(PREFIX + "STOCK: " + YELLOW + marketName + PURPLE + " PRICE: " + YELLOW + "PRIVATE" + PURPLE + " OWNERSHIP LIMIT: " + YELLOW + limit);
                            }
                        } else {
                            sender.sendMessage(PREFIX + "STOCK: " + YELLOW + marketName + PURPLE + " PRICE: " + YELLOW + plugin.Method.format(price) + PURPLE + " OWNERSHIP LIMIT: " + YELLOW + limit);
                        }
                    } else {
                        sender.sendMessage(PREFIX + "STOCK: " + YELLOW + marketName + PURPLE + " PRICE: " + YELLOW + plugin.Method.format(price) + PURPLE + " OWNERSHIP LIMIT: " + YELLOW + limit);
                    }
                }
            } else {
                sender.sendMessage(ERR_PREFIX + "That market does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void buy(Player player, String marketName, Integer amount) {
        boolean permission = true;
        String checkMarket = "SELECT COUNT(*) as 'Count', private, price, lim FROM market WHERE name = '" + marketName + "' LIMIT 1;";
        ResultSet marketCheckResult = plugin.doQuery(checkMarket);
        
        try {
            if (marketCheckResult.next() && marketCheckResult.getInt("Count") == 1) {

                if (marketCheckResult.getInt("private") == 1) {
                    if (plugin.checkPermissions("stocks.users.private." + marketName, player) == false) {
                        permission = false;
                    }
                }

                if (permission == true) {                
                    if (marketCheckResult.getInt("lim") >= amount || marketCheckResult.getInt("lim") == 0) {
                        String checkOwnership = "SELECT market, amount, owner, COUNT(*) AS 'Count' FROM ownership WHERE market = '" + marketName + "' AND owner = '" + player.getName().toLowerCase() + "' LIMIT 1;";
                        ResultSet ownershipCheckResult = plugin.doQuery(checkOwnership);

                        if (plugin.Method.getAccount(player.getName()).hasEnough(amount * marketCheckResult.getDouble("price"))) {
                            if (ownershipCheckResult.next()) {
                                if (ownershipCheckResult.getInt("Count") == 1) {
                                    if ((amount + ownershipCheckResult.getInt("amount")) <= marketCheckResult.getInt("lim")) {
                                        String updateQuery = "UPDATE ownership SET amount = amount + " + amount + ", buyprice = " + marketCheckResult.getDouble("price") + " WHERE market = '" + marketName + "' AND owner = '" + player.getName().toLowerCase() + "' LIMIT 1;";

                                        if (plugin.updateQuery(updateQuery) == true) {
                                            plugin.Method.getAccount(player.getName()).subtract(amount * marketCheckResult.getDouble("price"));
                                            player.sendMessage(PREFIX + "Bought " + YELLOW + amount + PURPLE + " of " + YELLOW + marketName + PURPLE + " for " + YELLOW + plugin.Method.format(amount * marketCheckResult.getDouble("price")) + PURPLE + "!");
                                        } else {
                                            player.sendMessage(ERR_PREFIX + "Unspecified error with SQL insertion! Alert the server admin.");
                                        }
                                    } else {
                                        player.sendMessage(ERR_PREFIX + "You can't buy that many shares of this market. The market ownership limit is " + YELLOW + marketCheckResult.getInt("lim"));
                                    }
                                } else {
                                    String updateQuery = "INSERT INTO ownership (owner, market, amount, buyprice) VALUES ('" + player.getName().toLowerCase() + "', '" + marketName + "', " + amount + ", " + marketCheckResult.getDouble("price") + ");";

                                    if (plugin.insertQuery(updateQuery) == true) {
                                        plugin.Method.getAccount(player.getName()).subtract(amount * marketCheckResult.getDouble("price"));
                                        player.sendMessage(PREFIX + "Bought " + YELLOW + amount + PURPLE + " of " + YELLOW + marketName + PURPLE + " for " + YELLOW + plugin.Method.format(amount * marketCheckResult.getDouble("price")) + PURPLE + "!");
                                    } else {
                                        player.sendMessage(ERR_PREFIX + "Unspecified error with SQL insertion! Alert the server admin.");
                                    }
                                }
                            }
                        }
                    } else {
                        player.sendMessage(ERR_PREFIX + "You can't buy that many shares of this market. The market ownership limit is " + YELLOW + marketCheckResult.getInt("lim"));
                    }
                } else {
                    player.sendMessage(ERR_PREFIX + "That market does not exist!");
                }
            } else {
                player.sendMessage(ERR_PREFIX + "That market does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void buymax(Player player, String marketName) {
        String marketQuery = "SELECT price, lim, COUNT(*) AS 'Count' FROM market WHERE name = '" + marketName + "' LIMIT 1;";
        String ownershipQuery = "SELECT amount, COUNT(*) AS 'Count' FROM ownership WHERE market = '" + marketName + "' AND owner = '" + player.getName().toLowerCase() + "' LIMIT 1;";
        ResultSet marketResult = plugin.doQuery(marketQuery);
        ResultSet ownershipResult = plugin.doQuery(ownershipQuery);
        
        try {
            if (marketResult.next() && marketResult.getInt("Count") == 1) {
                double calc = plugin.Method.getAccount(player.getName()).balance() / marketResult.getDouble("price");
                int amount = (int)Math.floor(calc);
                
                if (ownershipResult.next() && ownershipResult.getInt("Count") == 1) {
                    
                    if (ownershipResult.getInt("amount") == marketResult.getInt("lim")) {
                        player.sendMessage(ERR_PREFIX + "You already own the maximum amount of shares in this market.");
                    } else {
                        if ((amount + ownershipResult.getInt("amount")) > marketResult.getInt("lim")) {
                            this.buy(player, marketName, (marketResult.getInt("lim") - ownershipResult.getInt("amount")));
                        } else {
                            this.buy(player, marketName, amount);
                        }
                    }
                } else {
                    if (amount > marketResult.getInt("lim")) {
                        this.buy(player, marketName, marketResult.getInt("lim"));
                    } else {
                        this.buy(player, marketName, amount);
                    }
                }
            } else {
                player.sendMessage(ERR_PREFIX + "That market does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
    }
    
    public void sell(Player player, String marketName, Integer amount) {
        String checkQuery = "SELECT COUNT(*) AS 'Count', price FROM market WHERE name = '" + marketName + "' LIMIT 1;";
        ResultSet checkResult = plugin.doQuery(checkQuery);
        
        try {
            if (checkResult.next() && checkResult.getInt("Count") == 1) {
                String query = "SELECT owner, amount, buyprice, COUNT(*) AS 'Count' FROM ownership WHERE market = '" + marketName + "' AND owner = '" + player.getName().toLowerCase() + "' LIMIT 1;";
                ResultSet result = plugin.doQuery(query);
                
                if (result.next() && result.getInt("Count") == 1) {
                    if (amount >= result.getInt("amount")) {
                        this.sellall(player, marketName);
                    } else {
                        String updateQuery = "UPDATE ownership SET amount = amount - " + amount + " WHERE market = '" + marketName + "' AND owner = '" + player.getName().toLowerCase() + "' LIMIT 1;";
                        if (plugin.updateQuery(updateQuery) == true) {
                            double profit = amount * checkResult.getDouble("price");
                            double buyprice = amount * result.getDouble("buyprice");
                            plugin.Method.getAccount(player.getName()).add(profit);
                            player.sendMessage(PREFIX + "You sold " + YELLOW + amount + PURPLE + " shares of " + YELLOW + marketName + PURPLE + " for " + YELLOW + plugin.Method.format(profit) + PURPLE + ".");
                            if (profit > buyprice) {
                                player.sendMessage(PREFIX + "This sale made you a " + YELLOW + plugin.Method.format(profit - buyprice) + ChatColor.GREEN + " PROFIT" + PURPLE + ".");
                            } else if (profit == buyprice) {
                                player.sendMessage(PREFIX + "You " + ChatColor.GRAY + "BROKE EVEN" + PURPLE + " on this sale.");
                            } else {
                                player.sendMessage(PREFIX + "This sale made you a " + YELLOW + plugin.Method.format(buyprice - profit) + ChatColor.RED + " LOSS" + PURPLE + ".");
                            }
                            
                        } else {
                            player.sendMessage(ERR_PREFIX + "Unspecified error with SQL update! Alert the server admin.");
                        }
                    }
                } else {
                    player.sendMessage(ERR_PREFIX + "You don't own any shares of this market.");
                }
            } else {
                player.sendMessage(ERR_PREFIX + "That market does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void sellall(Player player, String marketName) {
        String checkQuery = "SELECT COUNT(*) AS 'Count', price FROM market WHERE name = '" + marketName + "' LIMIT 1;";
        ResultSet checkResult = plugin.doQuery(checkQuery);
        
        try {
            if (checkResult.next() && checkResult.getInt("Count") == 1) {
                String query = "SELECT owner, amount, buyprice, COUNT(*) AS 'Count' FROM ownership WHERE market = '" + marketName + "' AND owner = '" + player.getName().toLowerCase() + "' LIMIT 1;";
                ResultSet result = plugin.doQuery(query);
                
                if (result.next() && result.getInt("Count") == 1) {
                    int amount = result.getInt("amount");
                    String deleteQuery = "DELETE FROM ownership WHERE market = '" + marketName + "' AND owner = '" + player.getName().toLowerCase() + "' LIMIT 1;";
                    if (plugin.deleteQuery(deleteQuery) == true) {
                        double profit = amount * checkResult.getDouble("price");
                        double buyprice = amount * result.getDouble("buyprice");
                        plugin.Method.getAccount(player.getName()).add(profit);
                        player.sendMessage(PREFIX + "You sold " + YELLOW + amount + PURPLE + " shares of " + YELLOW + marketName + PURPLE + " for " + YELLOW + plugin.Method.format(profit) + PURPLE + ".");
                        if (profit > buyprice) {
                            player.sendMessage(PREFIX + "This sale made you a " + YELLOW + plugin.Method.format(profit - buyprice) + ChatColor.GREEN + " PROFIT" + PURPLE + ".");
                        } else if (profit == buyprice) {
                            player.sendMessage(PREFIX + "You " + ChatColor.GRAY + "BROKE EVEN" + PURPLE + " on this sale.");
                        } else {
                            player.sendMessage(PREFIX + "This sale made you a " + YELLOW + plugin.Method.format(buyprice - profit) + ChatColor.RED + " LOSS" + PURPLE + ".");
                        }
                    } else {
                        player.sendMessage(ERR_PREFIX + "Unspecified error with SQL deletion! Alert the server admin.");
                    }
                } else {
                    player.sendMessage(ERR_PREFIX + "You don't own any shares of this market.");
                }
            } else {
                player.sendMessage(ERR_PREFIX + "That market does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void increase(CommandSender sender, String marketName, Double amount) {
        String checkQuery = "SELECT COUNT(*) AS 'Count', price FROM market WHERE name = '" + marketName + "' LIMIT 1;";
        ResultSet checkResult = plugin.doQuery(checkQuery);
        
        try {
            if (checkResult.next() && checkResult.getInt("Count") == 1) {
                String query = "UPDATE market SET price = price + " + amount + " WHERE name = '" + marketName + "' LIMIT 1;";
                if (plugin.updateQuery(query) == true) {
                    sender.sendMessage(PREFIX + "You have increased the price of " + YELLOW + marketName + PURPLE + " to " + YELLOW + plugin.Method.format(amount + checkResult.getDouble("price")));
                } else {
                    sender.sendMessage(ERR_PREFIX + "Unspecified error with SQL update! Alert the server admin.");
                }
            } else {
                sender.sendMessage(ERR_PREFIX + "That market does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void decrease(CommandSender sender, String marketName, Double amount) {
        String checkQuery = "SELECT COUNT(*) AS 'Count', price FROM market WHERE name = '" + marketName + "' LIMIT 1;";
        ResultSet checkResult = plugin.doQuery(checkQuery);
        
        try {
            if (checkResult.next() && checkResult.getInt("Count") == 1) {
                if (amount <= checkResult.getDouble("price")) {
                    String query = "UPDATE market SET price = price - " + amount + " WHERE name = '" + marketName + "' LIMIT 1;";
                    if (plugin.updateQuery(query) == true) {
                        sender.sendMessage(PREFIX + "You have decreased the price of " + YELLOW + marketName + PURPLE + " to " + YELLOW + plugin.Method.format(checkResult.getDouble("price") - amount));
                    } else {
                        sender.sendMessage(ERR_PREFIX + "Unspecified error with SQL update! Alert the server admin.");
                    }
                } else {
                    sender.sendMessage(ERR_PREFIX + "You can't send a market into negatives!");
                }
            } else {
                sender.sendMessage(ERR_PREFIX + "That market does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void portfolio(Player player) {
        String checkQuery = "SELECT COUNT(*) AS 'Count' FROM ownership WHERE owner = '" + player.getName().toLowerCase() + "';";
        ResultSet checkResult = plugin.doQuery(checkQuery);
        
        try {
            if (checkResult.next() && checkResult.getInt("Count") > 0) {
                String query = "SELECT market, amount, buyprice FROM ownership WHERE owner = '" + player.getName().toLowerCase() + "' ORDER BY market ASC;";
                ResultSet result = plugin.doQuery(query);
                player.sendMessage(" ");
                player.sendMessage(PREFIX + "Your stock portfolio:");
                while (result.next()) {
                    player.sendMessage(PREFIX + YELLOW + result.getInt("amount") + PURPLE + " shares of " + YELLOW + result.getString("market") + PURPLE + ". (Bought at: " + YELLOW + plugin.Method.format(result.getDouble("buyprice")) + PURPLE + ")");
                }
                player.sendMessage(" ");
            } else {
                player.sendMessage(ERR_PREFIX + "You don't own any stocks.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void limit(CommandSender event, String marketName, int limit) {
        String checkQuery = "SELECT COUNT(*) AS 'Count' FROM market WHERE name = '" + marketName + "' LIMIT 1;";
        ResultSet checkResult = plugin.doQuery(checkQuery);
        
        try {
            if (checkResult.next() && checkResult.getInt("Count") == 1) {
                String query = "UPDATE market SET limit = " + limit + " WHERE name = '" + marketName + "' LIMIT 1;";
                if (plugin.updateQuery(query) == true) {
                    event.sendMessage(PREFIX + YELLOW + marketName + PURPLE + "'s ownership limit has been updated to " + YELLOW + limit);
                } else {
                    event.sendMessage(ERR_PREFIX + "Unspecified error with SQL update! Alert the server admin.");
                }
            } else {
                event.sendMessage(ERR_PREFIX + "That market does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /*public void gift(Player player, String receiver, String marketName, int amount) {
        if (plugin.market.containsKey(marketName)) {
            if (plugin.stockOwnership.get(player.getName() + "_" + marketName) != null) {
                if (amount < plugin.stockOwnership.get(player.getName() + "_" + marketName)) {
                    if (plugin.stockOwnership.get(receiver + "_" + marketName) == null) {
                        plugin.stockOwnership.put(player.getName() + "_" + marketName, plugin.stockOwnership.get(player.getName() + "_" + marketName) - amount);
                        plugin.stockOwnership.put(receiver + "_" + marketName, amount);
                        for (StockExchangeListener m : plugin.listeners) {
                            m.onPlayerGifting(player, receiver, marketName, amount);
                        }
                        player.sendMessage(PREFIX + "You have given " + YELLOW + amount + PURPLE + " shares of " + YELLOW + marketName + PURPLE + " to " + YELLOW + receiver + PURPLE + ".");
                    } else {
                        plugin.stockOwnership.put(player.getName() + "_" + marketName, plugin.stockOwnership.get(player.getName() + "_" + marketName) - amount);
                        plugin.stockOwnership.put(receiver + "_" + marketName, plugin.stockOwnership.get(receiver + "_" + marketName) + amount);
                        for (StockExchangeListener m : plugin.listeners) {
                            m.onPlayerGifting(player, receiver, marketName, amount);
                        }
                        player.sendMessage(PREFIX + "You have given " + YELLOW + amount + PURPLE + " shares of " + YELLOW + marketName + PURPLE + " to " + YELLOW + receiver + PURPLE + ".");
                    }
                } else if (amount == plugin.stockOwnership.get(player.getName() + "_" + marketName)) {
                    if (plugin.stockOwnership.get(receiver + "_" + marketName) == null) {
                        plugin.stockOwnership.remove(player.getName() + "_" + marketName);
                        plugin.stockOwnership.put(receiver + "_" + marketName, amount);
                        for (StockExchangeListener m : plugin.listeners) {
                            m.onPlayerGifting(player, receiver, marketName, amount);
                        }
                        player.sendMessage(PREFIX + "You have given " + YELLOW + amount + PURPLE + " shares of " + YELLOW + marketName + PURPLE + " to " + YELLOW + receiver + PURPLE + ".");
                    } else {
                        plugin.stockOwnership.remove(player.getName() + "_" + marketName);
                        plugin.stockOwnership.put(receiver + "_" + marketName, plugin.stockOwnership.get(receiver + "_" + marketName) + amount);
                        for (StockExchangeListener m : plugin.listeners) {
                            m.onPlayerGifting(player, receiver, marketName, amount);
                        }
                        player.sendMessage(PREFIX + "You have given " + YELLOW + amount + PURPLE + " shares of " + YELLOW + marketName + PURPLE + " to " + YELLOW + receiver + PURPLE + ".");
                    }
                } else {
                    player.sendMessage(PREFIX + "You don't have enough stocks in that market.");
                }
            } else {
                player.sendMessage(PREFIX + "You don't have enough stocks in that market.");
            }
        } else {
            player.sendMessage(PREFIX + "That market doesn't exist.");
        }
    }*/
    
    public void makePrivate(CommandSender sender, String marketName) {
        String checkQuery = "SELECT COUNT(*) AS 'Count', private FROM market WHERE name = '" + marketName + "' LIMIT 1;";
        ResultSet checkResult = plugin.doQuery(checkQuery);
        
        try {
            if (checkResult.next() && checkResult.getInt("Count") == 1) {
                if (checkResult.getInt("private") == 0) {
                    String query = "UPDATE market SET private = 1 WHERE name = '" + marketName + "' LIMIT 1;";
                    if (plugin.updateQuery(query) == true) {
                        sender.sendMessage(PREFIX + YELLOW + marketName + PURPLE + " is now private.");
                    } else {
                        sender.sendMessage(ERR_PREFIX + "Unspecified error with SQL update! Alert the server admin.");
                    }
                } else {
                    sender.sendMessage(ERR_PREFIX + "That market is already private!");
                }
            } else {
                sender.sendMessage(ERR_PREFIX + "That market does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void makePublic(CommandSender sender, String marketName) {
        String checkQuery = "SELECT COUNT(*) AS 'Count', private FROM market WHERE name = '" + marketName + "' LIMIT 1;";
        ResultSet checkResult = plugin.doQuery(checkQuery);
        
        try {
            if (checkResult.next() && checkResult.getInt("Count") == 1) {
                if (checkResult.getInt("private") == 1) {
                    String query = "UPDATE market SET private = 0 WHERE name = '" + marketName + "' LIMIT 1;";
                    if (plugin.updateQuery(query) == true) {
                        sender.sendMessage(PREFIX + YELLOW + marketName + PURPLE + " is now public.");
                    } else {
                        sender.sendMessage(ERR_PREFIX + "Unspecified error with SQL update! Alert the server admin.");
                    }
                } else {
                    sender.sendMessage(ERR_PREFIX + "That market is already public!");
                }
            } else {
                sender.sendMessage(ERR_PREFIX + "That market does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void list(CommandSender sender, int length) {
        String checkQuery = "SELECT COUNT(*) AS 'Count' FROM market;";
        ResultSet checkResult = plugin.doQuery(checkQuery);
        
        try {
            if (checkResult.next() && checkResult.getInt("Count") > 0) {
                String query = "SELECT name, price, private FROM market ORDER BY name ASC LIMIT " + length + ";";
                ResultSet result = plugin.doQuery(query);
                if (sender instanceof Player) {
                    sender.sendMessage(" ");
                }
                sender.sendMessage(PREFIX + "Market list: (sorted Alphabetically)");
                while (result.next()) {
                    String name = result.getString("name");
                    int priv = result.getInt("private");
                    String price;
                    if (priv == 1) {
                        if (sender instanceof Player) {
                            if (plugin.checkPermissions("stocks.users.private." + name, (Player) sender) == true) {
                                price = plugin.Method.format(result.getDouble("price"));
                            } else {
                                price = "PRIVATE";
                            }
                        } else {
                            price = plugin.Method.format(result.getDouble("price"));
                        }
                    } else {
                        price = plugin.Method.format(result.getDouble("price"));
                    }

                    sender.sendMessage(PREFIX + YELLOW + name + PURPLE + " at " + YELLOW + price);
                }
                if (sender instanceof Player) {
                    sender.sendMessage(" ");
                }
            } else {
                sender.sendMessage(ERR_PREFIX + "No markets exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void listAll(CommandSender sender) {
        String query = "SELECT COUNT(*) AS 'Count' FROM market;";
        ResultSet result = plugin.doQuery(query);
        
        try {
            if (result.next() && result.getInt("Count") > 0) {
                this.list(sender, result.getInt("Count"));
            } else {
                sender.sendMessage(ERR_PREFIX + "No markets exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
