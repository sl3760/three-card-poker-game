package com.poker.server;

import java.util.ArrayList;

import com.poker.shared.Card;
import com.poker.shared.GameOver;
import com.poker.shared.Rank;
import com.poker.shared.State;
import com.poker.shared.Suit;
import com.poker.shared.PlayerMove.Choose;
import com.poker.shared.PlayerMove.Decision;

public class StateSerializer {
	public static String serializeState(State state) {
		String serialized = String.valueOf(state.getPlayerTurn()) + "_";
		serialized += String.valueOf(state.getProcess());
		serialized += "_"
				+ String.valueOf(state.getPlayers().get(0).getBalance());
		serialized += "_"
				+ String.valueOf(state.getPlayers().get(1).getBalance());
		if (state.getProcess() != 0) {
			ArrayList<Card> dealerCards = state.getDealerCards();
			for (int i = 0; i < 3; i++) {
				Suit suit = dealerCards.get(i).getSuit();
				serialized += "_" + suit.name() + "_";
				Rank rank = dealerCards.get(i).getRank();
				serialized += rank.name();
			}
			ArrayList<Card> player1Cards = state.getPlayers().get(0)
					.getPlayerCards();
			for (int i = 0; i < 3; i++) {
				Suit suit = player1Cards.get(i).getSuit();
				serialized += "_" + suit.name() + "_";
				Rank rank = player1Cards.get(i).getRank();
				serialized += rank.name();
			}
			ArrayList<Card> player2Cards = state.getPlayers().get(1)
					.getPlayerCards();
			for (int i = 0; i < 3; i++) {
				Suit suit = player2Cards.get(i).getSuit();
				serialized += "_" + suit.name() + "_";
				Rank rank = player2Cards.get(i).getRank();
				serialized += rank.name();
			}
		}

		if (state.getProcess() == 2) {
			Choose choose = state.getPlayers().get(0).getChoose();
			serialized += "_" + choose.name();
		}
		if (state.getProcess() == 3) {
			Choose choose1 = state.getPlayers().get(0).getChoose();
			serialized += "_" + choose1.name() + "_";
			Choose choose2 = state.getPlayers().get(1).getChoose();
			serialized += choose2.name();
		}
		if (state.getProcess() == 4) {
			Choose choose1 = state.getPlayers().get(0).getChoose();
			serialized += "_" + choose1.name() + "_";
			Choose choose2 = state.getPlayers().get(1).getChoose();
			serialized += choose2.name() + "_";
			Decision decision1 = state.getPlayers().get(0).getDecision();
			serialized += decision1.name()+ "_";
			serialized += state.getPlayers().get(0).getResult();
		}
		if (state.getProcess() == 5) {
			Choose choose1 = state.getPlayers().get(0).getChoose();
			serialized += "_" + choose1.name() + "_";
			Choose choose2 = state.getPlayers().get(1).getChoose();
			serialized += choose2.name() + "_";
			Decision decision1 = state.getPlayers().get(0).getDecision();
			serialized += decision1.name() + "_";
			serialized += state.getPlayers().get(0).getResult() +"_";
			Decision decision2 = state.getPlayers().get(1).getDecision();
			serialized += decision2.name() + "_";
			serialized += state.getPlayers().get(1).getResult();		
		}
		return serialized;
	}
	
	public static State unserializeState(String serialized) {
		try {
			State state=new State();
			String[] tokens = serialized.split("_");
			int turn = Integer.parseInt(tokens[0]);
			state.setPlayerTurn(turn);
			int process = Integer.parseInt(tokens[1]);
			state.setProcess(process);
			state.getPlayers().get(0).setBalance(Integer.parseInt(tokens[2]));
			state.getPlayers().get(1).setBalance(Integer.parseInt(tokens[3]));
			if (process >= 1) {
				ArrayList<Card> dealerCards = new ArrayList<Card>();
				ArrayList<Card> player1Cards = new ArrayList<Card>();
				ArrayList<Card> player2Cards = new ArrayList<Card>();
				for (int i = 4; i < 9;) {
					Suit suit = Enum.valueOf(Suit.class, tokens[i]);

					Rank rank = Enum.valueOf(Rank.class, tokens[i + 1]);
					Card card = new Card(suit, rank);
					dealerCards.add(card);
					i += 2;
				}
				state.setDealerCards(dealerCards);
				for (int i = 10; i < 15;) {
					Suit suit = Enum.valueOf(Suit.class, tokens[i]);
					Rank rank = Enum.valueOf(Rank.class, tokens[i + 1]);
					Card card = new Card(suit, rank);
					player1Cards.add(card);
					i += 2;
				}
				state.setPlayerCards(0, player1Cards);
				for (int i = 16; i < 21;) {
					Suit suit = Enum.valueOf(Suit.class, tokens[i]);
					Rank rank = Enum.valueOf(Rank.class, tokens[i + 1]);
					Card card = new Card(suit, rank);
					player2Cards.add(card);
					i += 2;
				}
				state.setPlayerCards(1, player2Cards);
				state.clearDesk();
				state.setDesk();
			}

			if (process == 2) {
				state.getPlayers().get(0).setChoose(Choose.valueOf(tokens[22]));
			}

			if (process == 3) {
				state.getPlayers().get(0).setChoose(Choose.valueOf(tokens[22]));
				state.getPlayers().get(1).setChoose(Choose.valueOf(tokens[23]));
			}

			if (process == 4) {
				state.getPlayers().get(0).setChoose(Choose.valueOf(tokens[22]));
				state.getPlayers().get(1).setChoose(Choose.valueOf(tokens[23]));
				state.getPlayers().get(0).setDecision(Decision.valueOf(tokens[24]));
				state.getPlayers().get(0).setResult(GameOver.Result.valueOf(tokens[25]));
			}

			if (process == 5) {
				state.getPlayers().get(0).setChoose(Choose.valueOf(tokens[22]));
				state.getPlayers().get(1).setChoose(Choose.valueOf(tokens[23]));
				state.getPlayers().get(0).setDecision(Decision.valueOf(tokens[24]));
				state.getPlayers().get(0).setResult(GameOver.Result.valueOf(tokens[25]));
				state.getPlayers().get(1).setDecision(Decision.valueOf(tokens[26]));
				state.getPlayers().get(1).setResult(GameOver.Result.valueOf(tokens[27]));
			}

			return state;
		} catch (StringIndexOutOfBoundsException e) {
			return new State();
		} catch (ArrayIndexOutOfBoundsException e) {
			return new State();
		}
	}
}
