package com.poker.client;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.poker.shared.*;
import com.poker.shared.State.HAND;
public class MyPokerAI {
	 /**
	  *  the ratio for Ante:PairsPlusAnte:PairsPlus is 50:30:20
	  * @return
	  */
	 public String getChoose(){
	      Random rand=new Random();
	      int value=rand.nextInt(70)+1;
	      if(value>=1&&value<=50)
	    	  return "Ante";
	      else if(value>=51&&value<=80)
	    	  return "PairsPlusAnte";
	      else if(value>=81&&value<=100)
	    	  return "PairsPlus";
	      else
	    	  return null;
	 }
	 /**
	  *    the ratio for 10:20:50:100 is 50:35:10:5
	  * @return
	  */
     public int getChip(){
    	  Random rand=new Random();
	      int value=rand.nextInt(100)+1;
	      if(value>=1&&value<=50)
	    	  return 10;
	      else if(value>=51&&value<=85)
	    	  return 20;
	      else if(value>=86&&value<=95)
	    	  return 50;
	      else if(value>=96&&value<=100)
	    	  return 100;
	      else
	    	  return 0;    		   
     }
     
     public String getDecision(State state){
    	 ArrayList<Player> players = state.getPlayers();
    	 State.HAND hand=state.getHandType(players.get(1).getPlayerCards());
    	 if(hand==HAND.pair||hand==HAND.flush||hand==HAND.straight||hand==HAND.threeofakind||hand==HAND.straightflush){
    		 return "Play";
    	 }
    	 else{
    		 Collections.sort(players.get(1).getPlayerCards(),Card.NORMAL_COMPARATOR);
    		 if(players.get(1).getPlayerCards().get(2).getRank().compareTo(Rank.TEN)>=0)
    			 return "Play";
    		 else{
    		     Random rand=new Random();
   	             int value=rand.nextInt(13)+1;
   	             if(value>=1&&value<=3)
   	    	          return "Play";
   	             else 
   	    	          return "Flod";
    		 }
    	 }
     }
}
