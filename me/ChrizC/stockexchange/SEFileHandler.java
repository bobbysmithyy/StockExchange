package me.ChrizC.stockexchange;

import java.io.FileOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

import org.bukkit.util.config.Configuration;

import java.util.HashMap;
import java.util.Iterator;

public class SEFileHandler {
    
    private final StockExchange plugin;
    SEConfig config;
    String string;
    
    File file;
    Configuration configFile;
    
    public SEFileHandler(StockExchange instance, SEConfig config) {
        plugin = instance;
        this.config = config;
    }
    
    
    
    public void saveMarket() {
        if (config.fileTypes.contains("DAT")) {
        file = new File(plugin.getDataFolder(), "market.dat");
            //backupHandler.backup("market");
            if (plugin.market != null) {
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);

                    oos.writeObject(plugin.market);

                    if (config.verbose == true) {
                        System.out.println("[StockExchange] Market data saved to file successfully. (DAT)");
                    }

                    oos.close();
                } catch (IOException e) {
                    System.err.println("[StockExchange] Error! Unable to save market data to file: " + e.getMessage());
                }
            }
        }
        
        if (config.fileTypes.contains("YML")) {
            configFile = new Configuration(new File(plugin.getDataFolder(), "market.yml"));
            configFile.load();
            if (plugin.market.size() > 0) {
                Iterator<String> i = plugin.market.keySet().iterator();
                while (i.hasNext()) {
                    String val1 = i.next();
                    Double val2 = plugin.market.get(val1);
                    configFile.setProperty("market." + val1, val2);
                }
                configFile.save();
                if (config.verbose == true) {
                    System.out.println("[StockExchange] Market data saved to file successfully. (YML)");
                }
            }
        }
    }
    
    public void loadMarket() {
        if (config.loadType.equalsIgnoreCase("DAT")) {
            file = new File(plugin.getDataFolder(),"market.dat");
            try {
                if (!file.exists()) {
                    plugin.getDataFolder().mkdir();
                    file.createNewFile();
                    System.out.println("[StockExchange] Created market.dat data file successfully.");
                } else {
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);

                    plugin.market = new HashMap((HashMap)ois.readObject());

                    if (config.verbose == true) {
                        System.out.println("[StockExchange] Loaded market data successfully. (DAT)");
                    }

                    ois.close();
                }
            } catch (IOException e) {
                System.err.println("[StockExchange] Error! Unable to load market data: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.err.println("[StockExchange] Error! Unable to load market data: " + e.getMessage());
            }
        } else if (config.loadType.equalsIgnoreCase("YML")) {
            file = new File(plugin.getDataFolder(), "market.yml");
            configFile = new Configuration(file);
            configFile.load();
            if (file.exists()) {
                Iterator<String> i = configFile.getKeys("market").iterator();
                while (i.hasNext()) {
                    String s = i.next();
                    plugin.market.put(s, configFile.getDouble("market." + s, 1.00));
                }
                
                if (config.verbose == true) {
                    System.out.println("[StockExchange] Loaded market data successfully. (YML)");
                }
                
            }
        }
    }
    
    public void saveOwnership() {
        if (config.fileTypes.contains("DAT")) {
            file = new File(plugin.getDataFolder(),"ownership.dat");
            //backupHandler.backup("ownership");
            if (plugin.market != null) {
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);

                    oos.writeObject(plugin.stockOwnership);

                    if (config.verbose == true) {
                        System.out.println("[StockExchange] Ownership data saved to file successfully.");
                    }

                    oos.close();
                } catch (IOException e) {
                    System.err.println("[StockExchange] Error! Unable to save ownership data to file: " + e.getMessage());
                }
            }
        }
        
        if (config.fileTypes.contains("YML")) {
            configFile = new Configuration(new File(plugin.getDataFolder(), "ownership.yml"));
            configFile.load();
            if (plugin.stockOwnership.size() > 0) {
                Iterator<String> i = plugin.stockOwnership.keySet().iterator();
                while (i.hasNext()) {
                    String val1 = i.next();
                    double val2 = plugin.stockOwnership.get(val1);
                    configFile.setProperty("ownership." + val1, val2);
                }
                configFile.save();
            }
        }
    }
    
    public void loadOwnership() {
        if (config.loadType.equalsIgnoreCase("DAT")) {
            file = new File(plugin.getDataFolder(),"ownership.dat");
            try {
                if (!file.exists()) {
                    plugin.getDataFolder().mkdir();
                    file.createNewFile();
                    System.out.println("[StockExchange] Created ownership.dat data file successfully.");
                } else {
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);

                    plugin.stockOwnership = new HashMap((HashMap)ois.readObject());
                    if (config.verbose == true) {
                        System.out.println("[StockExchange] Loaded ownership data successfully. (DAT)");
                    }

                    ois.close();
                }
            } catch (IOException e) {
                System.err.println("[StockExchange] Error! Unable to load ownership data: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.err.println("[StockExchange] Error! Unable to load ownership data: " + e.getMessage());
            }
        } else if (config.loadType.equalsIgnoreCase("YML")) {
            file = new File(plugin.getDataFolder(), "ownership.yml");
            configFile = new Configuration(file);
            configFile.load();
            if (file.exists()) {
                Iterator<String> i = configFile.getKeys("ownership").iterator();
                while (i.hasNext()) {
                    String s = i.next();
                    plugin.stockOwnership.put(s, configFile.getInt("ownership." + s, 1));
                }
                
                if (config.verbose == true) {
                    System.out.println("[StockExchange] Loaded ownership data successfully. (YML)");
                }
                
            }
        }
    }
    
    public boolean checkBackup(int num) {
        if (num == 2) {
            File mrkCheck = new File(plugin.getDataFolder(), "backups/market2.bak");
            File ownCheck = new File(plugin.getDataFolder(), "backups/ownership2.bak");
            if (mrkCheck.exists() && ownCheck.exists()) {
                return true;
            }
        } else if (num == 1) {
            File mrkCheck = new File(plugin.getDataFolder(), "backups/market.bak");
            File ownCheck = new File(plugin.getDataFolder(), "backups/ownership.bak");
            if (mrkCheck.exists() && ownCheck.exists()) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean rollback(int num) {
        if (this.checkBackup(num) == true) {
            if (num == 1) {
                try {
                    this.saveMarket();
                    this.saveOwnership();
                    FileInputStream fis2 = new FileInputStream(new File(plugin.getDataFolder(), "backups/market.bak"));
                    ObjectInputStream ois2 = new ObjectInputStream(fis2);
                    FileInputStream fis = new FileInputStream(new File(plugin.getDataFolder(), "backups/ownership.bak"));
                    ObjectInputStream ois = new ObjectInputStream(fis);
                
                    plugin.stockOwnership = new HashMap((HashMap)ois.readObject());
                    plugin.market = new HashMap((HashMap)ois2.readObject());
                    
                    for (StockExchangeListener m : plugin.listeners) {
                        m.onPluginRollback(num);
                    }

                    ois.close();
                    ois2.close();
                    return true;
                } catch (IOException e) {
                    System.err.println("[StockExchange] Error! Unable to roll back data: " + e.getMessage());
                    return false;
                } catch (ClassNotFoundException e) {
                    System.err.println("[StockExchange] Error! Unable to roll back data: " + e.getMessage());
                    return false;
                }
            } else if (num == 2) {
                try {
                    this.saveMarket();
                    this.saveOwnership();
                    FileInputStream fis2 = new FileInputStream(new File(plugin.getDataFolder(), "backups/market2.bak"));
                    ObjectInputStream ois2 = new ObjectInputStream(fis2);
                    FileInputStream fis = new FileInputStream(new File(plugin.getDataFolder(), "backups/ownership2.bak"));
                    ObjectInputStream ois = new ObjectInputStream(fis);
                
                    plugin.stockOwnership = new HashMap((HashMap)ois.readObject());
                    plugin.market = new HashMap((HashMap)ois2.readObject());
                    
                    for (StockExchangeListener m : plugin.listeners) {
                        m.onPluginRollback(num);
                    }

                    ois.close();
                    ois2.close();
                    return true;
                } catch (IOException e) {
                    System.err.println("[StockExchange] Error! Unable to roll back data: " + e.getMessage());
                    return false;
                } catch (ClassNotFoundException e) {
                    System.err.println("[StockExchange] Error! Unable to roll back data: " + e.getMessage());
                    return false;
                }
            }
        }
        
        return false;
    }
    
    public boolean undo() {
        try {
            FileInputStream fis = new FileInputStream(new File(plugin.getDataFolder(),"ownership.dat"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            FileInputStream fis2 = new FileInputStream(new File(plugin.getDataFolder(),"market.dat"));
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
                
            plugin.stockOwnership = new HashMap((HashMap)ois.readObject());
            plugin.market = new HashMap((HashMap)ois2.readObject());
            
            for (StockExchangeListener m : plugin.listeners) {
                m.onPluginUndo();
            }

            ois.close();
            ois2.close();
            return true;
        } catch (IOException e) {
            System.err.println("[StockExchange] Error! Unable to undo rollback: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println("[StockExchange] Error! Unable to undo rollback: " + e.getMessage());
            return false;
        }
    }
}