package me.ChrizC.stockexchange;

import com.nijikokun.register.payment.Methods;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

public class SEPluginListener extends ServerListener {
    
    private Methods Methods = new Methods();
    
    //Economy plugins
    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        if(!this.Methods.hasMethod()){
            if(this.Methods.setMethod(event.getPlugin())){
                economyManager.economy = this.Methods.getMethod();
                System.out.println("[StockExchange] " + economyManager.economy.getName() + " version " + economyManager.economy.getVersion() + " loaded.");
            }
        }
    }
}
