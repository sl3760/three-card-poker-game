package com.poker.shared;

import java.util.ArrayList;

public class Player { 
	   private int id;
       private int balance;
       private int wagerPairsPlus, wagerAnte,wagerPlay;
       private ArrayList<Card> playerCards;
       private PlayerMove.Choose choose;
       private PlayerMove.Decision decision;
       private GameOver.Result result;
       
       public void setResult(GameOver.Result result){
    	   this.result=result;
       }
       
       public GameOver.Result getResult(){
    	   return this.result;
       }
       public Player(int i){
    	   this.id=i;
    	   this.balance=1000;
       }
       
       public PlayerMove.Choose getChoose(){
    	   return this.choose;
       }
       
       public void setChoose(PlayerMove.Choose choose){
    	   this.choose=choose;
       }
       
       public PlayerMove.Decision getDecision(){
    	   return this.decision;
       }
       
       public void setDecision(PlayerMove.Decision decision){
    	   this.decision=decision;
       }
       
       public int getID(){
    	  return this.id;   
       }
       
       public int getBalance(){
    	   return this.balance;
       }
       public void setBalance(int balance){
    	   this.balance=balance;
       }
       
       public int getWagerPairsPlus(){
    	   return this.wagerPairsPlus;
       }
       public void setWagerPairsPlus(int wagerPairsPlus){
    	   this.wagerPairsPlus=wagerPairsPlus;
       }
       
       public int getWagerAnte(){
    	   return this.wagerAnte;
       }
       public void setWagerAnte(int wagerAnte){
    	   this.wagerAnte=wagerAnte;
       }
       
       public int getWagerPlay(){
    	   return this.wagerPlay;
       }
       public void setWagerPlay(int wagerPlay){
    	   this.wagerPlay=wagerPlay;
       }
       
       public ArrayList<Card> getPlayerCards(){
    	   return this.playerCards;
       }
       
       public void setPlayerCards(ArrayList<Card> cards){
    	   this.playerCards=cards;
       }
       
       public void setChipPairsPlus(int chip){
       	wagerPairsPlus=chip;
       }
       
       public void setChipAnte(int chip){
       	wagerAnte=chip;
       }
       
       public void setChipPlay(int chip){
       	wagerPlay=chip;
       }
       
       public int getChipPairsPlus(){
       	return wagerPairsPlus;
       }
       
       public int getChipAnte(){
       	return wagerAnte;
       }
       
       public int getChipPlay(){
       	return wagerPlay;
       }
         
}
