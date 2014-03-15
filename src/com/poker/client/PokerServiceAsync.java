package com.poker.client;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
public interface PokerServiceAsync {
       void autoMatch(String userId, String NickName, String state,AsyncCallback<String> callback);
       //void Move(String state, String id1, String id2, AsyncCallback<String> callback);
       void AMove(String state, Long matchId, String id1, String id2, AsyncCallback<String> callback);
       void EndMove(String state, Long matchId, String id1, String id2, int meID,AsyncCallback<String> callback);
       void sendNewMatch(String p1Email, String p2Email,String state,AsyncCallback<Void> callback);
       void requestMatchList(String userid, AsyncCallback<List<String>> callback);
       void deleteMatchFromPlayer(String email, Long matchId,AsyncCallback<Void> callback);
       void getStateWithMatchId(Long matchId,AsyncCallback<String> callback);
       void getRank(String email,AsyncCallback<String> callback);
       void updatePlayerInfo(String email, String name,AsyncCallback<Void> callback);
}
