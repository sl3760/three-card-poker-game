package com.poker.shared;

public class GameOver {
     
	 public int balance;
	 
	 public static enum Result{
    	 WIN,LOSE,TIE,BONUS;
     }
	 
	
     public Result result;
     
     public Result getResult(){
         return this.result;
     }
 
     
     public GameOver()
     {
    	 
     }
     
     public GameOver(int balance,int wagerPairPlus,Result result, int multiplyPairPlus) {
    	 this.result=result;
    	 if(result==Result.LOSE)
    		 this.balance=balance-wagerPairPlus;
    	 if(result==Result.WIN)
    		 this.balance=balance+wagerPairPlus*multiplyPairPlus;
    	 if(result==Result.TIE)
    		 this.balance=balance;
     }
     
     public GameOver(int balance,int wagerAnte, int wagerPlay,Result result,int multiplyAnte,int multiplyPlay) {
    	 this.result=result;
    	 if(result==Result.LOSE)
    		 this.balance=balance-wagerAnte-wagerPlay;
    	 if(result==Result.WIN)
    		 this.balance=balance+wagerAnte*multiplyAnte+wagerPlay*multiplyPlay;
    	 if(result==Result.TIE)
    		 this.balance=balance;
    	 
     }
     
     public int getBalance(){
    	 return this.balance;
     }
}
