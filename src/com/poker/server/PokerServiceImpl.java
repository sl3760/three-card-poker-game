package com.poker.server;
import java.util.Date;
import java.text.DateFormat;
import java.util.List;

import com.poker.client.*;
import com.poker.shared.*;
import com.poker.client.PokerService;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

import com.googlecode.objectify.ObjectifyService;
import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet; 

public class PokerServiceImpl extends XsrfProtectedServiceServlet implements PokerService{
	  private static final long serialVersionUID = 1L;
	  private String waitUserId="";
	  private String waitNickName="";
	  ChannelService channelService = ChannelServiceFactory.getChannelService();
	  static {         
          // register entity
          ObjectifyService.register(MatchInfo.class);
          ObjectifyService.register(PlayerInfo.class);
          ObjectifyService.register(GamePeriodData.class);
      }
     /**
      * automatch event
      */
	  @Override
	  public String autoMatch(String userId, String NickName, String state){		  
		  if(waitUserId==""){
			  waitUserId=userId;
			  waitNickName=NickName;
		  }
		  else{
			  if(!(waitUserId.equals(userId))){
				  
				  channelService.sendMessage(new ChannelMessage(waitUserId, "A"+","+userId+","+NickName+ ",0"));
                  channelService.sendMessage(new ChannelMessage(userId, "A"+","+waitUserId+","+waitNickName+",1"));
                  createNewMatch(userId, waitUserId, state);
                  waitUserId = "";
                  
			  }
		  }
		  return userId;
	  }	  	 
	  
	  /**
	   * start a game by inputing a email
	   */
	  @Override
      public void sendNewMatch(String p1Email, String p2Email, String state) {		      
              createNewMatch(p1Email, p2Email, state);
      }
	  
	  private Long createNewMatch(String p1Email, String p2Email, String state) {

          Long matchId = System.currentTimeMillis();
          
          DataOperation.newMatchTransaction(matchId, p1Email, p2Email, state, p2Email,new Date());
      
          channelService.sendMessage(new ChannelMessage(p1Email, "N,"+matchId));
          channelService.sendMessage(new ChannelMessage(p2Email, "N,"+matchId));
          return matchId;
     }
	  
	  @Override
      public List<String> requestMatchList(String userid) {
              return DataOperation.getMatchStringList(userid);
      }
      
	  @Override
	  public void deleteMatchFromPlayer(String email, Long matchId) {
              DataOperation.deleteMatchFromPlayer(email, matchId);

      }
	  
	  @Override
	  public String getStateWithMatchId(Long matchId) {
          return DataOperation.getStateAndPlayerAndTurnInfoWithMatchId(matchId);
      }
	  /*
	  @Override
	  public String Move(String state, String id1, String id2){		 
			 channelService.sendMessage(new ChannelMessage(id2,"A,"+state));
			 channelService.sendMessage(new ChannelMessage(id1,"A,"+state));
			 return state;
	  }
	  */
	  
	  /**
	   * send move
	   */
	  @Override
	  public String AMove(String state, Long matchId, String id1, String id2){	
		 
		     String turn= DataOperation.getTurnWithMatchId(matchId);
		     turn = turn.equals(id1) ? id2 : id1;
		     DataOperation.updateMatch(new Long(matchId), state, turn);
		     channelService.sendMessage(new ChannelMessage(id2,"C,"+state));
		     channelService.sendMessage(new ChannelMessage(id1,"C,"+state));		  
		     return state;
	  }
	  
	  /**
	   * send the last move
	   */
	  @Override
	  public String EndMove(String state, Long matchId, String id1, String id2, int meID){	
		 
		     String turn= DataOperation.getTurnWithMatchId(matchId);
		     turn = turn.equals(id1) ? id1 : id2;
		     DataOperation.updateMatch(new Long(matchId), state, turn);
		     channelService.sendMessage(new ChannelMessage(id2,"C,"+state));
		     channelService.sendMessage(new ChannelMessage(id1,"C,"+state));
		     Date currentDate = new Date();
		     String currentDateString = DataOperation.dateToString(currentDate);
		     
		     String myRankAndRD = DataOperation.getRankAndRD(id1);
			 String myRankString = myRankAndRD.split(" ")[0];
			 String myRDString = myRankAndRD.split(" ")[1];
			 int myRank = Integer.valueOf(myRankString);			 
			 int myRD = Integer.valueOf(myRDString);
             double myS;
             
             String oppRankAndRD = DataOperation.getRankAndRD(id2);
     		 String oppRankString = oppRankAndRD.split(" ")[0];
     		 String oppRDString = oppRankAndRD.split(" ")[1];
     		 int oppRank = Integer.valueOf(oppRankString);
     		 int oppRD = Integer.valueOf(oppRDString);
     		 double oppS;
     		 
     		 State st=StateSerializer.unserializeState(state);
             if(meID==0){
            	if(st.getPlayers().get(0).getResult()==GameOver.Result.WIN){
            		myS=1.0;
            		if(st.getPlayers().get(1).getResult()==GameOver.Result.WIN)
            		     oppS=1.0;
            		else if(st.getPlayers().get(1).getResult()==GameOver.Result.LOSE)
            			 oppS=0;
            		else
            			 oppS=0.5;
            	}           	    
            	else if(st.getPlayers().get(0).getResult()==GameOver.Result.LOSE){
            		myS=0.0;
            	    if(st.getPlayers().get(1).getResult()==GameOver.Result.WIN)
       		            oppS=1.0;
       		        else if(st.getPlayers().get(1).getResult()==GameOver.Result.LOSE)
       			        oppS=0;
       		        else
       			        oppS=0.5;
               }else{
            	    myS=0.5;
            	    if(st.getPlayers().get(1).getResult()==GameOver.Result.WIN)
       		            oppS=1.0;
       		        else if(st.getPlayers().get(1).getResult()==GameOver.Result.LOSE)
       			        oppS=0;
       		        else
       			        oppS=0.5;
               }
            		   
             }else{
            	 if(st.getPlayers().get(1).getResult()==GameOver.Result.WIN){
             		myS=1.0;
             		if(st.getPlayers().get(0).getResult()==GameOver.Result.WIN)
             		     oppS=1.0;
             		else if(st.getPlayers().get(0).getResult()==GameOver.Result.LOSE)
             			 oppS=0;
             		else
             			 oppS=0.5;
             	 }           	    
             	 else if(st.getPlayers().get(1).getResult()==GameOver.Result.LOSE){
             		myS=0.0;
             	    if(st.getPlayers().get(0).getResult()==GameOver.Result.WIN)
        		         oppS=1.0;
        		    else if(st.getPlayers().get(0).getResult()==GameOver.Result.LOSE)
        			     oppS=0;
        		    else
        			     oppS=0.5;
                 }else{
             	    myS=0.5;
             	    if(st.getPlayers().get(0).getResult()==GameOver.Result.WIN)
        		            oppS=1.0;
        		        else if(st.getPlayers().get(0).getResult()==GameOver.Result.LOSE)
        			        oppS=0;
        		        else
        			        oppS=0.5;
                 } 
             }
            	 
             String rankAndRD1=DataOperation.updateGamePeriodData(id1, currentDateString, myRD,(double) myRank, oppS);
             String rank1=rankAndRD1.split(" ")[0];
             String RD1=rankAndRD1.split(" ")[1];
             channelService.sendMessage(new ChannelMessage(id1,"R"+","+rank1+","+RD1));
             String rankAndRD2=DataOperation.updateGamePeriodData(id2, currentDateString,oppRD, (double) oppRank, myS);
             String rank2=rankAndRD2.split(" ")[0];
             String RD2=rankAndRD2.split(" ")[1];
             channelService.sendMessage(new ChannelMessage(id2,"R"+","+rank2+","+RD2));
		     return state;
	  }
     //get rank and RD
	  @Override
	  public String getRank(String email){
		  String rankAndRD = DataOperation.getRankAndRD(email);
		  String rank = "1500";
	      String RD = "350";
	    
	      if (rankAndRD != null) {	    	   
			 	 rank = rankAndRD.split(" ")[0];			 	
				 RD = rankAndRD.split(" ")[1];
		  }
		  return  "R"+","+rank + "," + RD;

	  }
      //update player information
	  @Override
	  public void updatePlayerInfo(String email, String name){
		  PlayerInfo p = DataOperation.getPlayer(email);
		  Date date = new Date();
		  DateFormat dtf = DateFormat.getDateInstance();
          String formatedDate = dtf.format(date);
          if (p != null) {
        	  p.name = name;
        	  if(p.gamePeriodData == null|| !p.gamePeriodData.getDate().equals(formatedDate)) {
                     p.gamePeriodData = new GamePeriodData(formatedDate);
              }
        	  DataOperation.savePlayer(p);
          }
	      else {
	    	  PlayerInfo playerInfo = new PlayerInfo(email, name);
	    	  playerInfo.gamePeriodData = new GamePeriodData(formatedDate);
	    	  DataOperation.savePlayer(playerInfo);
	      }
	  }
}
