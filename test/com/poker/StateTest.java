package com.poker;

import com.poker.shared.Card;
import com.poker.shared.GameOver;
import com.poker.shared.PlayerMove;
import com.poker.shared.Rank;
import com.poker.shared.State;
import com.poker.shared.StateChanger;
import com.poker.shared.Suit;

import java.util.*;

import static org.junit.Assert.*;

import org.junit.Test;

public class StateTest {
    State state=new State();
    StateChanger stateChanger=new StateChanger();
	@Test
	public void testBeginningBalance() {
		int expectedBeginningBalance=1000;
		int actualBeginningBalance1=state.getPlayers().get(0).getBalance();
		int actualBeginningBalance2=state.getPlayers().get(1).getBalance();
		assertEquals(actualBeginningBalance1,expectedBeginningBalance);
		assertEquals(actualBeginningBalance2,expectedBeginningBalance);
	}
	
	@Test
	public void testInitial() {
		state.initialize();
		int expectedCardOfNumber=52;
		int expectedWagerPairsPlus=0;
		int expectedWagerAnte=0;
		int expectedWagerPlay=0;
		assertEquals(state.getDealerCards(),null);
		assertEquals(state.getPlayers().get(0).getPlayerCards(),null);
		assertEquals(state.getPlayers().get(1).getPlayerCards(),null);
		assertEquals(state.getDeck().getNumberOfCardsRemaining(),expectedCardOfNumber);
		assertEquals(state.getPlayers().get(0).getChipPairsPlus(),expectedWagerPairsPlus);
		assertEquals(state.getPlayers().get(1).getChipPairsPlus(),expectedWagerPairsPlus);
		assertEquals(state.getPlayers().get(0).getChipAnte(),expectedWagerAnte);
		assertEquals(state.getPlayers().get(1).getChipAnte(),expectedWagerAnte);
		assertEquals(state.getPlayers().get(0).getChipPlay(),expectedWagerPlay);
		assertEquals(state.getPlayers().get(1).getChipPlay(),expectedWagerPlay);
	}
   
	@Test
	public void testSetChipForPairsPlus(){
		state.initialize();
		PlayerMove move=new PlayerMove();
		move.setPlayerChoose(PlayerMove.Choose.PairsPlus);
		stateChanger.setChoice(state,0,move,10,0);
		int expectedWagerPairsPlus=10;
		int expectedWagerAnte=0;
		int expectedWagerPlay=0;
		assertEquals(state.getPlayers().get(0).getChipPairsPlus(),expectedWagerPairsPlus);
		assertEquals(state.getPlayers().get(0).getChipAnte(),expectedWagerAnte);
		assertEquals(state.getPlayers().get(0).getChipPlay(),expectedWagerPlay);
	}
	
	@Test
	public void testSetChipForAnte(){
		state.initialize();
		PlayerMove move=new PlayerMove();
		move.setPlayerChoose(PlayerMove.Choose.Ante);
		stateChanger.setChoice(state,1,move,0,10);
		int expectedWagerPairsPlus=0;
		int expectedWagerAnte=10;
		int expectedWagerPlay=0;
		assertEquals(state.getPlayers().get(1).getChipPairsPlus(),expectedWagerPairsPlus);
		assertEquals(state.getPlayers().get(1).getChipAnte(),expectedWagerAnte);
		assertEquals(state.getPlayers().get(1).getChipPlay(),expectedWagerPlay);
	}
	
	@Test
	public void testSetChipForPairsPlusAnte(){
		state.initialize();
		PlayerMove move=new PlayerMove();
		move.setPlayerChoose(PlayerMove.Choose.PairsPlusAnte);
		stateChanger.setChoice(state,0,move,10,10);
		int expectedWagerPairsPlus=10;
		int expectedWagerAnte=10;
		int expectedWagerPlay=0;
		assertEquals(state.getPlayers().get(0).getChipPairsPlus(),expectedWagerPairsPlus);
		assertEquals(state.getPlayers().get(0).getChipAnte(),expectedWagerAnte);
		assertEquals(state.getPlayers().get(0).getChipPlay(),expectedWagerPlay);
	}
	
	@Test
	public void testGiveCards(){
		state.initialize();
		PlayerMove move=new PlayerMove();
		move.setPlayerChoose(PlayerMove.Choose.PairsPlus);
		stateChanger.setChoice(state,0,move,10,0);
		stateChanger.setChoice(state,1,move,10,10);
		state.giveCards();
		int expectedNumberOfCards=3;
		assertEquals(state.getPlayers().get(0).getPlayerCards().size(),expectedNumberOfCards);
		assertEquals(state.getPlayers().get(1).getPlayerCards().size(),expectedNumberOfCards);
		assertEquals(state.getDealerCards().size(),expectedNumberOfCards);
	}
	
	@Test
	public void testPlayerCardsWithStraigtFlush(){
		state.initialize();
		ArrayList<Card> player=new ArrayList<Card>();
		Card card1=new Card(Suit.CLUBS,Rank.TWO);
		Card card2=new Card(Suit.CLUBS,Rank.THREE);
		Card card3=new Card(Suit.CLUBS,Rank.FOUR);
		player.add(card1);
		player.add(card2);
		player.add(card3);
		state.setPlayerCards(0,player);
		State.HAND expectedHandtype=State.HAND.straightflush;
		assertEquals(state.getHandType(state.getPlayers().get(0).getPlayerCards()),expectedHandtype);		
	}
	
	@Test
	public void testDealerCardsWithThreeOfAKind(){
		state.initialize();
		ArrayList<Card> dealer=new ArrayList<Card>();
		Card card1=new Card(Suit.CLUBS,Rank.TWO);
		Card card2=new Card(Suit.HEARTS,Rank.TWO);
		Card card3=new Card(Suit.DIAMONDS,Rank.TWO);
		dealer.add(card1);
	    dealer.add(card2);
		dealer.add(card3);
		state.setDealerCards(dealer);
		State.HAND expectedHandtype=State.HAND.threeofakind;
		assertEquals(state.getHandType(state.getDealerCards()),expectedHandtype);		
	}
	
	@Test
	public void testPlayerCardsWithStraight(){
		state.initialize();
		ArrayList<Card> player=new ArrayList<Card>();
		Card card1=new Card(Suit.CLUBS,Rank.TWO);
		Card card2=new Card(Suit.DIAMONDS,Rank.THREE);
		Card card3=new Card(Suit.SPADES,Rank.FOUR);
		player.add(card1);
		player.add(card2);
		player.add(card3);
		state.setPlayerCards(1,player);
		State.HAND expectedHandtype=State.HAND.straight;
		assertEquals(state.getHandType(state.getPlayers().get(1).getPlayerCards()),expectedHandtype);		
	}
	
	@Test
	public void testDealerCardsWithFlush(){
		state.initialize();
		ArrayList<Card> dealer=new ArrayList<Card>();
		Card card1=new Card(Suit.CLUBS,Rank.TWO);
		Card card2=new Card(Suit.CLUBS,Rank.KING);
		Card card3=new Card(Suit.CLUBS,Rank.TEN);
		dealer.add(card1);
	    dealer.add(card2);
		dealer.add(card3);
		state.setDealerCards(dealer);
		State.HAND expectedHandtype=State.HAND.flush;
		assertEquals(state.getHandType(state.getDealerCards()),expectedHandtype);		
	}
	
	@Test
	public void testPlayerCardsWithPair(){
		state.initialize();
		ArrayList<Card> player=new ArrayList<Card>();
		Card card1=new Card(Suit.CLUBS,Rank.NINE);
		Card card2=new Card(Suit.DIAMONDS,Rank.THREE);
		Card card3=new Card(Suit.SPADES,Rank.NINE);
		player.add(card1);
		player.add(card2);
		player.add(card3);
		state.setPlayerCards(0,player);
		State.HAND expectedHandtype=State.HAND.pair;
		assertEquals(state.getHandType(state.getPlayers().get(0).getPlayerCards()),expectedHandtype);		
	}
	
	@Test
	public void testDealerCardsWithHigh(){
		state.initialize();
		ArrayList<Card> dealer=new ArrayList<Card>();
		Card card1=new Card(Suit.CLUBS,Rank.TWO);
		Card card2=new Card(Suit.DIAMONDS,Rank.KING);
		Card card3=new Card(Suit.SPADES,Rank.TEN);
		dealer.add(card1);
	    dealer.add(card2);
		dealer.add(card3);
		state.setDealerCards(dealer);
		State.HAND expectedHandtype=State.HAND.high;
		assertEquals(state.getHandType(state.getDealerCards()),expectedHandtype);		
	}
	
	@Test
	public void testDealerCardsQualify(){
		state.initialize();
		ArrayList<Card> dealer=new ArrayList<Card>();
		Card card1=new Card(Suit.CLUBS,Rank.TWO);
		Card card2=new Card(Suit.DIAMONDS,Rank.KING);
		Card card3=new Card(Suit.SPADES,Rank.TEN);
		dealer.add(card1);
	    dealer.add(card2);
		dealer.add(card3);
		state.setDealerCards(dealer);
		boolean expected=true;
		assertEquals(state.dealerCardsQualify(state.getDealerCards()),expected);		
	}
	
	@Test
	public void testPlayerWinPairsPlus(){
		state.initialize();
		ArrayList<Card> player=new ArrayList<Card>();
		Card card1=new Card(Suit.CLUBS,Rank.NINE);
		Card card2=new Card(Suit.DIAMONDS,Rank.THREE);
		Card card3=new Card(Suit.SPADES,Rank.NINE);
		player.add(card1);
		player.add(card2);
		player.add(card3);
		state.setPlayerCards(0,player);
		boolean expected=true;
		assertEquals(state.playerCardsWinPairsPlus(state.getPlayers().get(0).getPlayerCards()),expected);
	}
	
	@Test
	public void testPlayerCardsGreaterDealerCards(){
		state.initialize();
		ArrayList<Card> dealer=new ArrayList<Card>();
		ArrayList<Card> player=new ArrayList<Card>();
		Card card1=new Card(Suit.CLUBS,Rank.TWO);
		Card card2=new Card(Suit.DIAMONDS,Rank.KING);
		Card card3=new Card(Suit.SPADES,Rank.TEN);
		dealer.add(card1);
	    dealer.add(card2);
		dealer.add(card3);
		Card card4=new Card(Suit.DIAMONDS,Rank.ACE);
		Card card5=new Card(Suit.DIAMONDS,Rank.JACK);
		Card card6=new Card(Suit.DIAMONDS,Rank.FIVE);
		player.add(card4);
	    player.add(card5);
		player.add(card6);	
		state.setDealerCards(dealer);
		state.setPlayerCards(0,player);
		GameOver.Result expectedWin=GameOver.Result.WIN;
		assertEquals(state.PlayerCompareDealer(state.getPlayers().get(0).getPlayerCards(), state.getDealerCards()),expectedWin);
	}
	
	@Test
	public void testMakeMoveDeal(){
		state.initialize();
		ArrayList<Card> player=new ArrayList<Card>();		
		Card card4=new Card(Suit.DIAMONDS,Rank.ACE);
		Card card5=new Card(Suit.CLUBS,Rank.JACK);
		Card card6=new Card(Suit.SPADES,Rank.FIVE);
		player.add(card4);
	    player.add(card5);
		player.add(card6);		
		state.setPlayerCards(0,player);
		PlayerMove move=new PlayerMove();
		move.setPlayerChoose(PlayerMove.Choose.PairsPlus);
		stateChanger.setChoice(state,0,move,10,0);
		move.setPlayerDecision(PlayerMove.Decision.Deal);	
		int expectedBalance=990;
		GameOver.Result expectedLose=GameOver.Result.LOSE;
		GameOver gameover=stateChanger.makeMove(state,0,move);
		assertEquals(gameover.getResult(),expectedLose);
		assertEquals(gameover.getBalance(),expectedBalance);	 	
	}
	
	@Test
	public void testMakeMoveFold(){
		state.initialize();
		ArrayList<Card> player=new ArrayList<Card>();		
		Card card4=new Card(Suit.DIAMONDS,Rank.ACE);
		Card card5=new Card(Suit.CLUBS,Rank.JACK);
		Card card6=new Card(Suit.SPADES,Rank.FIVE);
		player.add(card4);
	    player.add(card5);
		player.add(card6);		
		state.setPlayerCards(1,player);
		PlayerMove move=new PlayerMove();
		move.setPlayerChoose(PlayerMove.Choose.Ante);
		stateChanger.setChoice(state,1,move,0,10);
		move.setPlayerDecision(PlayerMove.Decision.Fold);	
		int expectedBalance=990;
		GameOver.Result expectedLose=GameOver.Result.LOSE;
		GameOver gameover=stateChanger.makeMove(state,1,move);
		assertEquals(gameover.getResult(),expectedLose);
		assertEquals(gameover.getBalance(),expectedBalance);	 	
	}
	
	@Test
	public void testMakeMovePlay(){
		state.initialize();
		ArrayList<Card> dealer=new ArrayList<Card>();
		ArrayList<Card> player=new ArrayList<Card>();
		Card card1=new Card(Suit.CLUBS,Rank.TWO);
		Card card2=new Card(Suit.DIAMONDS,Rank.KING);
		Card card3=new Card(Suit.SPADES,Rank.TEN);
		dealer.add(card1);
	    dealer.add(card2);
		dealer.add(card3);
		Card card4=new Card(Suit.DIAMONDS,Rank.ACE);
		Card card5=new Card(Suit.CLUBS,Rank.JACK);
		Card card6=new Card(Suit.HEARTS,Rank.FIVE);
		player.add(card4);
	    player.add(card5);
		player.add(card6);
		state.setDealerCards(dealer);
		state.setPlayerCards(0,player);
		PlayerMove move=new PlayerMove();
		move.setPlayerChoose(PlayerMove.Choose.Ante);
		stateChanger.setChoice(state,0,move,0,10);
		move.setPlayerDecision(PlayerMove.Decision.Play);	
		int expectedBalance=1020;
		GameOver.Result expectedWin=GameOver.Result.WIN;
		GameOver gameover=stateChanger.makeMove(state,0,move);
		assertEquals(gameover.getResult(),expectedWin);
		assertEquals(gameover.getBalance(),expectedBalance);	 
	}
	
	@Test
	public void testMakeMoveFoldWithPairsPlus(){
		state.initialize();
		ArrayList<Card> player=new ArrayList<Card>();		
		Card card4=new Card(Suit.DIAMONDS,Rank.ACE);
		Card card5=new Card(Suit.CLUBS,Rank.JACK);
		Card card6=new Card(Suit.SPADES,Rank.FIVE);
		player.add(card4);
	    player.add(card5);
		player.add(card6);		
		state.setPlayerCards(1,player);
		PlayerMove move=new PlayerMove();
		move.setPlayerChoose(PlayerMove.Choose.PairsPlusAnte);
		stateChanger.setChoice(state,1,move,10,10);
		move.setPlayerDecision(PlayerMove.Decision.Fold);	
		int expectedBalance=980;
		GameOver.Result expectedLose=GameOver.Result.LOSE;
		GameOver gameover=stateChanger.makeMove(state,1,move);
		assertEquals(gameover.getResult(),expectedLose);
		assertEquals(gameover.getBalance(),expectedBalance);	 	
	}
	
	@Test
	public void testMakeMovePlayWithPairsPlus(){
		state.initialize();
		ArrayList<Card> dealer=new ArrayList<Card>();
		ArrayList<Card> player=new ArrayList<Card>();
		Card card1=new Card(Suit.CLUBS,Rank.TWO);
		Card card2=new Card(Suit.DIAMONDS,Rank.KING);
		Card card3=new Card(Suit.SPADES,Rank.TEN);
		dealer.add(card1);
	    dealer.add(card2);
		dealer.add(card3);
		Card card4=new Card(Suit.DIAMONDS,Rank.ACE);
		Card card5=new Card(Suit.CLUBS,Rank.JACK);
		Card card6=new Card(Suit.HEARTS,Rank.FIVE);
		player.add(card4);
	    player.add(card5);
		player.add(card6);
		state.setDealerCards(dealer);
		state.setPlayerCards(0,player);
		PlayerMove move=new PlayerMove();
		move.setPlayerChoose(PlayerMove.Choose.PairsPlusAnte);
		stateChanger.setChoice(state,0,move,10,10);
		int expectedBalance=990;
		GameOver.Result expectedLose=GameOver.Result.LOSE;
		move.setPlayerDecision(PlayerMove.Decision.Deal);
		GameOver gameover=stateChanger.makeMove(state,0,move);
		assertEquals(gameover.getResult(),expectedLose);
		assertEquals(gameover.getBalance(),expectedBalance);
		state.setPlayerBalance(0,gameover.getBalance());
		expectedBalance=1010;
		GameOver.Result expectedWin=GameOver.Result.WIN;
		move.setPlayerDecision(PlayerMove.Decision.Play);
		gameover=stateChanger.makeMove(state,0,move);
		assertEquals(gameover.getResult(),expectedWin);
		assertEquals(gameover.getBalance(),expectedBalance);			 
	}
	
	@Test
	public void testMakeMovePlayBonusWithDealerUnqualifiy(){
		state.initialize();
		ArrayList<Card> dealer=new ArrayList<Card>();
		ArrayList<Card> player=new ArrayList<Card>();
		Card card1=new Card(Suit.CLUBS,Rank.TWO);
		Card card2=new Card(Suit.DIAMONDS,Rank.NINE);
		Card card3=new Card(Suit.SPADES,Rank.TEN);
		dealer.add(card1);
	    dealer.add(card2);
		dealer.add(card3);
		Card card4=new Card(Suit.DIAMONDS,Rank.ACE);
		Card card5=new Card(Suit.CLUBS,Rank.ACE);
		Card card6=new Card(Suit.HEARTS,Rank.ACE);
		player.add(card4);
	    player.add(card5);
		player.add(card6);
		state.setDealerCards(dealer);
		state.setPlayerCards(1,player);
		PlayerMove move=new PlayerMove();
		move.setPlayerChoose(PlayerMove.Choose.Ante);
		stateChanger.setChoice(state,1,move,0,10);
		int expectedBalance=1050;
		GameOver.Result expectedWin=GameOver.Result.WIN;
		move.setPlayerDecision(PlayerMove.Decision.Play);
		GameOver gameover=stateChanger.makeMove(state,1,move);
		assertEquals(gameover.getResult(),expectedWin);
		assertEquals(gameover.getBalance(),expectedBalance);			 
	}
	
	@Test
	public void testMakeMovePlayBonusWithDealerQualifiy(){
		state.initialize();
		ArrayList<Card> dealer=new ArrayList<Card>();
		ArrayList<Card> player=new ArrayList<Card>();
		Card card1=new Card(Suit.CLUBS,Rank.TWO);
		Card card2=new Card(Suit.DIAMONDS,Rank.KING);
		Card card3=new Card(Suit.SPADES,Rank.TEN);
		dealer.add(card1);
	    dealer.add(card2);
		dealer.add(card3);
		Card card4=new Card(Suit.DIAMONDS,Rank.ACE);
		Card card5=new Card(Suit.CLUBS,Rank.ACE);
		Card card6=new Card(Suit.HEARTS,Rank.ACE);
		player.add(card4);
	    player.add(card5);
		player.add(card6);
		state.setDealerCards(dealer);
		state.setPlayerCards(0,player);
		PlayerMove move=new PlayerMove();
		move.setPlayerChoose(PlayerMove.Choose.Ante);
		stateChanger.setChoice(state,0,move,0,10);
		int expectedBalance=1060;
		GameOver.Result expectedWin=GameOver.Result.WIN;
		move.setPlayerDecision(PlayerMove.Decision.Play);
		GameOver gameover=stateChanger.makeMove(state,0,move);
		assertEquals(gameover.getResult(),expectedWin);
		assertEquals(gameover.getBalance(),expectedBalance);			 
	}
	
	@Test
	public void testMakeMovePlayTie(){
		state.initialize();
		ArrayList<Card> dealer=new ArrayList<Card>();
		ArrayList<Card> player=new ArrayList<Card>();
		Card card1=new Card(Suit.CLUBS,Rank.TWO);
		Card card2=new Card(Suit.DIAMONDS,Rank.ACE);
		Card card3=new Card(Suit.SPADES,Rank.TEN);
		dealer.add(card1);
	    dealer.add(card2);
		dealer.add(card3);
		Card card4=new Card(Suit.CLUBS,Rank.ACE);
		Card card5=new Card(Suit.DIAMONDS,Rank.TWO);
		Card card6=new Card(Suit.HEARTS,Rank.TEN);
		player.add(card4);
	    player.add(card5);
		player.add(card6);
		state.setDealerCards(dealer);
		state.setPlayerCards(0,player);
		PlayerMove move=new PlayerMove();
		move.setPlayerChoose(PlayerMove.Choose.Ante);
		stateChanger.setChoice(state,0,move,0,10);
		int expectedBalance=1000;
		GameOver.Result expectedTie=GameOver.Result.TIE;
		move.setPlayerDecision(PlayerMove.Decision.Play);
		GameOver gameover=stateChanger.makeMove(state,0,move);
		assertEquals(gameover.getResult(),expectedTie);
		assertEquals(gameover.getBalance(),expectedBalance);			 
	}
	
	@Test
	public void testMakeMovePlayPairsPlusAnteWithTIE(){
		state.initialize();
		ArrayList<Card> dealer=new ArrayList<Card>();
		ArrayList<Card> player=new ArrayList<Card>();
		Card card1=new Card(Suit.SPADES,Rank.ACE);
		Card card2=new Card(Suit.HEARTS,Rank.ACE);
		Card card3=new Card(Suit.SPADES,Rank.TEN);
		dealer.add(card1);
	    dealer.add(card2);
		dealer.add(card3);
		Card card4=new Card(Suit.DIAMONDS,Rank.ACE);
		Card card5=new Card(Suit.CLUBS,Rank.ACE);
		Card card6=new Card(Suit.HEARTS,Rank.TEN);
		player.add(card4);
	    player.add(card5);
		player.add(card6);
		state.setDealerCards(dealer);
		state.setPlayerCards(1,player);
		PlayerMove move=new PlayerMove();
		move.setPlayerChoose(PlayerMove.Choose.PairsPlusAnte);
		stateChanger.setChoice(state,1,move,10,10);
		int expectedBalance=1010;
		GameOver.Result expectedWin=GameOver.Result.WIN;
		move.setPlayerDecision(PlayerMove.Decision.Deal);
		GameOver gameover=stateChanger.makeMove(state,1,move);
		assertEquals(gameover.getResult(),expectedWin);
		assertEquals(gameover.getBalance(),expectedBalance);	
		state.setPlayerBalance(1,gameover.getBalance());
		expectedBalance=1010;
		GameOver.Result expectedTie=GameOver.Result.TIE;
		move.setPlayerDecision(PlayerMove.Decision.Play);
		gameover=stateChanger.makeMove(state,1,move);
		assertEquals(gameover.getResult(),expectedTie);
		assertEquals(gameover.getBalance(),expectedBalance);
	}
}
