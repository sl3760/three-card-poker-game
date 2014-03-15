package com.poker.shared;

public class StateChanger {
	//constructor
	public StateChanger(){
		
	}
	
	//set player's choice with chip
    public void setChoice(State state, int turn, PlayerMove move, int chipPairsPlus, int chipAnte){
    	if(turn==0)
    	   state.setProcess(2);
    	else
    	   state.setProcess(3);
        switch(move.choose){ 
        case PairsPlus:
        	state.getPlayers().get(turn).setChipPairsPlus(chipPairsPlus);
        	state.getPlayers().get(turn).setChoose(move.choose);
   		    break;
   	    case Ante:
   	    	state.getPlayers().get(turn).setChipAnte(chipAnte);
   	    	state.getPlayers().get(turn).setChoose(move.choose);
   		    break;
   	    case PairsPlusAnte:
   	    	state.getPlayers().get(turn).setChipPairsPlus(chipPairsPlus);
   	    	state.getPlayers().get(turn).setChipAnte(chipAnte);
   	    	state.getPlayers().get(turn).setChoose(move.choose);
   		    break;
          }
       }
       
     //get the result if the player make a decision
	 public GameOver makeMove(State state, int turn, PlayerMove move){
		    if(turn==0)
		         state.setProcess(4);
		    else
		    	 state.setProcess(5);
	    	if(move.decision==PlayerMove.Decision.Play){
	    		state.getPlayers().get(turn).setChipPlay(state.getPlayers().get(turn).getChipAnte());
	    		state.getPlayers().get(turn).setDecision(move.decision);
	    		return state.getResult(turn,move.decision);
	    	}    	
	    	else
	    		state.getPlayers().get(turn).setDecision(move.decision);
	    		return state.getResult(turn,move.decision);
	    }    
}
