package me.chrizc.stockexchange;

import java.net.*;
import java.io.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;

public class SEUpdater {
    
    private final StockExchange plugin;
    SEConfig config;
    URL url;
    URLConnection urlConn = null;
    InputStreamReader inStream = null;
    BufferedReader buff = null;
    String version;
    PluginDescriptionFile pdfFile;
    int schid;
    Boolean sendToConsole;
    
    public SEUpdater(StockExchange instance, SEConfig config) {
        plugin = instance;
        this.config = config;
    }
    
    
    
    public void update(Boolean console) {
        if (config.checkVersion == true) {
            sendToConsole = console;
            try {
                url = new URL("http://chrizc.co.uk/checkversion.php?plugin=stockexchange");
                urlConn = url.openConnection();
                inStream = new InputStreamReader(urlConn.getInputStream());
                buff = new BufferedReader(inStream);
                version = buff.readLine();
                pdfFile = plugin.getDescription();
                if (!pdfFile.getVersion().equals(version)) {
                    schid = Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
                        public void run() {
                            if (sendToConsole == true) {
                                System.out.println("[StockExchange] There is an update available for StockExchange! Check the Bukkit forum post for more info.");
                            }
                        }
                    }, 20L);
                } else {
                    if (sendToConsole == true && config.verbose == true) {
                        System.out.println("[StockExchange] is up to date.");
                    }
                }
            } catch (MalformedURLException e) {
                System.err.println("[StockExchange] Please check the URL: " + e.toString() );
            } catch (IOException e1) {
                System.err.println("[StockExchange] Can't read from the Internet: "+ e1.toString() );
            }
        }
    }
    
}
