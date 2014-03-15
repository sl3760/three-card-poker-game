package com.poker.shared;

import java.util.Comparator;

public enum Suit {
	CLUBS, DIAMONDS, HEARTS, SPADES;
	
	public static final Suit[] VALUES = values();
	
	public static final Comparator<Suit> SUIT_COMPARATOR = new Comparator<Suit>() {    
        @Override
        public int compare(Suit o1, Suit o2) {
          int ord1 =  o1.ordinal();
          int ord2 =  o2.ordinal();
          return ord1 - ord2;
        }
      };
      
	public Suit getNext() {
	    if (this == VALUES[VALUES.length - 1]) {
	      return VALUES[0];      
	    }
	    return values()[ordinal() + 1];
	  }

	  public Suit getPrev() {
	    if (this == VALUES[0]) {
	      return VALUES[VALUES.length - 1];      
	    }
	    return values()[ordinal() - 1];
	  }

}
