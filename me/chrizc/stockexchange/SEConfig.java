package me.chrizc.stockexchange;

import java.io.File;

import org.bukkit.util.config.Configuration;

public class SEConfig {
    
    private final StockExchange plugin;
    
    Configuration file;
    
    Boolean checkVersion;
    boolean verbose;
    boolean refundOnRemoval;
    
    public SEConfig (StockExchange instance) {
        plugin = instance;
    }
    
    public void doConfig() {
        file = new Configuration(new File(plugin.getDataFolder(), "config.yml"));
        file.load();
        
        verbose = file.getBoolean("verbose", false);
        
        if (new File(plugin.getDataFolder(), "config.yml").exists()) {
            if (verbose == true) {
                System.out.println("[StockExchange] Configuration file loaded!");
            }
        } else {
            file.setProperty("checkVersion", true);
            file.setProperty("verbose", false);
            file.setProperty("refundOnRemoval", true);
            
            file.setProperty("backupOnDisable", true);
            
            file.setProperty("useMySQL", false);
            file.setProperty("database.host", "localhost");
            file.setProperty("database.username", "root");
            file.setProperty("database.password", "password");
            file.setProperty("database.dbname", "stockexchange");
            
            file.save();
            System.out.println("[StockExchange] Configuration file created with default values!");
        }
        
        //Updated?
        if (file.getProperty("verbose") == null) {
            file.setProperty("verbose", false);
        }
        if (file.getProperty("refundOnRemoval") == null) {
            file.setProperty("refundOnRemoval", true);
        }
        if (file.getProperty("backupOnDisable") != null) {
            file.removeProperty("backupOnDisable");
        }
        if (file.getProperty("fileFormats.default") != null) {
            file.removeProperty("fileFormats");
        }
        if (file.getProperty("useMySQL") == null) {
            file.setProperty("useMySQL", false);
            file.setProperty("database.host", "localhost");
            file.setProperty("database.username", "root");
            file.setProperty("database.password", "password");
            file.setProperty("database.dbname", "stockexchange");
        }
        file.save();
        
        //Get configs
        checkVersion = file.getBoolean("checkVersion", true);
        refundOnRemoval = file.getBoolean("refundOnRemoval", true);
        
        plugin.MySQL = file.getBoolean("useMySQL", false);
        plugin.dbHost = file.getString("database.host", "localhost");
        plugin.dbUser = file.getString("database.username", "root");
        plugin.dbPass = file.getString("database.password", "password");
        plugin.dbDatabase = file.getString("database.dbname", "stockexchange");
        
    } 
}
