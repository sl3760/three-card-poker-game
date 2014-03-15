package com.poker.client;
import com.poker.shared.Card;
import com.google.gwt.resources.client.ImageResource;

public class CardImageSupplier {
	private CardImages cardImages;
	public CardImageSupplier(CardImages cardImages) {
	    this.cardImages = cardImages;
	}
	
	public ImageResource getResource(CardImage cardImage) {
		    switch (cardImage.kind) {
		      case BACK:
		        return getBackOfCardImage();
		      case NORMAL:
		        return getCardImage(cardImage.card);
		      default:
		        throw new RuntimeException("Forgot kind=" + cardImage.kind);
		    }
     } 
	
	 public ImageResource getBackOfCardImage() {
	    return cardImages.b();
	 }
	 
	 public ImageResource getCardImage(Card card) {
		    switch (card.getRank()) {
		      case ACE:
		        switch (card.getSuit()) {
		          case CLUBS: return cardImages.ac();
		          case DIAMONDS: return cardImages.ad();
		          case HEARTS: return cardImages.ah();
		          case SPADES: return cardImages.as();
		          default: throw new RuntimeException("Forgot suit=" + card.getSuit());
		        }
		      case TWO:
		        switch (card.getSuit()) {
		          case CLUBS: return cardImages.c2();
		          case DIAMONDS: return cardImages.d2();
		          case HEARTS: return cardImages.h2();
		          case SPADES: return cardImages.s2();
		          default: throw new RuntimeException("Forgot suit=" + card.getSuit());
		        }
		      case THREE:
		        switch (card.getSuit()) {
		          case CLUBS: return cardImages.c3();
		          case DIAMONDS: return cardImages.d3();
		          case HEARTS: return cardImages.h3();
		          case SPADES: return cardImages.s3();
		          default: throw new RuntimeException("Forgot suit=" + card.getSuit());
		        }
		      case FOUR:
		        switch (card.getSuit()) {
		          case CLUBS: return cardImages.c4();
		          case DIAMONDS: return cardImages.d4();
		          case HEARTS: return cardImages.h4();
		          case SPADES: return cardImages.s4();
		          default: throw new RuntimeException("Forgot suit=" + card.getSuit());
		        }
		      case FIVE:
		        switch (card.getSuit()) {
		          case CLUBS: return cardImages.c5();
		          case DIAMONDS: return cardImages.d5();
		          case HEARTS: return cardImages.h5();
		          case SPADES: return cardImages.s5();
		          default: throw new RuntimeException("Forgot suit=" + card.getSuit());
		        }
		      case SIX:
		        switch (card.getSuit()) {
		          case CLUBS: return cardImages.c6();
		          case DIAMONDS: return cardImages.d6();
		          case HEARTS: return cardImages.h6();
		          case SPADES: return cardImages.s6();
		          default: throw new RuntimeException("Forgot suit=" + card.getSuit());
		        }
		      case SEVEN:
		        switch (card.getSuit()) {
		          case CLUBS: return cardImages.c7();
		          case DIAMONDS: return cardImages.d7();
		          case HEARTS: return cardImages.h7();
		          case SPADES: return cardImages.s7();
		          default: throw new RuntimeException("Forgot suit=" + card.getSuit());
		        }
		      case EIGHT:
		        switch (card.getSuit()) {
		          case CLUBS: return cardImages.c8();
		          case DIAMONDS: return cardImages.d8();
		          case HEARTS: return cardImages.h8();
		          case SPADES: return cardImages.s8();
		          default: throw new RuntimeException("Forgot suit=" + card.getSuit());
		        }
		      case NINE:
		        switch (card.getSuit()) {
		          case CLUBS:
		            return cardImages.c9();
		          case DIAMONDS:
		            return cardImages.d9();
		          case HEARTS:
		            return cardImages.h9();
		          case SPADES:
		            return cardImages.s9();
		          default:
		            throw new RuntimeException("Forgot suit=" + card.getSuit());
		        }
		      case TEN:
		        switch (card.getSuit()) {
		          case CLUBS:
		            return cardImages.tc();
		          case DIAMONDS:
		            return cardImages.td();
		          case HEARTS:
		            return cardImages.th();
		          case SPADES:
		            return cardImages.ts();
		          default:
		            throw new RuntimeException("Forgot suit=" + card.getSuit());
		        }
		      case JACK:
		        switch (card.getSuit()) {
		          case CLUBS:
		            return cardImages.jc();
		          case DIAMONDS:
		            return cardImages.jd();
		          case HEARTS:
		            return cardImages.jh();
		          case SPADES:
		            return cardImages.js();
		          default:
		            throw new RuntimeException("Forgot suit=" + card.getSuit());
		        }
		      case QUEEN:
		        switch (card.getSuit()) {
		          case CLUBS:
		            return cardImages.qc();
		          case DIAMONDS:
		            return cardImages.qd();
		          case HEARTS:
		            return cardImages.qh();
		          case SPADES:
		            return cardImages.qs();
		          default:
		            throw new RuntimeException("Forgot suit=" + card.getSuit());
		        }
		      case KING:
		        switch (card.getSuit()) {
		          case CLUBS:
		            return cardImages.kc();
		          case DIAMONDS:
		            return cardImages.kd();
		          case HEARTS:
		            return cardImages.kh();
		          case SPADES:
		            return cardImages.ks();
		          default:
		            throw new RuntimeException("Forgot suit=" + card.getSuit());
		        }
		      default:
		        throw new RuntimeException("Forgot rank=" + card.getRank());
		    }
		  }
	 
	     
}