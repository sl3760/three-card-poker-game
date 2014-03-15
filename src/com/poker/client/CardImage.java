package com.poker.client;
import com.poker.shared.Card;
import com.poker.shared.Rank;

public class CardImage {
	enum CardImageKind {
	    BACK,
	    NORMAL,
	  }
	
	 public final CardImageKind kind;
	 public final Rank rank;
	 public final Card card;
	 
	 public CardImage(CardImageKind kind, Rank rank, Card card) {
		    this.kind = kind;
		    this.rank = rank;
		    this.card = card;
     }
    
	 
}
