package com.poker.client;


import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Date;

import com.poker.shared.Suit;
import com.poker.shared.Rank;
import com.poker.shared.Card;
import com.poker.shared.GameOver;
import com.poker.shared.Player;
import com.poker.shared.PlayerMove;
import com.poker.shared.State;
import com.poker.shared.StateChanger;
import com.poker.shared.PlayerMove.Choose;
import com.poker.shared.PlayerMove.Decision;
import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.appengine.channel.client.ChannelError;
import com.google.gwt.appengine.channel.client.ChannelFactoryImpl;
import com.google.gwt.appengine.channel.client.Socket;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.HasRpcToken;
import com.google.gwt.user.client.rpc.XsrfToken;

public class Presenter {
	public interface Dropper {
		public abstract void onDrop(Image image);
	}

	public enum ButtonKind {
		PairsPlus, Ante, PairsPlusAnte, Deal, Play, Fold;
	}

	public interface View {
		void setPresenter(Presenter p);

		void setBackground(ArrayList<String> playerIds);

		void setCards(List<Card> list, List<ArrayList<Card>> list2);

		void showDealerCards(List<Card> list);

		void showStatus(String dealerStatus, ArrayList<String> playersStatus);

		void showGameInfo(int info);

		void showGameTurn(int turn, boolean b);

		void showGameWarning(String warning);

		void showBalance1(int balance1);

		void showBalance2(int balacen2);

		void clear();

		void playChooseSound();

		void playDecisionSound();

		void playWarningSound();

		void showPlayerCards(int i, List<Card> playerCards);

		void setAnimationCards(List<Card> list, List<ArrayList<Card>> list2);

		void initializeDragging(DragHandler dragHandler);
		
		void initializeDropping();
		
		boolean isDragAndDropSupported();
		
		void setDealBtn(boolean b);
		
		void setPlayBtn(boolean b);
		
		void setFoldBtn(boolean b);
		
		void setRestartBtn(boolean b);
	
		void setStartBtn(boolean b);
		
		void setSaveBtn(boolean b);
		
		void setLoadBtn(boolean b);
		
		void setCancelBtn(boolean b);
		
		void setNotDraggable();
		
		void showConnect(String s);
		
		void showEmail(String s);
		
		void showTurn(String s);
		
		void showNickName(String s);
		
		void showOtherEmail(String s);
		
		void showOtherNickName(String s);
		
		String getEmailInput();
		
		void setSignOutLink(String Url);
		
		void setMatchList(String match);
		
		void clearMatchList();
		
		String getSelectionFromMatchList();
		
		void showRank(String r);
		
		void showComputerChoose(String text);
		
		void showComputerChip(String text); 
		
		void showComputerDecision(String text);
	}

	private State state;
	private StateChanger stateChanger = new StateChanger();
	private View graphics;
	private GameOver gameResult1 = null;
	private GameOver gameResult2 = null;
	private int process;
	Storage local = Storage.getLocalStorageIfSupported();
	String userId="";
	String otherId="";
	String NickName="";
	String OtherNickName="";
	AsyncCallback<String> updateCallback;
	AsyncCallback<Void> updateVoidCallback;
	PokerServiceAsync pokerService;
	Socket socket;
	boolean start=false;
	private List<String> matchList = new LinkedList<String>();
	int meID=-1;
    Long currentMatchId;
    String currentOtherId;
    boolean drag=false;
    int rank;
    int RD;
    boolean AI=false;
    MyPokerAI myPokerAI=new MyPokerAI();
	public Presenter(){
			updateCallback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
	                  Window.alert(caught.getMessage());
			}	
			@Override
			public void onSuccess(String result) {
			}
	
			};	
			
			updateVoidCallback = new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
		                  Window.alert(caught.getMessage());
				}	
				@Override
				public void onSuccess(Void result) {
				}
		
				};
	 }
	 
	 public void setId(String id){
		this.userId=id;
	 }
	 
	 public void setOtherId(String otherId){
		 this.otherId=otherId;
	 }
	 
	 public void setNickName(String nickname){
		 this.NickName=nickname;
	 }
	 
	 public void setOtherNickName(String nickname){
		 this.OtherNickName=nickname;
	 }
	
	public State getState(){
		return this.state;
	}
	
	public interface Factory {
		Presenter create(View view);
	}

	public void setView(View view) {
		this.graphics = view;
		graphics.setPresenter(this);
	}
	/**
	 * players can cancel the chosen chips and change to other mount 
	 */
	public void cancel(){
		ArrayList<Player> players = state.getPlayers();
		ArrayList<String> playerIds = new ArrayList<String>();
		for (int i = 0; i < players.size(); i++) {
			playerIds.add(String.valueOf(players.get(i).getID()));
		}
		graphics.setBackground(playerIds);
		if(state.getPlayerTurn()==1){
			graphics.setSaveBtn(true);
			graphics.setLoadBtn(true);
		}
		//History.newItem(serializeState(state));
	}
	public void setDrag(boolean b){
		drag=b;
	}
   /**
    * initialize state
    * @param graphics
    */
	public void initState(View graphics) {
		this.graphics = graphics;
		this.state = new State();
		this.state.initialize();
		this.state.setProcess(0);
		ArrayList<Player> players = state.getPlayers();
		ArrayList<String> playerIds = new ArrayList<String>();
		for (int i = 0; i < players.size(); i++) {
			playerIds.add(String.valueOf(players.get(i).getID()));
		}
		graphics.setBackground(playerIds);
		showBalance1();
		showBalance2();
		//setState(this.state);
		//History.newItem(serializeState(state));
        graphics.setCancelBtn(false);
        
	}
    /**
     * set state
     * @param state
     */
	public void setState(State state) {
		this.state = state;
		this.process = this.state.getProcess();
		if (state.getGameOver()) {
			String result;
			if (state.getPlayers().get(0).getBalance() <= 0)
				result = Graphics.messages.setResult1();
			else
				result = Graphics.messages.setResult2();
			graphics.showGameWarning(result);
			state.setResult(result);
		} else {			
			graphics.showGameTurn(state.getPlayerTurn(),AI);
			graphics.showGameInfo(this.process);
			graphics.showGameWarning("");
			graphics.clear();
			ArrayList<Player> players = state.getPlayers();
			ArrayList<String> playerIds = new ArrayList<String>();
			for (int i = 0; i < players.size(); i++) 
				playerIds.add(String.valueOf(players.get(i).getID()));
			graphics.setBackground(playerIds);
			if (this.process==0)
				graphics.setStartBtn(true);	
		    if(drag){
		    	graphics.setNotDraggable();
		    	drag=false;
		    }
		    	
			if(this.process>0&&this.process<3&&(meID==state.getPlayerTurn())){			
				if (graphics.isDragAndDropSupported()) {
		            this.initializeDragAndDrop();
		            graphics.setCancelBtn(true);
		            drag=true;
		        }
			}
			
			if(this.process>0&&this.process<3&&AI){			
				if (graphics.isDragAndDropSupported()) {
		            this.initializeDragAndDrop();
		            graphics.setCancelBtn(true);
		            drag=true;
		        }
			}
						
			if (this.process == 3){
				graphics.setAnimationCards(state.getDealerCards(),state.getDesk());	
				graphics.setSaveBtn(true);
				graphics.setLoadBtn(true);
				if(meID==state.getPlayerTurn()||AI){
					if (state.getPlayers().get(0).getChoose() == Choose.Ante){
						graphics.setPlayBtn(true);
						graphics.setFoldBtn(true);
					}
					else
						graphics.setDealBtn(true);
					
					graphics.setCancelBtn(false);
				}
				else{
					graphics.setDealBtn(false);
					graphics.setPlayBtn(false);
					graphics.setFoldBtn(false);
				}			
				
			}					
			if (this.process == 4){
				graphics.setCards(state.getDealerCards(), state.getDesk());
				graphics.setSaveBtn(true);
				graphics.setLoadBtn(true);
				
				if(meID==state.getPlayerTurn()||AI){
					
				    if (state.getPlayers().get(1).getChoose() == Choose.Ante){
					    graphics.setPlayBtn(true);
					    graphics.setFoldBtn(true);
				    } 
				    else
					    graphics.setDealBtn(true);
				}
				else{
				
					graphics.setDealBtn(false);
					graphics.setPlayBtn(false);
					graphics.setFoldBtn(false);
				}
				
				graphics.setCancelBtn(false);
			}
			if (this.process == 5) {
				graphics.setSaveBtn(true);
				graphics.setLoadBtn(true);
				graphics.setCards(state.getDealerCards(), state.getDesk());
				graphics.showDealerCards(state.getDealerCards());
				graphics.showPlayerCards(1, state.getPlayers().get(0)
						.getPlayerCards());
				graphics.showPlayerCards(2, state.getPlayers().get(1)
						.getPlayerCards());
				graphics.showStatus(getDealerStatus(), getPlayersStatus());
				graphics.showBalance1(state.getPlayers().get(0).getBalance());
				graphics.showBalance2(state.getPlayers().get(1).getBalance());
				String result = Graphics.messages.setPlayerID1()
						+ setResult(state.getPlayers().get(0).getResult()) + "! " +Graphics.messages.setPlayerID2()
						+ setResult(state.getPlayers().get(1).getResult()) + "!";
				graphics.showGameWarning(result);
				//graphics.showGameWarning(state.getResult());
				graphics.setRestartBtn(true);
				graphics.setCancelBtn(false);
			}
		}

	}
	/**
	 * for translation
	 */
	public String setResult(GameOver.Result result){
		if(result==GameOver.Result.WIN)
			return Graphics.messages.setWin();
		else if(result==GameOver.Result.LOSE)
			return Graphics.messages.setLose();
		else
			return Graphics.messages.setTie();		
	}
	
	public void AIStartGame() {
		this.state.initialize();
		gameResult1 = null;
		gameResult2 = null;
		ArrayList<Player> players = this.state.getPlayers();
		state.setDealerCards(state.getHandCards());
		for (int i = 0; i < players.size(); i++)
			state.setPlayerCards(i, state.getHandCards());
		state.setDesk();
		AI=true;
		setState(this.state);
		graphics.showOtherEmail("");
		graphics.showOtherNickName("");
        graphics.showTurn("");
	}
	
    /**
     * start game function
     */
	public void startGame() {
		AI=false;
		this.state.initialize();
		gameResult1 = null;
		gameResult2 = null;
		ArrayList<Player> players = this.state.getPlayers();
		state.setDealerCards(state.getHandCards());
		for (int i = 0; i < players.size(); i++)
			state.setPlayerCards(i, state.getHandCards());
		state.setDesk();			
		//setState(this.state);
		//History.newItem(serializeState(state));
		graphics.showComputerChip("");
		graphics.showComputerChoose("");
		graphics.showComputerDecision("");
		graphics.showGameWarning("");
		pokerService.autoMatch(userId, NickName, serializeState(state),updateCallback);

	}
    /**
     * restart game function
     */
	public void reStartGame() {
		this.state.initialize();
		gameResult1 = null;
		gameResult2 = null;
		ArrayList<Player> players = state.getPlayers();
		ArrayList<String> playerIds = new ArrayList<String>();
		for (int i = 0; i < players.size(); i++) {
			playerIds.add(String.valueOf(players.get(i).getID()));
		}
		graphics.setBackground(playerIds);
		state.setDealerCards(state.getHandCards());
        for(int i=0;i<players.size();i++)
                state.setPlayerCards(i, state.getHandCards());
        state.setDesk();
		graphics.showGameTurn(state.getPlayerTurn(),AI);
		graphics.showGameInfo(state.getProcess());
		graphics.showGameWarning("");
		//History.newItem(serializeState(state));
		//setState(this.state);
		if(AI)
			setState(state);
		else
		    pokerService.AMove(serializeState(this.state), currentMatchId, userId, otherId, updateCallback);
		graphics.showComputerChoose("");
		graphics.showComputerChip("");
		graphics.showComputerDecision("");
		graphics.setLoadBtn(true);
	}
    /**
     * show player1 balance
     */
	public void showBalance1() {
		graphics.showBalance1(state.getPlayers().get(0).getBalance());
	}
    /**
     * show player2 balance
     */
	public void showBalance2() {
		graphics.showBalance2(state.getPlayers().get(1).getBalance());
	}
    /**
     * choose function for players to choose parisplus, ante and both pairsplus and ante.
     * @param choose
     * @param chipPairsPlus
     * @param chipAnte
     */
	public void doChoose(Choose choose, int chipPairsPlus, int chipAnte) {
		switch (choose) {
		case PairsPlus:			
				stateChanger.setChoice(state, state.getPlayerTurn(),
						new PlayerMove(Choose.PairsPlus), chipPairsPlus, chipAnte);
				if (state.getPlayerTurn() == 0) {
					state.setNextPlayerTurn();
					graphics.showGameTurn(state.getPlayerTurn(),AI);
					//setState(this.state);
					//History.newItem(serializeState(state));					
					graphics.setNotDraggable();
					drag=false;
					if(!AI)
                        pokerService.AMove(serializeState(this.state), currentMatchId,userId, otherId, updateCallback);
					
				}

				else {					
					state.setNextPlayerTurn();
					graphics.showGameTurn(state.getPlayerTurn(),AI);
					graphics.showGameInfo(state.getProcess());
					graphics.playChooseSound();
					//setState(this.state);
					//History.newItem(serializeState(state));			
				    pokerService.AMove(serializeState(this.state), currentMatchId,userId, otherId, updateCallback);
						 
					if (state.getPlayers().get(0).getChoose() == Choose.Ante){
						graphics.setPlayBtn(true);
						graphics.setFoldBtn(true);
					}
					else
						graphics.setDealBtn(true);					    
				}

			break;
		case Ante:
				stateChanger.setChoice(state, state.getPlayerTurn(),new PlayerMove(Choose.Ante), chipPairsPlus, chipAnte);
				if (state.getPlayerTurn() == 0) {
					state.setNextPlayerTurn();
					graphics.showGameInfo(state.getPlayerTurn());
					//setState(this.state);
					//History.newItem(serializeState(state));
					graphics.setNotDraggable();
					drag=false;
					if(!AI)
                       pokerService.AMove(serializeState(this.state), currentMatchId,userId, otherId, updateCallback);
				
				}

				else {
					state.setNextPlayerTurn();
					graphics.showGameInfo(state.getPlayerTurn());
					graphics.showGameInfo(state.getProcess());
					graphics.playChooseSound();
					//setState(this.state);
					//History.newItem(serializeState(state));
					pokerService.AMove(serializeState(this.state), currentMatchId,userId, otherId, updateCallback);
					if (state.getPlayers().get(0).getChoose() == Choose.Ante){
						graphics.setPlayBtn(true);
						graphics.setFoldBtn(true);
					}
					else
						graphics.setDealBtn(true);
				}

			break;
		case PairsPlusAnte:
				stateChanger.setChoice(state, state.getPlayerTurn(),new PlayerMove(Choose.PairsPlusAnte), chipPairsPlus, chipAnte);
				if (state.getPlayerTurn() == 0) {
					state.setNextPlayerTurn();
					graphics.showGameInfo(state.getPlayerTurn());
					//setState(this.state);
					//History.newItem(serializeState(state));
					graphics.setNotDraggable();
					drag=false;
					if(!AI)
					   pokerService.AMove(serializeState(this.state), currentMatchId,userId, otherId, updateCallback);
					
				}

				else {
					state.setNextPlayerTurn();
					graphics.showGameInfo(state.getPlayerTurn());
					graphics.showGameInfo(state.getProcess());
					graphics.playChooseSound();
					//setState(this.state);
					//History.newItem(serializeState(state));
					pokerService.AMove(serializeState(this.state), currentMatchId,userId, otherId, updateCallback);
					if (state.getPlayers().get(0).getChoose() == Choose.Ante){
						graphics.setPlayBtn(true);
						graphics.setFoldBtn(true);
					}
					else
						graphics.setDealBtn(true);
				}				
			break;
		default:
			break;
		}
		if(AI){						
			String s=myPokerAI.getChoose();
			if(s=="Ante"){
				chipAnte=myPokerAI.getChip();
				chipPairsPlus=0;
				stateChanger.setChoice(state, state.getPlayerTurn(),
						new PlayerMove(Choose.Ante), chipPairsPlus, chipAnte);
			}
			else if(s=="PairsPlus"){
				chipPairsPlus=myPokerAI.getChip();
				chipAnte=0;
				stateChanger.setChoice(state, state.getPlayerTurn(),
						new PlayerMove(Choose.PairsPlus), chipPairsPlus, chipAnte);
			}
			else if(s=="PairsPlusAnte"){
				chipPairsPlus=myPokerAI.getChip();
				chipAnte=myPokerAI.getChip();
				stateChanger.setChoice(state, state.getPlayerTurn(),
						new PlayerMove(Choose.PairsPlus), chipPairsPlus, chipAnte);
			}
			state.setNextPlayerTurn();
			graphics.showGameInfo(state.getProcess());
			graphics.playChooseSound();
			if (state.getPlayers().get(0).getChoose() == Choose.Ante){
				graphics.setPlayBtn(true);
				graphics.setFoldBtn(true);
			}
			else
				graphics.setDealBtn(true);
			setState(state);
			if(drag){
		    	graphics.setNotDraggable();
		    	drag=false;
		    }
			graphics.showComputerChoose("Computer choose: "+s);
			graphics.showComputerChip("Computer PairsPlus chip: "+chipPairsPlus+ "   Computer Ante chip: "+chipAnte);
		}
	}
    /**
     * move function for players to choose deal, play and fold
     * @param decision
     */
	public void doMove(Decision decision) {
		switch (decision) {
		case Deal:
			if (state.getPlayerTurn() == 0) {
				if (state.getPlayers().get(0).getChoose() == Choose.PairsPlus) {
					gameResult1 = stateChanger.makeMove(state,
							state.getPlayerTurn(),
							new PlayerMove(Decision.Deal));
					state.getPlayers().get(0).setResult(gameResult1.getResult());
					state.setPlayerBalance(0, gameResult1.getBalance());
					state.setNextPlayerTurn();
					graphics.showGameInfo(state.getPlayerTurn());
					//setState(this.state);
					//History.newItem(serializeState(state));
					if(!AI)
					     pokerService.AMove(serializeState(this.state), currentMatchId,userId, otherId, updateCallback);
					if (state.getPlayers().get(1).getChoose() == Choose.Ante){
						graphics.setPlayBtn(true);
						graphics.setFoldBtn(true);
						graphics.setDealBtn(false);
					}
					else{
						graphics.setDealBtn(true);
						graphics.setPlayBtn(false);
						graphics.setFoldBtn(false);
					}
					
					//if player play with computer
					if(AI){
						if (state.getPlayers().get(1).getChoose() == Choose.PairsPlus) {
							gameResult2 = stateChanger.makeMove(state,
									state.getPlayerTurn(),
									new PlayerMove(Decision.Deal));
							state.getPlayers().get(1).setResult(gameResult2.getResult());
							state.setPlayerBalance(1, gameResult2.getBalance());
							graphics.playDecisionSound();
							setState(state);
							graphics.setRestartBtn(true);
					    }
						else if (state.getPlayers().get(1).getChoose() == Choose.Ante){
							String decison=myPokerAI.getDecision(state);
							if(decison=="Play"){
								gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Play));
								state.getPlayers().get(1).setResult(gameResult2.getResult());
								state.setPlayerBalance(1, gameResult2.getBalance());
								graphics.playDecisionSound();
						        setState(state);
						        graphics.showComputerDecision("Computer decide: Play");
								graphics.setRestartBtn(true);
							}
							else if(decison=="Flod"){
								gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Fold));
								state.getPlayers().get(1).setResult(gameResult2.getResult());
								state.setPlayerBalance(1, gameResult2.getBalance());
								graphics.playDecisionSound();
								setState(state);
								graphics.showComputerDecision("Computer decide: Fold");
								graphics.setRestartBtn(true);
							}
						}
						else{
							   gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Deal));
							   state.setPlayerBalance(1, gameResult2.getBalance());
							   String decison=myPokerAI.getDecision(state);
								if(decison=="Play"){
									gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Play));
									state.getPlayers().get(1).setResult(gameResult2.getResult());
									state.setPlayerBalance(1, gameResult2.getBalance());
									graphics.playDecisionSound();
							        setState(state);
									graphics.setRestartBtn(true);
								}
								else if(decison=="Flod"){
									gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Fold));
									state.getPlayers().get(1).setResult(gameResult2.getResult());
									state.setPlayerBalance(1, gameResult2.getBalance());
									graphics.playDecisionSound();
									setState(state);
									graphics.setRestartBtn(true);
								}
						}
					}	
						
				} 
				else {
					gameResult1 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Deal));
					state.setPlayerBalance(0, gameResult1.getBalance());					
                    graphics.setPlayBtn(true);
                    graphics.setFoldBtn(true);
                    graphics.setDealBtn(false);
			    }
				
		   } else {
				if (state.getPlayers().get(1).getChoose() == Choose.PairsPlus) {
					gameResult2 = stateChanger.makeMove(state,
							state.getPlayerTurn(),
							new PlayerMove(Decision.Deal));
					state.getPlayers().get(1).setResult(gameResult2.getResult());
					state.setPlayerBalance(1, gameResult2.getBalance());
					graphics.playDecisionSound();
					//setState(this.state);
					//History.newItem(serializeState(state));					
					
					pokerService.EndMove(serializeState(this.state), currentMatchId,userId, otherId, meID,updateCallback);
					graphics.setRestartBtn(true);
                    
				} else {
					gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Deal));
					state.setPlayerBalance(1, gameResult2.getBalance());
					graphics.setPlayBtn(true);
                    graphics.setFoldBtn(true);
                    graphics.setDealBtn(false);
				}
			}
		    
			break;
		case Play:
			if (state.getPlayerTurn() == 0) {
				if (state.getPlayers().get(0).getChoose() == Choose.Ante) {
					gameResult1 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Play));
					state.getPlayers().get(0).setResult(gameResult1.getResult());
					state.setPlayerBalance(0, gameResult1.getBalance());
					state.setNextPlayerTurn();
					graphics.showGameInfo(state.getPlayerTurn());
					//setState(this.state);
					//History.newItem(serializeState(state));
					if(!AI)
					     pokerService.AMove(serializeState(this.state), currentMatchId,userId, otherId, updateCallback);
					if (state.getPlayers().get(1).getChoose() == Choose.Ante){
						graphics.setPlayBtn(true);
						graphics.setFoldBtn(true);
						graphics.setDealBtn(false);
					}
					else{
						graphics.setDealBtn(true);
						graphics.setPlayBtn(false);
						graphics.setFoldBtn(false);
					}
				} else {					
						gameResult1 = stateChanger.makeMove(state, state.getPlayerTurn(),new PlayerMove(Decision.Play));
						state.getPlayers().get(0).setResult(gameResult1.getResult());
						state.setPlayerBalance(0, gameResult1.getBalance());
						state.setNextPlayerTurn();
						graphics.showGameTurn(state.getPlayerTurn(),AI);
						if(!AI)
						     pokerService.AMove(serializeState(this.state), currentMatchId,userId, otherId, updateCallback);
						if (state.getPlayers().get(1).getChoose() == Choose.Ante){
							graphics.setPlayBtn(true);
							graphics.setFoldBtn(true);
							graphics.setDealBtn(false);
						}
						else{
							graphics.setDealBtn(true);
							graphics.setPlayBtn(false);
							graphics.setFoldBtn(false);
						}					
				}

				//if player play with computer
				if(AI){
					if (state.getPlayers().get(1).getChoose() == Choose.PairsPlus) {
						gameResult2 = stateChanger.makeMove(state,
								state.getPlayerTurn(),
								new PlayerMove(Decision.Deal));
						state.getPlayers().get(1).setResult(gameResult2.getResult());
						state.setPlayerBalance(1, gameResult2.getBalance());
						graphics.playDecisionSound();
						setState(state);
						graphics.setRestartBtn(true);
				    }
					else if (state.getPlayers().get(1).getChoose() == Choose.Ante){
						String decison=myPokerAI.getDecision(state);
						if(decison=="Play"){
							gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Play));
							state.getPlayers().get(1).setResult(gameResult2.getResult());
							state.setPlayerBalance(1, gameResult2.getBalance());
							graphics.playDecisionSound();
					        setState(state);
					        graphics.showComputerDecision("Computer decide: Play");
							graphics.setRestartBtn(true);
						}
						else if(decison=="Flod"){
							gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Fold));
							state.getPlayers().get(1).setResult(gameResult2.getResult());
							state.setPlayerBalance(1, gameResult2.getBalance());
							graphics.playDecisionSound();
							setState(state);
							graphics.showComputerDecision("Computer decide: Fold");
							graphics.setRestartBtn(true);
						}
					}
					else{
						   gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Deal));
						   state.setPlayerBalance(1, gameResult2.getBalance());
						   String decison=myPokerAI.getDecision(state);
							if(decison=="Play"){
								gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Play));
								state.getPlayers().get(1).setResult(gameResult2.getResult());
								state.setPlayerBalance(1, gameResult2.getBalance());
								graphics.playDecisionSound();
						        setState(state);
								graphics.setRestartBtn(true);
							}
							else if(decison=="Flod"){
								gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Fold));
								state.getPlayers().get(1).setResult(gameResult2.getResult());
								state.setPlayerBalance(1, gameResult2.getBalance());
								graphics.playDecisionSound();
								setState(state);
								graphics.setRestartBtn(true);
							}
					}
				}	
				
			} else {
				if (state.getPlayers().get(1).getChoose() == Choose.Ante) {
					gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Play));
					state.getPlayers().get(1).setResult(gameResult2.getResult());
					state.setPlayerBalance(1, gameResult2.getBalance());
					graphics.playDecisionSound();
					//setState(this.state);
					//History.newItem(serializeState(state));
					
					pokerService.EndMove(serializeState(this.state), currentMatchId,userId, otherId, meID,updateCallback);
					graphics.setRestartBtn(true);
				} else {
						gameResult2 = stateChanger.makeMove(state, state.getPlayerTurn(),new PlayerMove(Decision.Play));
						state.getPlayers().get(1).setResult(gameResult2.getResult());
						state.setPlayerBalance(1, gameResult2.getBalance());
						graphics.playDecisionSound();
						//setState(this.state);
						//History.newItem(serializeState(state));
						
						pokerService.EndMove(serializeState(this.state), currentMatchId,userId, otherId, meID,updateCallback);
						graphics.setRestartBtn(true);
					}

			}
			break;
		case Fold:
			if (state.getPlayerTurn() == 0) {
				if (state.getPlayers().get(0).getChoose() == Choose.Ante) {
					gameResult1 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Fold));
					state.getPlayers().get(0).setResult(gameResult1.getResult());
					state.setPlayerBalance(0, gameResult1.getBalance());
					state.setNextPlayerTurn();
					graphics.showGameTurn(state.getPlayerTurn(),AI);
					//setState(this.state);
					//History.newItem(serializeState(state));
					if(!AI)
					     pokerService.AMove(serializeState(this.state), currentMatchId,userId, otherId, updateCallback);
					if (state.getPlayers().get(1).getChoose() == Choose.Ante){
						graphics.setPlayBtn(true);
						graphics.setFoldBtn(true);
						graphics.setDealBtn(false);
					}
					else{
						graphics.setDealBtn(true);
						graphics.setPlayBtn(false);
						graphics.setFoldBtn(false);
					}
				} else {
						gameResult1 = stateChanger.makeMove(state, state.getPlayerTurn(),new PlayerMove(Decision.Fold));
						state.getPlayers().get(0).setResult(gameResult1.getResult());
						state.setPlayerBalance(0, gameResult1.getBalance());
						state.setNextPlayerTurn();
						graphics.showGameTurn(state.getPlayerTurn(),AI);
						if(!AI)
						    pokerService.AMove(serializeState(this.state), currentMatchId,userId, otherId, updateCallback);
						if (state.getPlayers().get(1).getChoose() == Choose.Ante){
							graphics.setPlayBtn(true);
							graphics.setFoldBtn(true);
							graphics.setDealBtn(false);
						}
						else{
							graphics.setDealBtn(true);
							graphics.setPlayBtn(false);
							graphics.setFoldBtn(false);
						}
					}

				//if player play with computer
				if(AI){
					if (state.getPlayers().get(1).getChoose() == Choose.PairsPlus) {
						gameResult2 = stateChanger.makeMove(state,
								state.getPlayerTurn(),
								new PlayerMove(Decision.Deal));
						state.getPlayers().get(1).setResult(gameResult2.getResult());
						state.setPlayerBalance(1, gameResult2.getBalance());
						graphics.playDecisionSound();
						setState(state);
						graphics.setRestartBtn(true);
				    }
					else if (state.getPlayers().get(1).getChoose() == Choose.Ante){
						String decison=myPokerAI.getDecision(state);
						if(decison=="Play"){
							gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Play));
							state.getPlayers().get(1).setResult(gameResult2.getResult());
							state.setPlayerBalance(1, gameResult2.getBalance());
							graphics.playDecisionSound();
					        setState(state);
					        graphics.showComputerDecision("Computer decide: Play");
							graphics.setRestartBtn(true);
						}
						else if(decison=="Flod"){
							gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Fold));
							state.getPlayers().get(1).setResult(gameResult2.getResult());
							state.setPlayerBalance(1, gameResult2.getBalance());
							graphics.playDecisionSound();
							setState(state);
							graphics.showComputerDecision("Computer decide: Fold");
							graphics.setRestartBtn(true);
						}
					}
					else{
						   gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Deal));
						   state.setPlayerBalance(1, gameResult2.getBalance());
						   String decison=myPokerAI.getDecision(state);
							if(decison=="Play"){
								gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Play));
								state.getPlayers().get(1).setResult(gameResult2.getResult());
								state.setPlayerBalance(1, gameResult2.getBalance());
								graphics.playDecisionSound();
						        setState(state);
								graphics.setRestartBtn(true);
							}
							else if(decison=="Flod"){
								gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Fold));
								state.getPlayers().get(1).setResult(gameResult2.getResult());
								state.setPlayerBalance(1, gameResult2.getBalance());
								graphics.playDecisionSound();
								setState(state);
								graphics.setRestartBtn(true);
							}
					}
				}	
				
			} else {
				if (state.getPlayers().get(1).getChoose() == Choose.Ante) {
					gameResult2 = stateChanger.makeMove(state,state.getPlayerTurn(),new PlayerMove(Decision.Fold));
					state.getPlayers().get(1).setResult(gameResult2.getResult());
					state.setPlayerBalance(1, gameResult2.getBalance());
					graphics.playDecisionSound();
					//setState(this.state);
					//History.newItem(serializeState(state));
					
					pokerService.EndMove(serializeState(this.state), currentMatchId,userId, otherId, meID,updateCallback);
					graphics.setRestartBtn(true);
				} else {
						gameResult2 = stateChanger.makeMove(state, state.getPlayerTurn(),new PlayerMove(Decision.Fold));
						state.getPlayers().get(1).setResult(gameResult2.getResult());
						state.setPlayerBalance(1, gameResult2.getBalance());
						graphics.playDecisionSound();
						//setState(this.state);
						//History.newItem(serializeState(state));
						
						pokerService.EndMove(serializeState(this.state), currentMatchId,userId, otherId,meID, updateCallback);
						graphics.setRestartBtn(true);
					}
			}
			break;
		default:
			break;
		}

	}
    /**
     * Disable History Support
    
	// Add the ValueChangeHandler responsible for traversing the browser history
	public void initializeHistory() {
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();
				setState(unserializeState(historyToken));
			}
		});
		
		String startState = History.getToken();
		setState(unserializeState(startState));	
		
	}
     */
	
	 /**
     * serialize function to store state
     *
     * @param state
     * @return serialized string of state
     */

	public String serializeState(State state) {
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

	 /**
     * deserialize function restore state from string
     *
     * @param string
     * @return restored state
     */

	public State unserializeState(String serialized) {
		try {
			
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
    /**
     * get the dealer's hand type
     * @return String
     */
	private String getDealerStatus() {
		ArrayList<Card> dealerCards = state.getDealerCards();
		if (!state.dealerCardsQualify(dealerCards))
			return Graphics.messages.setDealerNotQualify();
		else
			return Graphics.messages.setDealer()+": " + setHandType(state.getHandType(dealerCards));

	}
    /**
     *  get the two players' hand types
     * @return ArrayList<String>
     */
	private ArrayList<String> getPlayersStatus() {
		ArrayList<Player> players = state.getPlayers();
		ArrayList<String> playersStatus = new ArrayList<String>();

		for (int i = 0; i < players.size(); i++) {
			String status = Graphics.messages.setID() + String.valueOf(players.get(i).getID());
			status = status + " "
					+ setHandType(state.getHandType(players.get(i).getPlayerCards()));
			playersStatus.add(status);
		}
		return playersStatus;
	}
	
	/**
	 *  translate for handtype 
	 */
	public String setHandType(State.HAND hand){
		if(hand==State.HAND.high)
			return Graphics.messages.setHigh();
		else if(hand==State.HAND.pair)
			return Graphics.messages.setPair();
		else if(hand==State.HAND.flush)
			return Graphics.messages.setFlush();
		else if(hand==State.HAND.straight)
			return Graphics.messages.setStraight();
		else if(hand==State.HAND.threeofakind)
			return Graphics.messages.setThreeOfKind();
		else
			return Graphics.messages.setStraightFlush();
	}
	
    /**
     * initialize drag and drop function
     */
	private void initializeDragAndDrop() {
		graphics.initializeDragging(new DragHandlerAdapter() {
			@Override
			public void onDragStart(DragStartEvent event) {
			}
		});

		graphics.initializeDropping();
	}
	/**
	 * save the same
	 * @param key
	 */	
	public void saveGame(String key){		
        if (local != null) {
        	    String value=this.serializeState(state);
                local.setItem(key, value);
        }
	}
	/**
	 * load this game
	 * @param key
	 */
	public void loadGame(String key){
		String saveString=local.getItem(key);
         if(local!=null)
        	 setState(unserializeState(saveString));
	}
	
	/**
	 * open this game when logging in successfully
	 * @param token
	 */
	public void openGame(String token,XsrfToken xsrfToken){
		pokerService = GWT.create(PokerService.class);
		((HasRpcToken) pokerService).setRpcToken(xsrfToken);
		socket = new ChannelFactoryImpl().createChannel(token).open(
                new SocketListener() {
                        @Override
                        public void onOpen() {
                        	graphics.showConnect(Graphics.messages.setConnecting());
                            requestMatchList();
                            rank();
                        }
                        @Override
                        public void onMessage(String message) {
                        	parseMessage(message);                      	 
                        }
                        @Override
                        public void onError(ChannelError error) {
                        }
                        @Override
                        public void onClose() {
                        }
                });		
               graphics.showEmail(userId);
               graphics.showNickName(NickName);

	}
	/**
	 *  show the player's rank
	 */
	public void rank(){
		   pokerService.getRank(userId, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
	                  Window.alert(caught.getMessage());
			}	
			@Override
			public void onSuccess(String result) {
				 String[] msgs=result.split(",");				
	       		 rank=Integer.valueOf(msgs[1]);
	       		 RD=Integer.valueOf(msgs[2]);    		 
	       		 graphics.showRank(Graphics.messages.setRank()+" [" + (rank - 2 * RD) + ", "+ (rank + 2 * RD) + "]");
                 pokerService.updatePlayerInfo(userId,NickName,updateVoidCallback);
			}	
			});
        
	}
	/**
	 *  analysis messages from socket
	 */
     public void parseMessage(String message){
    	 
    	 String[] msgs=message.split(",");
    	 if((!start)&&msgs[0].equals("A")){
       		 start=true;          	 
           	 int len=msgs.length;
           	 currentOtherId=msgs[len-3];
           	 setOtherId(currentOtherId);
           	 setOtherNickName(msgs[len-2]);
             if(msgs[len-1].equals("0"))
             	 meID=0;
             else
             	 meID=1;
             graphics.showConnect(Graphics.messages.setConnected());
             graphics.showOtherEmail(otherId);
             //graphics.showOtherNickName(OtherNickName);
             graphics.showTurn(Graphics.messages.setYouPlayer()+String.valueOf(meID+1));
             graphics.showGameTurn(state.getPlayerTurn(),AI);
             graphics.showGameInfo(state.getProcess());
             setState(state);
       	     }
       	 else if(msgs[0].equals("N")){
       		 currentMatchId=new Long(msgs[1]);
       		 setOtherId(currentOtherId);
      		 requestMatchList();      		
       	 }
       	 else if(msgs[0].equals("C")){      		 
       		 requestMatchList();
       		 setState(unserializeState(msgs[1]));      		
       	 }
       	 else if(msgs[0].equals("R")){       	
       		 rank=Integer.valueOf(msgs[1]);       		 
       		 RD=Integer.valueOf(msgs[2]);   		 
       		 graphics.showRank(Graphics.messages.setRank()+" [" + (rank - 2 * RD) + ", "+ (rank + 2 * RD) + "]");
       	 }
     }
     /**
      * update match list
      */
     public void requestMatchList(){
    	 AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
             public void onFailure(Throwable caught) {
                     
             }
             public void onSuccess(List<String> result) {                    
            	   matchList = result;
                   updateMatchList(processDate(result));
             }
          };
         pokerService.requestMatchList(userId, callback);

     }
     /**
      *  process date to list info on the matchlist
      * @param matchList
      * @return List<String>
      */
     @SuppressWarnings("deprecation")
     public List<String> processDate(List<String> matchList) {
             List<String> processedMatchList = new LinkedList<String>();
             for (String match : matchList) {
                     String[] matchSplit = match.split("##");                     
                     String opponent = matchSplit[1].equals(userId) ? matchSplit[2]: matchSplit[1];
                     String dateString=matchSplit[4];
                     Date today = new Date(dateString);                
                     String formatedDate = DateTimeFormat.getMediumDateTimeFormat().format(today).toString();
                     if(opponent==matchSplit[1])
                    	 meID=0;
                     else
                    	 meID=1;
                     String processedMatch = Graphics.messages.setMatchID() + matchSplit[0]+" "+Graphics.messages.setYourID()+(meID+1)
                                     +" "+ Graphics.messages.setOpponent() + opponent +" "+ Graphics.messages.setTurn() + matchSplit[3]
                                     +" "+Graphics.messages.setDate()+formatedDate;

                     processedMatchList.add(processedMatch);
             }
             return processedMatchList;
     }

	/**
	 * Start a new match by inputing opponent's email
	 */
	public void newMatch(){
		if (graphics.getEmailInput() != null) {
			if(graphics.getEmailInput()==userId)
				Window.alert(Graphics.messages.setPlayYouself());
			else{				
				currentOtherId=graphics.getEmailInput();
				this.state.initialize();
				state.getPlayers().get(0).setBalance(1000);
				state.getPlayers().get(1).setBalance(1000);
				gameResult1 = null;
				gameResult2 = null;
				ArrayList<Player> players = this.state.getPlayers();
				state.setDealerCards(state.getHandCards());
				for (int i = 0; i < players.size(); i++)
					state.setPlayerCards(i, state.getHandCards());
				state.setDesk();	
				String st=serializeState(state);
                pokerService.sendNewMatch(graphics.getEmailInput(), userId, st, new AsyncCallback<Void>() {
    			@Override
    			public void onFailure(Throwable caught) {
    				Window.alert(caught.getMessage());
    	        }
    	
    			@Override
    				public void onSuccess(Void result) {
    			}
    	
    			});
			}
        }
	}	
	/**
	 * delete match from selected match
	 */
	public void deleteMatch(){
		String match = graphics.getSelectionFromMatchList();
        String id = match.split(" ")[2];
        graphics.clear();
        start=false;
        this.state.initialize();
		graphics.showGameTurn(state.getPlayerTurn(),AI);
		graphics.showGameInfo(state.getProcess());
		graphics.showGameWarning("");
        setState(state);
        // delete selected match from current player's list
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
            }

            public void onSuccess(Void result) {
                    requestMatchList();
                    
            }
        };
       pokerService.deleteMatchFromPlayer(userId, new Long(id), callback);
	}
	/**
	 * set sign out link
	 * @param Url
	 */
	void setSignOutUrl(String Url){
		graphics.setSignOutLink(Url);
	}
	
	/**
	 * update match list
	 * @param matchList
	 */
	public void updateMatchList(List<String> matchList) {
        if (matchList != null) {
                graphics.clearMatchList();
                graphics.setMatchList(Graphics.messages.setSelectMatch());
                for (String s : matchList) {
                        graphics.setMatchList(s);
                }
        }
     }
	/**
	 * list all the matches on the matchlist
	 */
	public void getMatchList() {         
		     AI=false;
		     graphics.showComputerChip("");
		     graphics.showComputerChoose("");
			 graphics.showComputerDecision("");
             String match = graphics.getSelectionFromMatchList();
             if (!match.equals(Graphics.messages.setSelectMatch())) {
                     final String id = match.split(" ")[2];
                    
                     // send id to server
                     AsyncCallback<String> callback = new AsyncCallback<String>() {
                         public void onFailure(Throwable caught) {
                                Window.alert(caught.getMessage());
                         }
                         public void onSuccess(String result) {                        
                                 if (result != null) {   
                                	     String[] resultSplit = result.split("##");
                                         State state = unserializeState(resultSplit[0]);
                                         setState(state);
                                         if (resultSplit[1].equals(userId)) 
                                             setOtherId(resultSplit[2]);
                                         else 
                                        	 setOtherId(resultSplit[1]);                            
                                         currentMatchId=new Long(id);
                                         graphics.showOtherEmail(otherId); 
                                         //graphics.showOtherNickName("  ");
                                         graphics.showTurn(Graphics.messages.setYouPlayer()+(meID+1));
                                         graphics.showGameTurn(state.getPlayerTurn(),AI);
                                         graphics.showGameInfo(state.getProcess());
                                         graphics.showBalance1(state.getPlayers().get(0).getBalance());
                         				 graphics.showBalance2(state.getPlayers().get(1).getBalance());
                                 }
                         }
                     };
                     pokerService.getStateWithMatchId(new Long(id), callback);
             }
	}

}