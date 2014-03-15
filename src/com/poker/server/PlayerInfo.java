package com.poker.server;

import java.util.LinkedList;
import java.util.List;
import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
@Entity
public class PlayerInfo implements Serializable{
	    private static final long serialVersionUID = 1L;
  	 	@Id
  	 	String email;
  	 	String name;
	 	int rank;
	 	int rd;
	 	Date lastCompetition=null;
	 	GamePeriodData gamePeriodData=null;
  	 	List<MatchInfo> matches = new LinkedList<MatchInfo>();
  	 	
  	 	public PlayerInfo() {
  	 	
  	 	}
  	 	
  	 	public PlayerInfo(String email, String name) {
  	 	    this.email = email;
  	 	    this.name = name;
  	 	    rank=1500;
  	 	    rd=350;
  	 	}
  	 	
  	  	
  	 	public List<MatchInfo> getMatches(){
  	 	    return this.matches;	
  	 	}
  	 	
  	 	 	
  	 	public void addMatch(MatchInfo match) {
  	 	    matches.add(match);
  	 	}
  	 	
  	 	public List<MatchInfo> getMatchList() {
   	 	   return matches;
   	 	}
  	 	
  	 	public void deleteMatch(MatchInfo match) {
  	 	    matches.remove(match);
  	 	}
  		
}
