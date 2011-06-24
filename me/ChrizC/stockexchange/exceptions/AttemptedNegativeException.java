package me.ChrizC.stockexchange.exceptions;

public class AttemptedNegativeException extends Exception {
    
    String err;
    
    public AttemptedNegativeException(String error) {
        super(error);
        err = error;
    }
    
    public String getError() {
        return err;
    }
    
}
