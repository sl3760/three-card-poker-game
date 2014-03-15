package com.poker.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.XsrfProtectedService;

@RemoteServiceRelativePath("PokerService")	
public interface PokerService extends XsrfProtectedService{
	public String autoMatch(String userId, String NickName, String state);
	//public String Move(String state, String id1, String id2);
	public String AMove(String state,Long matchId,String id1, String id2);
	public String EndMove(String state,Long matchId,String id1, String id2, int meID);
	public void sendNewMatch(String p1Email,String p2Email,String state);
    public List<String> requestMatchList(String userid);
    public void deleteMatchFromPlayer(String email, Long matchId);
    public String getStateWithMatchId(Long matchId);
    public String getRank(String email);
    public void updatePlayerInfo(String email, String name);
}
