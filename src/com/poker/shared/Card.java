package com.poker.shared;
import java.util.*;
public class Card {
	  private Suit suitValue;
	  private Rank rankValue;

	  public static final Comparator<Card> NORMAL_COMPARATOR = new Comparator<Card>() {
		    @Override
		    public int compare(Card o1, Card o2) {
		      int rankDiff = o1.getRank().compareTo(o2.getRank());
		      return rankDiff;
		    }
		  };

	  	  
	  
	  /**
	   * Creates a new playing card.
	   *
	   * @param suit the suit value of this card.
	   * @param rank the rank value of this card.
	   */
	  
	  public Card(Suit suit, Rank rank) {
	    suitValue = suit;
	    rankValue = rank;
	  }

	  /**
	   * get a copy of this card.
	   * */
	  public Card getCopy() {
	    return new Card(this.suitValue, this.rankValue);
	  }

	  /**
	   * Returns the suit of the card.
	   *
	   * @return a Suit constant representing the suit value of the card.
	   */
	  public Suit getSuit() {
	    return suitValue;
	  }


	  /**
	   * Returns the rank of the card.
	   *
	   * @return a Rank constant representing the rank value of the card.
	   */
	  public Rank getRank() {
	    return rankValue;
	  }

	  /**
	   * Returns a description of this card.
	   *
	   * @return the name of the card.
	   */
	  @Override
	  public String toString() {
	    return rankValue.toString() + " of " + suitValue.toString();
	  }


	  /**
	   * Returns a description of the rank of this card.
	   *
	   * @return the rank value of the card as a string.
	   */
	  public String rankToString() {
	    return rankValue.toString();
	  }


	  /**
	   * Returns a description of the suit of this card.
	   *
	   * @return the suit value of the card as a string.
	   */
	  public String suitToString() {
	    return suitValue.toString();
	  }

}
