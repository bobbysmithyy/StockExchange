package me.ChrizC.stockexchange;

import com.nijikokun.register.payment.Method;

public class economyManager {

    public static Method economy;

    public static boolean hasAccount(String p){
        return economy.hasAccount(p);
    }
    
   // public static boolean hasBankAccount(String b, String p) {
   //     return economy.hasBankAccount(b, p);
   // }
    
    public static void add(String name, float amount){
        economy.getAccount(name).add(amount);
    }
    
    public static void substract(String name, float amount){
        economy.getAccount(name).subtract(amount);
    }
    public static boolean hasEnough(String name, float amount) {
        return economy.getAccount(name).hasEnough(amount);
    }
    
    public static double balance(String name){
        return economy.getAccount(name).balance();
    }
    
    public static String formatedBalance(double amount){
        return economy.format(amount);
    }
}