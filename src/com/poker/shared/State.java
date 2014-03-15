package com.poker.shared;
import java.util.*;
public class State {
	private static final int MAX_PLAYERNUM = 2;
    private ArrayList<Player> players=new ArrayList<Player>();
	private Player player1;
	private Player player2;	
	private int playerTurn;
	private int process;
	private Deck deck;
	private static GameOver g;
	private ArrayList<Card> dealerCards;
    public static enum HAND {high,pair,flush,straight,threeofakind,straightflush};
    private HAND playerHand;
    private HAND dealerHand;
    private ArrayList<ArrayList<Card>> desk; // the i-th List means the i-th player's cards
    String result;
    private int chipProcess;
   
    //constructor
	public State()
    { 
		player1=new Player(1);
		player2=new Player(2);
		this.players.add(this.player1);
		this.players.add(this.player2);
		this.deck=new Deck();
		this.desk = new ArrayList<ArrayList<Card>>();

    }
	


    //initialize
    public void initialize()
    {
    	this.deck.restoreDeck();
    	this.desk = new ArrayList<ArrayList<Card>>();
    	IntGenerator generator = new IntGenerator() {
    	      @Override
    	      public int getInt(int from, int to) throws IllegalArgumentException {
    	        Random random = new Random();
    	        int s = random.nextInt(to) % (to - from + 1) + from;
    	        return s;
    	      }
        };
    	this.deck.shuffle(generator);	   
      	dealerCards=null;
    	for(int i=0;i<MAX_PLAYERNUM;i++){
    		 this.players.get(i).setPlayerCards(null);
    		 this.players.get(i).setWagerPairsPlus(0);
    		 this.players.get(i).setWagerAnte(0);
    		 this.players.get(i).setWagerPlay(0);
    	}   
    	this.playerTurn=0;
    	this.process=1;
    	this.chipProcess=0;
    }
    
    
    public String getResult(){
    	return this.result;
    }
    
    public void setResult(String result){
       this.result=result;	
    }
    
    public int getProcess(){
    	return this.process;
    }
    
    public void setProcess(int i){
        this.process=i;	
    }
    
    public int getChipProcess(){
    	return this.chipProcess;
    }
    
    public void setChipProcess(int i){
        this.chipProcess=i;	
    }
    
    //get player's turn
    public int getPlayerTurn(){
    	return this.playerTurn;
    }
    
    public void setPlayerTurn(int turn){
    	this.playerTurn=turn;
    }
    //set next turn
    public void setNextPlayerTurn(){
       this.playerTurn=Math.abs(playerTurn-1);
    }
    //get deck
    public Deck getDeck(){
    	return this.deck;
    }
    //set desk
    public void setDesk(){
    	for(int i=0;i<MAX_PLAYERNUM;i++){
    		this.desk.add(players.get(i).getPlayerCards());
    	}
    }
    //get desk
    public ArrayList<ArrayList<Card>> getDesk(){
    	return this.desk;
    }  
    //clear desk
    public void clearDesk(){
    	this.desk.clear();
    }
    //get all players
    public ArrayList<Player> getPlayers(){
    	return this.players;
    }
    //set player cards in this turn
    public void setPlayerCards(int turn,ArrayList<Card> cards)
    {        
    	this.players.get(turn).setPlayerCards(cards);
    }
    //set dealer cards
    public void setDealerCards(ArrayList<Card> cards)
    {
          this.dealerCards=cards;
    }
    //get player cards in this turn 
    public ArrayList<Card> getPlayerCards(int turn)
    {
         return this.players.get(turn).getPlayerCards();
    }
    //get dealer cards
    public ArrayList<Card> getDealerCards()
    {
         return dealerCards;
    }
    //get hand cards
    public ArrayList<Card> getHandCards()
    {
    	ArrayList<Card> handCards=new ArrayList<Card>();
    	for(int i=0;i<3;i++)
    		handCards.add(deck.dealCard());
    	return handCards;
    }
    //hand out cards to dealer or player
    public void giveCards(){
    	for(int i=0;i<MAX_PLAYERNUM;i++)
    	    this.players.get(i).setPlayerCards(getHandCards());
    	dealerCards=getHandCards();
    }
    
    
    //get player or dealer hand type
    public HAND getHandType(ArrayList<Card> handCards){
    	Collections.sort(handCards,Card.NORMAL_COMPARATOR);
    	if(handCards.get(1).getRank().compareTo(handCards.get(0).getRank())==1&&handCards.get(2).getRank().compareTo(handCards.get(1).getRank())==1&&handCards.get(0).getSuit().compareTo(handCards.get(1).getSuit())==0&&handCards.get(1).getSuit().compareTo(handCards.get(2).getSuit())==0)
    		return HAND.straightflush;
    	else if(handCards.get(0).getRank().compareTo(handCards.get(2).getRank())==0)
    		return HAND.threeofakind;
    	else if(handCards.get(1).getRank().compareTo(handCards.get(0).getRank())==1&&handCards.get(2).getRank().compareTo(handCards.get(1).getRank())==1)
    		return HAND.straight;
    	else if(handCards.get(0).getSuit().compareTo(handCards.get(1).getSuit())==0&&handCards.get(1).getSuit().compareTo(handCards.get(2).getSuit())==0)
    		return HAND.flush;
    	else if(handCards.get(0).getRank().compareTo(handCards.get(1).getRank())==0||handCards.get(1).getRank().compareTo(handCards.get(2).getRank())==0)
    		return HAND.pair;
    	else
    		return HAND.high;
    	
    }
    //check if the dealer's cards qualify
    public boolean dealerCardsQualify(ArrayList<Card> cards){
    	dealerHand=getHandType(cards);
    	if(dealerHand.compareTo(HAND.high)==0&&dealerCards.get(2).getRank().compareTo(Rank.QUEEN)<0)
    		return false;
    	else
    		return true;
    }
    // check if the player's cards win if he chooses PairsPlus option
    public boolean playerCardsWinPairsPlus(ArrayList<Card> cards){
    	playerHand=getHandType(cards);
    	if(playerHand.compareTo(HAND.pair)>=0)
    		return true;
    	else
    		return false;
    }
    //compare player's cards with dealer's cards
    public GameOver.Result PlayerCompareDealer(ArrayList<Card> player, ArrayList<Card> dealer){
    	dealerHand=getHandType(dealer);
		Collections.sort(dealer,Card.NORMAL_COMPARATOR);
		playerHand=getHandType(player);
		Collections.sort(player,Card.NORMAL_COMPARATOR);
		//player's hand greater than dealer's hand
		if(playerHand.compareTo(dealerHand)>0)
		{
			return GameOver.Result.WIN;
		}
		//player's hand less than dealer's hand
		else if(playerHand.compareTo(dealerHand)<0)
		{
			return GameOver.Result.LOSE;		
		}
		//compare high card
		else{
			if(player.get(2).getRank().compareTo(dealer.get(2).getRank())>0){
				return GameOver.Result.WIN;
			}
			else if(player.get(2).getRank().compareTo(dealer.get(2).getRank())<0){
				return GameOver.Result.LOSE;
			}
			else{	
				return GameOver.Result.TIE;
			}        				
     	} 	
		
    }
       
    //get the current result in this round
    public GameOver getResult(int turn, PlayerMove.Decision decision){
    	if(decision==PlayerMove.Decision.Deal){
       		
    		if(getHandType(players.get(turn).getPlayerCards())==HAND.pair)
    			 g= new GameOver(players.get(turn).getBalance(),players.get(turn).getWagerPairsPlus(),GameOver.Result.WIN,1);
    		else if(getHandType(players.get(turn).getPlayerCards())==HAND.flush)
    		     g= new GameOver(players.get(turn).getBalance(),players.get(turn).getWagerPairsPlus(),GameOver.Result.WIN,4);
    		else if(getHandType(players.get(turn).getPlayerCards())==HAND.straight)
   				 g= new GameOver(players.get(turn).getBalance(),players.get(turn).getWagerPairsPlus(),GameOver.Result.WIN,6);
    		else if(getHandType(players.get(turn).getPlayerCards())==HAND.threeofakind)
   				 g= new GameOver(players.get(turn).getBalance(),players.get(turn).getWagerPairsPlus(),GameOver.Result.WIN,30);
    		else if(getHandType(players.get(turn).getPlayerCards())==HAND.straightflush)
   				 g= new GameOver(players.get(turn).getBalance(),players.get(turn).getWagerPairsPlus(),GameOver.Result.WIN,40);
    		else
    		{
        		g= new GameOver(players.get(turn).getBalance(),players.get(turn).getWagerPairsPlus(),GameOver.Result.LOSE,1);
    		}
    		
    	}
    	else if(decision==PlayerMove.Decision.Play){
    		//check if the player can get bonus
    		if(getHandType(players.get(turn).getPlayerCards())==HAND.straightflush)
    				players.get(turn).setBalance(players.get(turn).getBalance()+players.get(turn).getWagerAnte()*5);
			if(getHandType(players.get(turn).getPlayerCards())==HAND.threeofakind)
					players.get(turn).setBalance(players.get(turn).getBalance()+players.get(turn).getWagerAnte()*4);
			if(getHandType(players.get(turn).getPlayerCards())==HAND.flush)
					players.get(turn).setBalance(players.get(turn).getBalance()+players.get(turn).getWagerAnte());
			
    		dealerHand=getHandType(dealerCards);
    		Collections.sort(dealerCards,Card.NORMAL_COMPARATOR);
    		playerHand=getHandType(players.get(turn).getPlayerCards());
    		Collections.sort(players.get(turn).getPlayerCards(),Card.NORMAL_COMPARATOR);
    		
    		//test whether the dealer's hand qualifies with a queen high or better
    		if(dealerHand.compareTo(HAND.high)==0&&dealerCards.get(2).getRank().compareTo(Rank.QUEEN)<0){
    				g= new GameOver(players.get(turn).getBalance(),players.get(turn).getWagerAnte(),players.get(turn).getWagerPlay(),GameOver.Result.WIN,1,0);  				
    		}
    		else
    		{ 
    			//player's hand higher than dealer's hand
        		if(playerHand.compareTo(dealerHand)>0)
        		{        
        			g= new GameOver(players.get(turn).getBalance(),players.get(turn).getWagerAnte(),players.get(turn).getWagerPlay(),GameOver.Result.WIN,1,1);
        		}
        		//player's hand less than dealer's hand
        		else if(playerHand.compareTo(dealerHand)<0)
        		{
        				g= new GameOver(players.get(turn).getBalance(),players.get(turn).getWagerAnte(),players.get(turn).getWagerPlay(),GameOver.Result.LOSE,1,1); 			
        		}
        		//compare high card
        		else{
        			if(players.get(turn).getPlayerCards().get(2).getRank().compareTo(dealerCards.get(2).getRank())>0){
        		
            			g= new GameOver(players.get(turn).getBalance(),players.get(turn).getWagerAnte(),players.get(turn).getWagerPlay(),GameOver.Result.WIN,1,1);
        			}
        			else if(players.get(turn).getPlayerCards().get(2).getRank().compareTo(dealerCards.get(2).getRank())<0){
        				
        				g= new GameOver(players.get(turn).getBalance(),players.get(turn).getWagerAnte(),players.get(turn).getWagerPlay(),GameOver.Result.LOSE,1,1);
        			}
        			else{	
        				
            			g= new GameOver(players.get(turn).getBalance(),players.get(turn).getWagerAnte(),players.get(turn).getWagerPlay(),GameOver.Result.TIE,1,1);
        			}        				
             	} 			
    		} 		
    	}
    	else if(decision==PlayerMove.Decision.Fold){
    		players.get(turn).setBalance(players.get(turn).getBalance()-players.get(turn).getWagerPairsPlus());
    		g= new GameOver(players.get(turn).getBalance(),players.get(turn).getWagerAnte(),0,GameOver.Result.LOSE,1,1);
    	}
    	return g;
    }
    //set player balance
    public void setPlayerBalance(int turn ,int balance){
		this.players.get(turn).setBalance(balance);
	}
    //check if the game is over
    public boolean getGameOver(){
    	for(int i=0;i<MAX_PLAYERNUM;i++){
    		if(players.get(i).getBalance()<=0)
    			return true;
    	}
    	return false;
    }
}
