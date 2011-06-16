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
    File backup;
    File backup2;
    
    public SEFileHandler(StockExchange instance) {
        plugin = instance;
    }
    
    public void backup(String type) {
        new File(plugin.getDataFolder(), "backups").mkdir();
        if (type.equals("market")) {
            file = new File(plugin.getDataFolder(), "market.dat");
            backup = new File(plugin.getDataFolder(), "backups/market.bak");
            backup2 = new File(plugin.getDataFolder(), "backups/market2.bak");
            if (backup.exists()) {
            try {
                if (!backup2.exists()) {
                    backup2.createNewFile();
                }
                FileOutputStream fout = new FileOutputStream(backup2);
                ObjectOutputStream oout = new ObjectOutputStream(fout);
                FileInputStream fin = new FileInputStream(backup);
                ObjectInputStream oin = new ObjectInputStream(fin);
                
                oout.writeObject((HashMap)oin.readObject());
            } catch (IOException e) {
                System.err.println("[StockExchange] Market.dat backup #1 to backup #2 copy failed: " + e.getMessage());
            } catch (ClassNotFoundException e1) {
                System.err.println("[StockExchange] Market.dat backup #1 to backup #2 copy failed: " + e1.getMessage());
            }
            }
            
            try {
                if (!backup.exists()) {
                    backup.createNewFile();
                }
                FileOutputStream fout = new FileOutputStream(backup);
                ObjectOutputStream oout = new ObjectOutputStream(fout);
                FileInputStream fin = new FileInputStream(file);
                ObjectInputStream oin = new ObjectInputStream(fin);
                
                oout.writeObject((HashMap)oin.readObject());
            } catch (IOException e) {
                System.err.println("[StockExchange] Market.dat file to backup #1 copy failed: " + e.getMessage());
            } catch (ClassNotFoundException e1) {
                System.err.println("[StockExchange] Market.dat file to backup #1 copy failed: " + e1.getMessage());
            }
        } else if (type.equals("ownership")) {
            file = new File(plugin.getDataFolder(), "ownership.dat");
            backup = new File(plugin.getDataFolder(), "backups/ownership.bak");
            backup2 = new File(plugin.getDataFolder(), "backups/ownership2.bak");
            if (backup.exists()) {
                try {
                    if (!backup2.exists()) {
                        backup2.createNewFile();
                    }
                    FileOutputStream fout = new FileOutputStream(backup2);
                    ObjectOutputStream oout = new ObjectOutputStream(fout);
                    FileInputStream fin = new FileInputStream(backup);
                    ObjectInputStream oin = new ObjectInputStream(fin);
                
                    oout.writeObject((HashMap)oin.readObject());
                } catch (IOException e) {
                    System.err.println("[StockExchange] Ownership.dat backup #1 to backup #2 copy failed: " + e.getMessage());
                } catch (ClassNotFoundException e1) {
                    System.err.println("[StockExchange] Ownership.dat backup #1 to backup #2 copy failed: " + e1.getMessage());
                }
            }
            
            try {
                if (!backup.exists()) {
                    backup.createNewFile();
                }
                FileOutputStream fout = new FileOutputStream(backup);
                ObjectOutputStream oout = new ObjectOutputStream(fout);
                FileInputStream fin = new FileInputStream(file);
                ObjectInputStream oin = new ObjectInputStream(fin);
                
                oout.writeObject((HashMap)oin.readObject());
            } catch (IOException e) {
                System.err.println("[StockExchange] Ownership.dat file to backup #1 copy failed: " + e.getMessage());
            } catch (ClassNotFoundException e1) {
                System.err.println("[StockExchange] Ownership.dat file to backup #1 copy failed: " + e1.getMessage());
            }
        }
    }
    
    public void saveMarket() {
        file = new File(plugin.getDataFolder(),"market.dat");
        this.backup("market");
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
        this.backup("ownership");
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
            System.err.println("[StockExchange] Error! Unable to load ownership data: " + e.getMessage());
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