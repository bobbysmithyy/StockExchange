package me.ChrizC.stockexchange.exceptions;

public class NonExistantMarketException extends Exception {
    
    String err;
    
    public NonExistantMarketException(String error) {
        super(error);
        err = error;
    }
    
    public String getError() {
        return err;
    }
}
