package com.poker.server;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static com.googlecode.objectify.ObjectifyService.ofy;
import com.googlecode.objectify.Work;

public class DataOperation {
	
	 //the function name can show its function
	 public static String getPlayersInfoWithMatchId(Long id) {
	    MatchInfo m = ofy().load().type(MatchInfo.class).id(id).now();
	  	if (m == null)
	  	    return null;
	    return m.getPlayerOneEmail() + "##" + m.getPlayerTwoEmail();		
	  }
	 
	 public static String getStateAndPlayerAndTurnInfoWithMatchId(Long id){
		 MatchInfo m = ofy().load().type(MatchInfo.class).id(id).now();
         if (m == null)
                 return null;
         return m.getState()+"##"+m.getPlayerOneEmail()+"##"+m.getPlayerTwoEmail()+"##";
	 }
	 
	 public static String getTurnWithMatchId(Long id) {
         MatchInfo m = ofy().load().type(MatchInfo.class).id(id).now();
         if (m == null)
                 return null;
         return m.getTurn();
     }

	//updating the game
	public static void updateMatch(final Long id, final String state,final String turn) {
		  	 	@SuppressWarnings("unused")
		  	 	Boolean res = ofy().transact(new Work<Boolean>() {
		  	 	public Boolean run() {
		        MatchInfo m = ofy().load().type(MatchInfo.class).id(id).now();
		  	 	if (m == null)
		  	 	    return false;
		  	 	m.setState(state);
		  	 	m.setTurn(turn);
		     	ofy().save().entity(m);
		  	 	
		  	 	String players = getPlayersInfoWithMatchId(id);
		  	 	String p1Email = players.split("##")[0];
		  	 	String p2Email = players.split("##")[1];
		  	 	
		  	 	// load p1 add match
		  	 	PlayerInfo p1 = ofy().load().type(PlayerInfo.class).id(p1Email).now();
		  	 	if (p1==null)
		  	 	   return false;
		  	 	
		  	 	p1.deleteMatch(m);
		  	 	p1.addMatch(m);
		  	 	
		  	 	ofy().save().entity(p1);
		  	 	
		  	 	// load p2 add match
		  	 	PlayerInfo p2 = ofy().load().type(PlayerInfo.class).id(p2Email).now();
		  	 	if (p2 == null)
		  	 	    return false;		  	 	
		  	 	p2.deleteMatch(m);
		  	 	p2.addMatch(m);		  	 	
		  	 	ofy().save().entity(p2);
		  	 	return true;
		  	 	}
		  	 	});
		  	 	
		 }
	
	    
	  	public static boolean deleteMatchFromPlayer(final String email,final Long matchId) {
	  	 	
	  	 	Boolean res = ofy().transact(new Work<Boolean>() {
	   	
	  	 	   public Boolean run() {
	  	 	      PlayerInfo p = ofy().load().type(PlayerInfo.class).id(email).now();
	  	 	      if (p == null)
	  	 	          return false;
	  	 	
	  	 	     Iterator<MatchInfo> itr=p.getMatches().iterator();
	  	 	     while(itr.hasNext()){
	   	             MatchInfo m=itr.next();
	  	 	         if(m.getMatchId().equals(matchId))
	  	 	             itr.remove();
	  	 	      }
	  	 	     ofy().save().entities(p);
	  	 	     return true;
	  	 	}
	   	
	  	    });
	  	 	return res;
	  	}
	  	
	  	 public static List<MatchInfo> getMatchList(String email) {
		  	 	PlayerInfo p = ofy().load().type(PlayerInfo.class).id(email).now();
		  	 	if (p == null)
		  	 	     return null;
		  	 	return p.getMatchList();
		 }
		 	 		
	  	public static List<String> getMatchStringList(String email) {	
			List<MatchInfo> matchList = getMatchList(email);
        	List<String> matchStringList = new LinkedList<String>();
		  	if (matchList == null || matchList.isEmpty()) {
		  	    return matchStringList;
		  	}
		  	for (MatchInfo m : matchList) {
		  		matchStringList.add(m.toString());
		  	}
		     	return matchStringList;
		}
	  	
	  	public static PlayerInfo getPlayer(String email) {
		  	return ofy().load().type(PlayerInfo.class).id(email).now();
        }
       
	  	public static void savePlayer(PlayerInfo player) {
		    ofy().save().entities(player);
		}
			
        public static void deletePlayerWithEmail(String email) {
		    ofy().delete().type(PlayerInfo.class).id(email);
	    }

        public static void deleteMatchWithMatchId(String matchid) {
		   	ofy().delete().type(MatchInfo.class).id(matchid);
		}
        //start a transaction by inputing a email
        public static void newMatchTransaction(Long matchId, final String p1Email,
                    final String p2Email, String state, String turn, Date date) {
        	 final MatchInfo newMatch = new MatchInfo(matchId, p1Email, p2Email, state,turn,date);
        	 
        	 @SuppressWarnings("unused")
        	 Boolean res = ofy().transact(new Work<Boolean>() {
        	     public Boolean run() {
        	    	 
        	  	    ofy().save().entity(newMatch);
	                // load p1 add match
        	        PlayerInfo p1 = ofy().load().type(PlayerInfo.class).id(p1Email).now();       	        
        	  		if (p1 == null)
        	  	        p1 = new PlayerInfo(p1Email, null); 
        	  		p1.addMatch(newMatch);   	
        	     	ofy().save().entity(p1);  
        	     	
        	     	// load p2 add match
        	      	PlayerInfo p2 = ofy().load().type(PlayerInfo.class).id(p2Email).now();
        	     	if (p2 == null)
        	  		    p2 = new PlayerInfo(p2Email, null);
        	  	    p2.addMatch(newMatch);        	  		
        	      	ofy().save().entity(p2);
        	        
        	  		return true;
        	  	 	}
        	  	 	
        	 });
    	}
        
      
        public static String getRankAndRD(String email) {
    		PlayerInfo p = ofy().load().type(PlayerInfo.class).id(email).now();
    		if (p == null)
    		  return null;
    		return String.valueOf(p.rank) + " " + String.valueOf(p.rd);
    	}
        
        public static String updateGamePeriodData(final String email,
                final String date, final int RD, final double r, final double s) {
               @SuppressWarnings("unused")
               String res = ofy().transact(new Work<String>() {
                public String run() {

                        PlayerInfo p = ofy().load().type(PlayerInfo.class).id(email).now();
                        String recordDate = p.gamePeriodData.getDate();
                        Date currentDate;

                        try {
                                currentDate = new SimpleDateFormat("MMMM d, yyyy",
                                                Locale.ENGLISH).parse(date);
                        } catch (ParseException e) {
                               
                                return null;
                        }

                        // if in same game period, just add new info and update ranking
                        if (p != null) {
                                if (recordDate.equals(date)) {
                                        p.gamePeriodData.addr(r);
                                        p.gamePeriodData.addRD(RD);
                                        p.gamePeriodData.adds(s);
                                        ofy().save().entity(p);
                                        updateRanking(email, currentDate);

                                } else {

                                        // update ranking, create a new Game Period Data, add
                                        // the corresponding info

                                        updateRanking(email, currentDate);

                                        p.gamePeriodData = new GamePeriodData(
                                                        dateToString(currentDate));
                                        p.gamePeriodData.addr(r);
                                        p.gamePeriodData.addRD(RD);
                                        p.gamePeriodData.adds(s);
                                        ofy().save().entity(p);

                                }
                        }else{
                                return null;
                        }

                        return p.rank+" "+p.rd;
                  }
                });
       
              return res;
        }

        public static void updateRanking(final String email, final Date currentDate) {
            @SuppressWarnings("unused")
            Boolean res = ofy().transact(new Work<Boolean>() {

                    public Boolean run() {

                            PlayerInfo p = ofy().load().type(PlayerInfo.class)
                                            .id(email).now();

                            if (p != null) {
                                    GamePeriodData data = p.gamePeriodData;
                                    if (data != null) {
                                            ArrayList<Integer> RDs = data.getRDs();
                                            ArrayList<Double> rs = data.getrs();
                                            ArrayList<Double> s = data.gets();

                                            // compute how long passed since last competition
                                            Date lastCompDate = p.lastCompetition;
                                            int daysPassed;
                                            if (lastCompDate == null) {
                                                    daysPassed = 365;
                                            } else {
                                                    daysPassed = daysBetween(lastCompDate, currentDate);
                                            }
                                            Ranking ranking = new Ranking(RDs, rs, s, p.rank, p.rd,daysPassed);

                                            int newRank = (int) ranking.computeNewRanking();
                                            int newRD = (int) ranking.computeNewRD();

                                            p.rank = newRank;
                                            p.rd = newRD;
                                            p.lastCompetition=currentDate;
                                            ofy().save().entity(p);
                                    }

                            }

                            return true;
                    }
            });

    }

    public static int daysBetween(Date earlier, Date later) {
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            calendar1.setTime(earlier);
            calendar2.setTime(later);

            long milliseconds1 = calendar1.getTimeInMillis();

            long milliseconds2 = calendar2.getTimeInMillis();
            long diff = milliseconds2 - milliseconds1;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            return (int) diffDays;

    }

    public static String dateToString(Date date) {
            DateFormat dtf = DateFormat.getDateInstance();
            String formatedDate = dtf.format(date);
            return formatedDate;
    }

}
