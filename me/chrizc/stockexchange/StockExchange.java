package me.chrizc.stockexchange;

import com.alta189.sqllibrary.mysql.mysqlCore;
import com.alta189.sqllibrary.sqlite.sqlCore;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.*;
import org.bukkit.event.*;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Set;
import java.util.HashSet;
import java.util.logging.Logger;

import java.net.MalformedURLException;

import java.sql.ResultSet;

import com.nijikokun.register.payment.Method;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class StockExchange extends JavaPlugin {
    
    /**
     * To-do list:
     * TODO Figure out how to work "buying out" markets.
     * TODO Add buy/sales charges.
     **/
    
    public String logPrefix = "[StockExchange] "; // Prefix to go in front of all log entries
    public Logger log = Logger.getLogger("Minecraft"); // Minecraft log and console
    
    //MySQL handlers
    public mysqlCore manageMySQL;
    public sqlCore manageSQLite;
    
    //MySQL settings variables
    public boolean MySQL = false;
    public String dbHost = null;
    public String dbUser = null;
    public String dbPass = null;
    public String dbDatabase = null;
    
    public static PermissionHandler permissionHandler;
    protected final SEConfig config = new SEConfig(this);
    private final SEHelper helper = new SEHelper(this);
    private final SEDatabaseHandler databaseHandler = new SEDatabaseHandler(this, config);
    public final SEMarketHandler marketHandler = new SEMarketHandler(this, config);
    private final SEPluginListener pluginListener = new SEPluginListener(this);
    private final SECommandListener cmdHandler = new SECommandListener(this, marketHandler, config, helper);
    private final SEUpdater updater = new SEUpdater(this, config);
    public Method Method = null;
    
    protected static Set<StockExchangeListener> listeners = new HashSet<StockExchangeListener>();
    
    //static HashMap<String, Double> market = new HashMap<String, Double>();
    //static HashMap<String, Integer> stockOwnership = new HashMap<String, Integer>();
    
    @Override
    public void onDisable() {
        if (this.MySQL == true) {
            this.manageMySQL.close();
            if (config.verbose == true) {
                this.log.info(this.logPrefix + "MySQL connection closed.");
            }
        } else {
            this.manageSQLite.close();
            if (config.verbose == true) {
                this.log.info(this.logPrefix + "SQLite connection closed.");
            }
        }
        this.log.info(this.logPrefix + "Disabled.");
    }
    
    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, pluginListener, Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLUGIN_DISABLE, pluginListener, Priority.Monitor, this);
        config.doConfig();
        //updater.update(true);
        cmdHandler.setupCommands();
        setupPermissions();
        databaseHandler.setupDatabases();
        PluginDescriptionFile pdfFile = this.getDescription();
        this.log.info(this.logPrefix + "version v" + pdfFile.getVersion() + " is enabled.");
        if (pdfFile.getVersion().contains("dev")) {
            this.log.warning(this.logPrefix + "Warning: you are using a DEVELOPMENT build.");
            this.log.warning(this.logPrefix + "Warning: Be sure to back up your worlds, and report all bugs to the thread.");
        }
    }
    
    private void setupPermissions() {
      Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");

      if (this.permissionHandler == null) {
            if (permissionsPlugin != null) {
                this.permissionHandler = ((Permissions) permissionsPlugin).getHandler();
                this.log.info(this.logPrefix + "hooked into Permissions.");
            } else {
                this.log.info(this.logPrefix + "Permissions not found, defaulting to SuperPerms.");
            }
        }
    }  
    
    public boolean checkPermissions(String node, Player player) {
        
        if (node.contains("users.private.")) {
            if (this.permissionHandler != null) {
                return this.permissionHandler.has(player, node);
            } else {
                Permission p = new Permission(node, PermissionDefault.OP);
                return player.hasPermission(p);
            }
        }
        
        if (this.permissionHandler != null) {
            return this.permissionHandler.has(player, node);
        } else {
            return player.hasPermission(node);
        }
    }
    
    public boolean updateQuery(String query) {
        if (this.MySQL) {
            try {
                this.manageMySQL.updateQuery(query);
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            this.manageSQLite.updateQuery(query);
            return true;
        }
    }
    
    public ResultSet doQuery(String query) {
        ResultSet result = null;
        if (this.MySQL) {
            try {
                result = this.manageMySQL.sqlQuery(query);
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return this.manageSQLite.sqlQuery(query);
        }
    }
    
    public boolean insertQuery(String query) {
        if (this.MySQL) {
            try {
                this.manageMySQL.insertQuery(query);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            } catch (InstantiationException e) {
                e.printStackTrace();
                return false;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            this.manageSQLite.insertQuery(query);
            return true;
        }
    }
    
    public boolean deleteQuery(String query) {
        if (this.MySQL) {
            try {
                this.manageMySQL.insertQuery(query);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            } catch (InstantiationException e) {
                e.printStackTrace();
                return false;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            this.manageSQLite.insertQuery(query);
            return true;
        }
    }
    
    

}
