package com.poker.shared;

public class PlayerMove {
       public static enum Choose{
    	   PairsPlus, Ante, PairsPlusAnte;
       }
	   public static enum Decision{
    	   Deal, Play, Fold;
       }
       
	   public Choose choose;
	   public Decision decision;
	  
	   public PlayerMove(){
		   
	   }
	   
	   public PlayerMove(Choose choose){
		   this.choose=choose;	
	   }
	   
	   public PlayerMove(Decision decision){
		   this.decision=decision;	
	   }
	   
	   public void setPlayerChoose(Choose choose){
		   this.choose=choose;		   
	   }
	   
	   public void setPlayerDecision(Decision decision){
		   this.decision=decision;		   
	   }
	   
	   public Choose getChoose(){
		   return choose;
	   }
	   public Decision getDecision()
	   {
		   return decision;
	   }       
	   
}
