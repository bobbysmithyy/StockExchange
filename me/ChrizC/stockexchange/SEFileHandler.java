package me.ChrizC.stockexchange;

import java.io.FileOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

import java.util.HashMap;


public class SEFileHandler {
    
    private final StockExchange plugin;
    
    File file;
    
    public SEFileHandler(StockExchange instance) {
        plugin = instance;
    }
    
    public void saveMarket() {
        file = new File(plugin.getDataFolder(),"market.dat");
        if (plugin.market != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                   
                oos.writeObject(plugin.market);
                System.out.println("[StockExchange] Market data saved to file successfully.");

                oos.close();
            } catch (IOException e) {
                System.err.println("[StockExchange] Error! Unable to save market data to file: " + e.getMessage());
            }
        }
    }
    
    public void loadMarket() {
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
                System.out.println("[StockExchange] Loaded market data successfully.");

                ois.close();
            }
        } catch (IOException e) {
            System.err.println("[StockExchange] Error! Unable to load market data: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("[StockExchange] Error! Unable to load market data: " + e.getMessage());
        }
    }
    
    public void saveOwnership() {
        file = new File(plugin.getDataFolder(),"ownership.dat");
        if (plugin.market != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                   
                oos.writeObject(plugin.stockOwnership);
                System.out.println("[StockExchange] Ownership data saved to file successfully.");

                oos.close();
            } catch (IOException e) {
                System.err.println("[StockExchange] Error! Unable to save ownership data to file: " + e.getMessage());
            }
        }
    }
    
    public void loadOwnership() {
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
                System.out.println("[StockExchange] Loaded ownership data successfully.");

                ois.close();
            }
        } catch (IOException e) {
            System.err.println("[StockExchange] Error! Unable to load ownership data: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("[StockExchange] Error! Unable to load ownsership data: " + e.getMessage());
        }
    }
    
}
