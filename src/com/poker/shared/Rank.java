package com.poker.shared;
import java.util.*;
public enum Rank {
	TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;
    public static final Rank[] VALUES=values();
    
    public static final Comparator<Rank> RANK_COMPARATOR = new Comparator<Rank>() {    
        @Override
        public int compare(Rank o1, Rank o2) {
          int ord1 =  o1.ordinal();
          int ord2 =  o2.ordinal();
          return ord1 - ord2;
        }
      };
      
    
    public Rank getNext()
    {
    	if(this==VALUES[VALUES.length-1])
    		return VALUES[0];
    	return values()[ordinal()+1];
    }
    
    public Rank getPrev()
    {
    	if(this==VALUES[0])
    		return VALUES[VALUES.length-1];
    	return values()[ordinal()-1];
    }
}
