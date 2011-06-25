package me.ChrizC.stockexchange.exceptions;

public class ExistantMarketException extends Exception {
    
    String err;
    
    public ExistantMarketException(String error) {
        super(error);
        err = error;
    }
    
    public String getError() {
        return err;
    }
}
