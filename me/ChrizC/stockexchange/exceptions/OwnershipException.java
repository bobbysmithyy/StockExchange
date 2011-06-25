package me.ChrizC.stockexchange.exceptions;

public class OwnershipException extends Exception {
    
    String err;
    
    public OwnershipException(String error) {
        super(error);
        err = error;
    }
    
    public String getError() {
        return err;
    }
    
}
