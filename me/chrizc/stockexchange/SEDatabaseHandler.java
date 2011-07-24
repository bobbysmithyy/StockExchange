package me.chrizc.stockexchange;


import com.alta189.sqllibrary.mysql.mysqlCore;
import com.alta189.sqllibrary.sqlite.sqlCore;
import java.util.logging.Logger;

import java.net.MalformedURLException;

public class SEDatabaseHandler {
    
    private final StockExchange plugin;
    SEConfig config;
    
    public String logPrefix = "[StockExchange] "; // Prefix to go in front of all log entries
    public Logger log = Logger.getLogger("Minecraft"); // Minecraft log and console
    
    
    public SEDatabaseHandler(StockExchange instance, SEConfig config) {
        plugin = instance;
        this.config = config;
    }
    
    public void setupDatabases() {
        if (plugin.MySQL == true) {
            plugin.manageMySQL = new mysqlCore(this.log, this.logPrefix, plugin.dbHost, plugin.dbDatabase, plugin.dbUser, plugin.dbPass);
            if (config.verbose == true) {
                this.log.info(this.logPrefix + "MySQL initializing.");
            }
            plugin.manageMySQL.initialize();

            try {
                if (plugin.manageMySQL.checkConnection()) {
                    if (config.verbose == true) {
                        this.log.info(this.logPrefix + "MySQL connection successful");
                    }

                    if (!plugin.manageMySQL.checkTable("market")) {
                        if (config.verbose == true) {
                            this.log.info(this.logPrefix + "Creating stock market table.");
                        }
                        String query = "CREATE TABLE market (id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, name VARCHAR(50) NOT NULL, price DOUBLE NOT NULL, private BOOLEAN NOT NULL, owner VARCHAR(20) NOT NULL, lim INT NOT NULL);";
                        plugin.manageMySQL.createTable(query);
                    }
                    if (!plugin.manageMySQL.checkTable("ownership")) {
                        if (config.verbose == true) {
                            this.log.info(this.logPrefix + "Creating stock market table.");
                        }
                        String query = "CREATE TABLE ownership (id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, owner VARCHAR(20) NOT NULL, market VARCHAR(50) NOT NULL, amount INT NOT NULL, buyprice DOUBLE NOT NULL);";
                        plugin.manageMySQL.createTable(query);
                    }
                } else {
                    this.log.severe(this.logPrefix + "MySQL connection failed");
                    plugin.MySQL = false;
                }
            } catch (MalformedURLException e) {
                    e.printStackTrace();
            } catch (InstantiationException e) {
                    e.printStackTrace();
            } catch (IllegalAccessException e) {
                    e.printStackTrace();
            }

        } else {

            if (config.verbose == true) {
                this.log.info(this.logPrefix + "SQLite initializing.");
            }

            plugin.manageSQLite = new sqlCore(this.log, this.logPrefix, "StockExchange", plugin.getDataFolder().toString());

            plugin.manageSQLite.initialize();

            if (!plugin.manageSQLite.checkTable("market")) {
                if (config.verbose == true) {
                    this.log.info(this.logPrefix + "Creating stock market table.");
                }
                String query = "CREATE TABLE market (id INT PRIMARY_KEY AUTO_INCREMENT, name VARCHAR(50), price DOUBLE, private BOOLEAN, owner STRING(20), lim INT);";
                plugin.manageSQLite.createTable(query);
            }
            if (!plugin.manageSQLite.checkTable("ownership")) {
                if (config.verbose == true) {
                    this.log.info(this.logPrefix + "Creating stock market table.");
                }
                String query = "CREATE TABLE ownership (id INT PRIMARY_KEY AUTO_INCREMENT, owner STRING(20), market VARCHAR(50), amount INT, buyprice DOUBLE);";
                plugin.manageSQLite.createTable(query);
            }
        }
    }
    
}
