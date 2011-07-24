package me.chrizc.stockexchange;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class SECommandListener {
    
    private final StockExchange plugin;
    SEMarketHandler marketHandler;
    SEConfig config;
    SEHelper helper;
    
    Player player;
    
    List list = new ArrayList();
    
    public SECommandListener(StockExchange instance, SEMarketHandler marketHandler, SEConfig config, SEHelper helper) {
        plugin = instance;
        this.marketHandler = marketHandler;
        this.config = config;
        this.helper = helper;
    }
    
    public void setupCommands() {
        PluginCommand stocks = plugin.getCommand("stocks");
        CommandExecutor commandExecutor = new CommandExecutor() {
            public boolean onCommand( CommandSender sender, Command command, String label, String[] args ) {
                if (sender instanceof Player) {
                    if (args.length > 0) {
                        stocks(sender, args);
                    }
                } else {
                    consolestocks(sender, args);
                }
                
                  
                return true;
            }
        };
        if (stocks != null) {
            stocks.setExecutor(commandExecutor);
        }
    }
    
    public void stocks(CommandSender event, String[] args) {
        player = (Player) event;
        if (args.length >= 1) {
            if (args[0].equals("top5")) {
                marketHandler.top5(event);
            } else if (args[0].equals("add")) {
                if (plugin.checkPermissions("stocks.admin.add", player) == true) {
                    if (args.length == 2) {
                        marketHandler.add(event, args[1], 1.0);
                    } else if (args.length >= 3) {
                        marketHandler.add(event, args[1], Double.parseDouble(args[2]));
                    }
                }
            } else if (args[0].equals("remove") && args.length > 1) {
                if (plugin.checkPermissions("stocks.admin.remove", player) == true) {
                    marketHandler.remove(player, args[1]);
                }
            } else if (args[0].equals("lookup") && args.length > 1) {
                if (plugin.checkPermissions("stocks.users.lookup", player) == true) {
                    marketHandler.lookup(player, args[1]);
                } 
            } else if (args[0].equals("buy") && args.length > 2) {
                if (plugin.checkPermissions("stocks.users.trade", player) == true) {
                    if (args[2].equals("max")) {
                        marketHandler.buymax(player, args[1]);
                    } else if (Integer.parseInt(args[2]) > 0) {
                        marketHandler.buy(player, args[1], Integer.parseInt(args[2]));
                    } else {
                        player.sendMessage(ChatColor.RED + "[Stocks] Incorrect number!");
                    }
                }
            } else if (args[0].equals("sell") && args.length > 2) {
                if (plugin.checkPermissions("stocks.users.trade", player) == true) {
                    if (args[2].equals("all") || args[2].equals("max")) {
                        marketHandler.sellall(player, args[1]);
                    } else {
                        if (Integer.parseInt(args[2]) > 0) {
                            marketHandler.sell(player, args[1], Integer.parseInt(args[2]));
                        } else {
                            player.sendMessage(ChatColor.RED + "[Stocks] Incorrect number!");
                        }
                    }
                } 
            } else if (args[0].equals("increase") && args.length > 2) {
                if (plugin.checkPermissions("stocks.admin.modify", player) == true) {
                    if (Double.parseDouble(args[2]) > 0) {
                        marketHandler.increase(player, args[1], Double.parseDouble(args[2]));
                    } else {
                        player.sendMessage(ChatColor.RED + "[Stocks] Incorrect number!");
                    }
                }
            } else if (args[0].equals("decrease") && args.length > 2) {
                if (plugin.checkPermissions("stocks.admin.modify", player) == true) {
                    if (Double.parseDouble(args[2]) > 0) {
                        marketHandler.decrease(player, args[1], Double.parseDouble(args[2]));
                    } else {
                        player.sendMessage(ChatColor.RED + "[Stocks] Incorrect number!");
                    }
                }
            } else if (args[0].equals("portfolio") || args[0].equals("showmine")) {
                marketHandler.portfolio(player);
            } else if (args[0].equals("limit")) {
                if (args.length == 3) {
                    if (plugin.checkPermissions("stocks.admin.limit", player) == true) {
                        marketHandler.limit(event, args[1], Integer.parseInt(args[2]));
                    }
                }
            //} else if (args[0].equals("giveto") || args[0].equals("gift") || args[0].equals("give")) {
                //if (args.length == 4) {
                    //if (plugin.checkPermissions("stocks.users.gift", player) == true) {
                        //marketHandler.gift(player, args[1], args[2], Integer.parseInt(args[3]));
                    //}
                //}
            } else if (args[0].equals("private")) {
                if (args.length == 2) {
                    if (plugin.checkPermissions("stocks.admin.private", player) == true) {
                        marketHandler.makePrivate(event, args[1]);
                    }
                }
            } else if (args[0].equals("public")) {
                if (args.length == 2) {
                    if (plugin.checkPermissions("stocks.admin.public", player) == true) {
                        marketHandler.makePublic(event, args[1]);
                    }
                }
            } else if (args[0].equals("help")) {
                if (args.length == 1) {
                    helper.helpMe(event, "user");
                } else if (args.length == 2) {
                    if (args[1].equals("admin")) {
                        helper.helpMe(event, "admin");
                    }
                }
            } else if (args[0].equals("?")) {
                if (args.length == 1) {
                    helper.helpMe(event, "user");
                } else if (args.length == 2) {
                    if (args[1].equals("admin")) {
                        helper.helpMe(event, "admin");
                    }
                }
            } else if (args[0].equals("list")) {
                if (args.length > 1) {
                    if (args[1].equals("all")) {
                        marketHandler.listAll(event);
                    } else {
                        marketHandler.list(event, Integer.parseInt(args[1]));
                    }
                } else {
                    marketHandler.list(event, 5);
                }
            }
        }
    }
    
    public void consolestocks(CommandSender event, String[] args) {
        if (args.length >= 1) {
            if (args[0].equals("top5")) {
                marketHandler.top5(event);
            } else if (args[0].equals("add")) {
                if (args.length == 2) {
                    marketHandler.add(event, args[1], 1.0);
                } else if (args.length >= 3) {
                    marketHandler.add(event, args[1], Double.parseDouble(args[2]));
                }
            } else if (args[0].equals("remove") && args.length > 1) {
                marketHandler.remove(event, args[1]);
            } else if (args[0].equals("lookup") && args.length > 1) {
                marketHandler.lookup(event, args[1]);
            } else if (args[0].equals("increase") && args.length > 2) {
                if (Double.parseDouble(args[2]) > 0) {
                    marketHandler.increase(event, args[1], Double.parseDouble(args[2]));
                } else {
                    event.sendMessage(ChatColor.RED + "[Stocks] Incorrect number!");
                }
            } else if (args[0].equals("decrease") && args.length > 2) {
            if (Double.parseDouble(args[2]) > 0) {
                    marketHandler.decrease(event, args[1], Double.parseDouble(args[2]));
                } else {
                    event.sendMessage(ChatColor.RED + "[Stocks] Incorrect number!");
                }
            } else if (args[0].equals("limit")) {
                if (args.length == 3) {
                    marketHandler.limit(event, args[1], Integer.parseInt(args[2]));
                }
            } else if (args[0].equals("private")) {
                if (args.length == 2) {
                    marketHandler.makePrivate(event, args[1]);
                }
            } else if (args[0].equals("public")) {
                if (args.length == 2) {
                    marketHandler.makePublic(event, args[1]);
                }
            } else if (args[0].equals("help")) {
                helper.consoleHelpMe(event);
            } else if (args[0].equals("list")) {
                if (args.length > 1) {
                    if (args[1].equals("all")) {
                        marketHandler.listAll(event);
                    } else {
                        marketHandler.list(event, Integer.parseInt(args[1]));
                    }
                } else {
                    marketHandler.list(event, 5);
                }
            }
        }
    }
}