package com.poker.shared;

import java.util.*;


public class Deck {
     
	 private ArrayList<Card> deck = new ArrayList<Card>();

	 /**
	   * Creates an empty deck of cards.
	   */
	  public Deck() {
	    restoreDeck();    
	  }
	 
	  /**
	   * Copy constructor.
	   * */
	  public Deck(Deck d) {
	    deck.addAll(d.getCards());
	  }

	  public List<Card> getCards() {
	    return Collections.unmodifiableList(deck);
	  }

	  /**
	   * get a copy of this deck.
	   * */
	  public Deck getDeckCopy() {
	    Deck d = new Deck(this);
	    return d;
	  }
	  /**
	   * The number of cards left in the deck.
	   *
	   * @return the number of cards left to be dealt from the deck.
	   */
	  public int getNumberOfCardsRemaining() {
	    return deck.size();
	  }

	  /**
	   * Deal one card from the deck.
	   *
	   * @return a card from the deck, or the null reference if there are no cards left in the deck.
	   */
	  public Card dealCard() {
		Card val;
	    if (getNumberOfCardsRemaining() != 0) {
	      val=deck.get(0);
	      deck.remove(0);
	      return val;
	    } else {
	      return null;
	    }
	  }


	  /**
	   * Shuffles the cards present in the deck.
	   */
	  public void shuffle(IntGenerator generator) {
		    Utils.shuffle(deck, generator);
      }


	  /**
	   * Looks for an empty deck.
	   *
	   * @return <code>true</code> if there are no cards left to be dealt from the deck.
	   */
	  public boolean isEmpty() {
	    if (getNumberOfCardsRemaining() == 0) {
	      return true;
	    }
	    return false;

	  }


	  /**
	   * Restores the deck to "full deck" status.
	   */
	  public void restoreDeck() {
	    deck.clear();
	    for (Suit s : Suit.values()) {
	      for (Rank r : Rank.values()) {
	        deck.add(new Card(s, r));
	      }
	    }
	  }

	  public Card getDeck(int index) {
	    return deck.get(index);
	  }

}
