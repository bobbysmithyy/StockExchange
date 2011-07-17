package me.ChrizC.stockexchange;

import java.io.FileOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.IOException;

import org.bukkit.util.config.Configuration;

import java.util.Iterator;

public class SEBackupHandler {
    
    private StockExchange plugin;
    SEConfig config;
    SEDateHandler dateHandler;
    
    File file;
    File file2;
    
    Configuration configFile;
    Configuration configFile2;
    
    public SEBackupHandler(StockExchange instance, SEConfig config, SEDateHandler handle) {
        plugin = instance;
        this.config = config;
        dateHandler = handle;
    }
    
    public void backup() {
        new File(plugin.getDataFolder(), "backups").mkdir();
        if (config.fileTypes.contains("DAT")) {
            new File(plugin.getDataFolder(), "backups/" + dateHandler.getDate()).mkdir();
            file = new File(plugin.getDataFolder(), "backups/" + dateHandler.getDate() + "/market.dat");
            file2 = new File(plugin.getDataFolder(), "backups/" + dateHandler.getDate() + "/ownership.dat");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                
                FileOutputStream fos2 = new FileOutputStream(file2);
                ObjectOutputStream oos2 = new ObjectOutputStream(fos2);

                oos.writeObject(plugin.market);
                oos2.writeObject(plugin.stockOwnership);

                if (config.verbose == true) {
                    System.out.println("[StockExchange] Backup /" + dateHandler.getDate() + "/market.dat saved!");
                    System.out.println("[StockExchange] Backup /" + dateHandler.getDate() + "/ownership.dat saved!");
                }

                oos.close();
                oos2.close();
            } catch (IOException e) {
                System.err.println("[StockExchange] Error! Unable to backup: " + e.getMessage());
            }
        }
        if (config.fileTypes.contains("YML")) {
            configFile = new Configuration(new File(plugin.getDataFolder(), "backups/" + dateHandler.getDate() + "/market.yml"));
            configFile.load();
            if (plugin.market.size() > 0) {
                Iterator<String> i = plugin.market.keySet().iterator();
                while (i.hasNext()) {
                    String val1 = i.next();
                    Double val2 = plugin.market.get(val1);
                    configFile.setProperty("market." + val1, val2);
                }
                
                if (config.verbose == true) {
                    System.out.println("[StockExchange] Backup /" + dateHandler.getDate() + "/market.yml saved!");
                }
                configFile.save();
            }
             
            configFile2 = new Configuration(new File(plugin.getDataFolder(), "backups/" + dateHandler.getDate() + "/ownership.yml"));
            configFile2.load();
            if (plugin.stockOwnership.size() > 0) {
                Iterator<String> i = plugin.stockOwnership.keySet().iterator();
                while (i.hasNext()) {
                    String val1 = i.next();
                    double val2 = plugin.stockOwnership.get(val1);
                    configFile.setProperty("ownership." + val1, val2);
                }
                
                if (config.verbose == true) {
                    System.out.println("[StockExchange] Backup /" + dateHandler.getDate() + "/ownership.yml saved!");
                }
            }
            configFile2.save();
        }
    }
    
}
