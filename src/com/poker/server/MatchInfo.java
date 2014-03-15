package com.poker.server;
import java.io.Serializable;
import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import java.util.Date;

@Entity
@Embed
public class MatchInfo implements Serializable{
        
	    private static final long serialVersionUID = 1L;
  	 	@Id
  	 	private Long matchID;
  	 	private String playerOneEmail;
  	 	private String playerTwoEmail;
  	    private String state;
  	 	private String turn;
  	 	Date startDate;
  	 	
  	 	public MatchInfo() {
  	 	
  	 	}
  	 	
  	 	public MatchInfo(Long matchId, String p1Email, String p2Email, String state,String turn,Date date) {
  	 	    this.matchID = matchId;
  	 	    this.playerOneEmail = p1Email;
  	 	    this.playerTwoEmail = p2Email;
  	 	    this.state = state;
  	 	    this.turn = turn;
  	 	    this.startDate=date;

  	 	}
  	 	
  	 	public Long getMatchId(){
  	 	    return this.matchID;	
  	 	}
  	 	
  	 	public void setPlayerOneEmail(String playerOneEmail){
  	 		this.playerOneEmail=playerOneEmail;
  	 	}
  	 	
  	 	public String getPlayerOneEmail(){
  	 		return this.playerOneEmail;
  	 	}
  	 	
  	 	public void setPlayerTwoEmail(String playerTwoEmail){
  	 		this.playerTwoEmail=playerTwoEmail;
  	 	}
  	 	
  	 	public String getPlayerTwoEmail(){
  	 		return this.playerTwoEmail;
  	 	}
  	 	
  	 	public void setState(String state){
  	 		this.state=state;
  	 	}
  	 	
  	 	public String getState(){
  	 		return this.state;
  	 	}
  	 	
  	 	public void setTurn(String turn){
  	 	   this.turn=turn;	
  	 	}
  	 	
  	 	public String getTurn(){
  	 		return this.turn;
  	 	}
  	 	
  	 	
  	 	@Override
  	 	public String toString() {
  	 	   return matchID + "##" + playerOneEmail + "##" + playerTwoEmail + "##" + turn+"##"+startDate.toString() ;
  	 	}
  	 	
  	 	@Override
  	 	public boolean equals(Object obj){
  	 	   if (this == obj) return true;
  	 	   if (obj == null) return false;
  	 	   if (!(obj instanceof MatchInfo)) return false;
  	 	
  	 	   MatchInfo m=(MatchInfo) obj;
  	 	   return this.matchID.equals(m.matchID);
  	 	}
}
