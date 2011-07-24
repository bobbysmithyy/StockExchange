/*    */ package me.chrizc.stockexchange.exceptions;
/*    */ 
/*    */ public class QueryException extends Exception
/*    */ {
/*    */   String err;
/*    */ 
/*    */   public QueryException(String error)
/*    */   {
/*  8 */     super(error);
/*  9 */     this.err = error;
/*    */   }
/*    */ 
/*    */   public String getError() {
/* 13 */     return this.err;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\ChrisC\My Documents\NetBeansProjects\StockExchange\dist\StockExchange.jar
 * Qualified Name:     me.chrizc.stockexchange.exceptions.QueryException
 * JD-Core Version:    0.6.0
 */