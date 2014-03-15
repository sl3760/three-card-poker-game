package com.poker;
import java.util.ArrayList;

import org.mockito.Mockito;
import org.junit.Before;
import org.junit.Test;

import com.poker.client.Presenter;
import com.poker.shared.Card;
import com.poker.shared.GameOver;
import com.poker.shared.Player;
import com.poker.shared.PlayerMove;
import com.poker.shared.Rank;
import com.poker.shared.State;
import com.poker.shared.StateChanger;
import com.poker.shared.Suit;

public class PresenterTest {
     Presenter presenter;
     Presenter.View view;
     State state=new State();
     StateChanger stateChanger=new StateChanger();
     @Before
     public void setup() {
       this.presenter = new Presenter();
       this.view = Mockito.mock(Presenter.View.class); 
       presenter.setView(this.view);
     }
     
     @Test
     public void testInitState(){
    	 state.initialize();
    	 ArrayList<Player> players=state.getPlayers();
   	     ArrayList<String> playerIds = new ArrayList<String>();
		  for (int i = 0; i < players.size(); i++) {   		        
		        playerIds.add(String.valueOf(players.get(i).getID()));
		  }
    	 presenter.setState(state);
    	 Mockito.verify(view).setBackground(playerIds);
     }
     
     @Test
     public void testGameOverWithPlayer1Win(){
    	 state.initialize();
    	 ArrayList<Player> players=state.getPlayers();
    	 players.get(0).setBalance(10);
    	 players.get(1).setBalance(-10);
   	     presenter.setState(state);
   	     String result="Game Over! Player ID: 1 Win! Player ID: 2 Lose!";
    	 Mockito.verify(view).showGameWarning(result);
     }
     
     @Test
     public void testGameOverWithPlayer2Win(){
    	 state.initialize();
    	 ArrayList<Player> players=state.getPlayers();
    	 players.get(0).setBalance(-10);
    	 players.get(1).setBalance(100);
   	     presenter.setState(state);
   	     String result="Game Over! Player ID: 1 Lose! Player ID: 2 Win!";
    	 Mockito.verify(view).showGameWarning(result);
     }

     @Test
     public void testGame1(){
    	 state.initialize();
    	 ArrayList<Card> dealer=new ArrayList<Card>();
 		 ArrayList<Card> player1=new ArrayList<Card>();
 		 ArrayList<Card> player2=new ArrayList<Card>();
 		 Card card1=new Card(Suit.CLUBS,Rank.TWO);
 		 Card card2=new Card(Suit.DIAMONDS,Rank.KING);
 		 Card card3=new Card(Suit.SPADES,Rank.TEN);
 		 dealer.add(card1);
 	     dealer.add(card2);
 		 dealer.add(card3);
 		 Card card4=new Card(Suit.DIAMONDS,Rank.ACE);
 		 Card card5=new Card(Suit.CLUBS,Rank.JACK);
 		 Card card6=new Card(Suit.HEARTS,Rank.FIVE);
 		 player1.add(card4);
 	     player1.add(card5);
 		 player1.add(card6);
 		 Card card7=new Card(Suit.DIAMONDS,Rank.TEN);
		 Card card8=new Card(Suit.CLUBS,Rank.NINE);
		 Card card9=new Card(Suit.HEARTS,Rank.SEVEN);
		 player2.add(card7);
	     player2.add(card8);
		 player2.add(card9);
		 state.setDealerCards(dealer);
		 state.setPlayerCards(0,player1);
		 state.setPlayerCards(1,player2);
		 ArrayList<ArrayList<Card>> desk = new ArrayList<ArrayList<Card>>();
		 desk.add(player1);
		 desk.add(player2);
		 state.setDesk();
		 PlayerMove move=new PlayerMove();
	     move.setPlayerChoose(PlayerMove.Choose.PairsPlus);
		 stateChanger.setChoice(state,0,move,10,0);
		 stateChanger.setChoice(state,1,move,10,0);
         presenter.setState(state); 
         Mockito.verify(view).showGameInfo(3);
         Mockito.verify(view).showBalance1(1000);
    	 Mockito.verify(view).showBalance2(1000);
         Mockito.verify(view).setCards(dealer, desk);        
     }
     
     @Test
     public void testGame2(){
    	 state.initialize();
    	 ArrayList<Card> dealer=new ArrayList<Card>();
 		 ArrayList<Card> player1=new ArrayList<Card>();
 		 ArrayList<Card> player2=new ArrayList<Card>();
 		 Card card1=new Card(Suit.CLUBS,Rank.TWO);
 		 Card card2=new Card(Suit.DIAMONDS,Rank.KING);
 		 Card card3=new Card(Suit.SPADES,Rank.TEN);
 		 dealer.add(card1);
 	     dealer.add(card2);
 		 dealer.add(card3);
 		 Card card4=new Card(Suit.DIAMONDS,Rank.ACE);
 		 Card card5=new Card(Suit.CLUBS,Rank.JACK);
 		 Card card6=new Card(Suit.HEARTS,Rank.FIVE);
 		 player1.add(card4);
 	     player1.add(card5);
 		 player1.add(card6);
 		 Card card7=new Card(Suit.DIAMONDS,Rank.TEN);
		 Card card8=new Card(Suit.CLUBS,Rank.NINE);
		 Card card9=new Card(Suit.HEARTS,Rank.SEVEN);
		 player2.add(card7);
	     player2.add(card8);
		 player2.add(card9);
		 state.setDealerCards(dealer);
		 state.setPlayerCards(0,player1);
		 state.setPlayerCards(1,player2);
		 ArrayList<ArrayList<Card>> desk = new ArrayList<ArrayList<Card>>();
		 desk.add(player1);
		 desk.add(player2);
		 state.setDesk();
		 PlayerMove move=new PlayerMove();
	     move.setPlayerChoose(PlayerMove.Choose.Ante);
		 stateChanger.setChoice(state,0,move,0,10);
		 stateChanger.setChoice(state,1,move,0,10);
		 move.setPlayerDecision(PlayerMove.Decision.Play);
         stateChanger.makeMove(state,0,move);
         presenter.setState(state);         
         Mockito.verify(view).showGameInfo(4);
         Mockito.verify(view).showBalance1(1000);
         Mockito.verify(view).showBalance2(1000);
     }
     
     @Test
     public void testGame3(){
    	 state.initialize();
    	 ArrayList<Card> dealer=new ArrayList<Card>();
 		 ArrayList<Card> player1=new ArrayList<Card>();
 		 ArrayList<Card> player2=new ArrayList<Card>();
 		 Card card1=new Card(Suit.CLUBS,Rank.TWO);
 		 Card card2=new Card(Suit.DIAMONDS,Rank.KING);
 		 Card card3=new Card(Suit.SPADES,Rank.TEN);
 		 dealer.add(card1);
 	     dealer.add(card2);
 		 dealer.add(card3);
 		 Card card4=new Card(Suit.DIAMONDS,Rank.ACE);
 		 Card card5=new Card(Suit.CLUBS,Rank.JACK);
 		 Card card6=new Card(Suit.HEARTS,Rank.FIVE);
 		 player1.add(card4);
 	     player1.add(card5);
 		 player1.add(card6);
 		 Card card7=new Card(Suit.DIAMONDS,Rank.TEN);
		 Card card8=new Card(Suit.CLUBS,Rank.NINE);
		 Card card9=new Card(Suit.HEARTS,Rank.SEVEN);
		 player2.add(card7);
	     player2.add(card8);
		 player2.add(card9);
		 state.setDealerCards(dealer);
		 state.setPlayerCards(0,player1);
		 state.setPlayerCards(1,player2);
		 ArrayList<ArrayList<Card>> desk = new ArrayList<ArrayList<Card>>();
		 desk.add(player1);
		 desk.add(player2);
		 state.setDesk();
		 PlayerMove move=new PlayerMove();
	     move.setPlayerChoose(PlayerMove.Choose.PairsPlus);
		 stateChanger.setChoice(state,0,move,10,0);
		 stateChanger.setChoice(state,1,move,10,0);
		 move.setPlayerDecision(PlayerMove.Decision.Deal);
		 GameOver gameResult1=stateChanger.makeMove(state,0,move);
		 GameOver gameResult2=stateChanger.makeMove(state,1,move);
		 state.setPlayerBalance(0,gameResult1.getBalance());
		 state.setPlayerBalance(1,gameResult2.getBalance());
         presenter.setState(state);         
         Mockito.verify(view).showGameInfo(5);
         Mockito.verify(view).showBalance1(990);
         Mockito.verify(view).showBalance2(990);
         ArrayList<String> playerCardInfo=new ArrayList<String>();
         playerCardInfo.add("ID: 1 high");
         playerCardInfo.add("ID: 2 high");
         Mockito.verify(view).showStatus("Dealer: high",playerCardInfo);
     }
     
     @Test
     public void testGame4(){
    	 state.initialize();
    	 ArrayList<Card> dealer=new ArrayList<Card>();
 		 ArrayList<Card> player1=new ArrayList<Card>();
 		 ArrayList<Card> player2=new ArrayList<Card>();
 		 Card card1=new Card(Suit.CLUBS,Rank.TWO);
 		 Card card2=new Card(Suit.DIAMONDS,Rank.NINE);
 		 Card card3=new Card(Suit.SPADES,Rank.TEN);
 		 dealer.add(card1);
 	     dealer.add(card2);
 		 dealer.add(card3);
 		 Card card4=new Card(Suit.DIAMONDS,Rank.FIVE);
 		 Card card5=new Card(Suit.CLUBS,Rank.JACK);
 		 Card card6=new Card(Suit.HEARTS,Rank.FIVE);
 		 player1.add(card4);
 	     player1.add(card5);
 		 player1.add(card6);
 		 Card card7=new Card(Suit.DIAMONDS,Rank.TEN);
		 Card card8=new Card(Suit.CLUBS,Rank.ACE);
		 Card card9=new Card(Suit.HEARTS,Rank.SEVEN);
		 player2.add(card7);
	     player2.add(card8);
		 player2.add(card9);
		 state.setDealerCards(dealer);
		 state.setPlayerCards(0,player1);
		 state.setPlayerCards(1,player2);
		 ArrayList<ArrayList<Card>> desk = new ArrayList<ArrayList<Card>>();
		 desk.add(player1);
		 desk.add(player2);
		 state.setDesk();
		 PlayerMove move=new PlayerMove();
	     move.setPlayerChoose(PlayerMove.Choose.PairsPlus);
		 stateChanger.setChoice(state,0,move,10,0);
		 move.setPlayerChoose(PlayerMove.Choose.Ante);
		 stateChanger.setChoice(state,1,move,0,10);
		 move.setPlayerDecision(PlayerMove.Decision.Deal);
		 GameOver gameResult1=stateChanger.makeMove(state,0,move);
		 move.setPlayerDecision(PlayerMove.Decision.Play);
		 GameOver gameResult2=stateChanger.makeMove(state,1,move);
		 state.setPlayerBalance(0,gameResult1.getBalance());
		 state.setPlayerBalance(1,gameResult2.getBalance());
		 state.setResult("Player ID:1 You Win!  Player ID 2: You Win Press ReStartGame button to continue this game!");
         presenter.setState(state);         
         Mockito.verify(view).showGameInfo(5);
         Mockito.verify(view).showBalance1(1010);
         Mockito.verify(view).showBalance2(1010);
         ArrayList<String> playerCardInfo=new ArrayList<String>();
         playerCardInfo.add("ID: 1 pair");
         playerCardInfo.add("ID: 2 high");
         Mockito.verify(view).showStatus("Dealer: did not qualify!",playerCardInfo);
         Mockito.verify(view).showGameWarning("Player ID:1 You Win!  Player ID 2: You Win Press ReStartGame button to continue this game!");
     }
}
