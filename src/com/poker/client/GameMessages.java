package com.poker.client;
import com.google.gwt.i18n.client.Messages;

public interface GameMessages extends Messages {   
	  @DefaultMessage("AutoStart")
	  String setAutoStart();
	  @DefaultMessage("RestartGame")
	  String setRestartGame();
	  @DefaultMessage("Save")
	  String setSave();
	  @DefaultMessage("Load")
	  String setLoad();
	  @DefaultMessage("PairsPlus")
	  String setPairsPlus();
	  @DefaultMessage("Ante")
	  String setAnte();
	  @DefaultMessage("PairsPlusAnte")
	  String setPairsPlusAnte();
	  @DefaultMessage("Deal")
	  String setDeal();
	  @DefaultMessage("Play")
	  String setPlay();
	  @DefaultMessage("Fold")
	  String setFold();
	  @DefaultMessage("Cancel")
	  String setCancel();
	  @DefaultMessage("start match")
	  String setStartMatch();
	  @DefaultMessage("Delete match")
	  String setDeleteMatch();
	  @DefaultMessage("Please select match")
	  String setSelectMatch();
	  @DefaultMessage("Sign out")
	  String setSignout();
	  @DefaultMessage("ID:1 Balance: ")
	  String setBalance1();
	  @DefaultMessage("ID:2 Balance: ")
	  String setBalance2();
	  @DefaultMessage("Dealer")
	  String setDealer();
	  @DefaultMessage("ID: ")
	  String setID();
	  @DefaultMessage("Welcome: ")
	  String setWelcome();
	  @DefaultMessage("Now Player ")
	  String setNowPlayer();
	  @DefaultMessage(" turn.")
	  String setSTurn();
	  @DefaultMessage("Email: ")
	  String setEmail();
	  @DefaultMessage("NickName: ")
	  String setNickName();
	  @DefaultMessage("Opponent Email: ")
	  String setOpEmail();
	  @DefaultMessage("Opponent NickName: ")
	  String setOpNickName();
	  @DefaultMessage("Please press start game button to start game")
	  String setInfoS0();
	  @DefaultMessage("Please drag the chips and make a choice:PairsPlus or Ante or PairsPlusAnte")
	  String setInfoS1();
	  @DefaultMessage("Please make a move: ")
	  String setInfoS2();
	  @DefaultMessage("This round is over.Please press ReStartGame button to continue this game.")
	  String setInfoS3();
	  @DefaultMessage("Server connected! Press AutoStart button to play with another online player.")
	  String setConnecting();
	  @DefaultMessage("Now you can play with another player:")
	  String setConnected();
	  @DefaultMessage("Match ID: ")
	  String setMatchID();
	  @DefaultMessage(" Your ID: ")
	  String setYourID();
	  @DefaultMessage(" Opponent: ")
	  String setOpponent();
	  @DefaultMessage(" Turn: ")
	  String setTurn();
	  @DefaultMessage(" Date: ")
	  String setDate();
	  @DefaultMessage("You are player ")
	  String setYouPlayer();
	  @DefaultMessage("You can not play with yourself!")
	  String setPlayYouself();
	  @DefaultMessage("Player ID:1 You ")
	  String setPlayerID1();
	  @DefaultMessage("Player ID:2 You ")
	  String setPlayerID2();	  
	  @DefaultMessage(" Press ReStartGame button to continue this game!")
	  String setShowRestart();
	  @DefaultMessage("Game Over! Player ID: 1 Lose! Player ID: 2 Win!")
	  String setResult1();
	  @DefaultMessage("Game Over! Player ID: 1 Win! Player ID: 2 Lose!")
	  String setResult2();
	  @DefaultMessage("Win!")
	  String setWin();
	  @DefaultMessage("Lose!")
	  String setLose();
	  @DefaultMessage("Tie!")
	  String setTie();
	  @DefaultMessage("Dealer: did not qualify!")
	  String setDealerNotQualify();
	  @DefaultMessage("high")
	  String setHigh();
	  @DefaultMessage("pair")
	  String setPair();
	  @DefaultMessage("flush")
	  String setFlush();
	  @DefaultMessage("straight")
	  String setStraight();
	  @DefaultMessage("three of a kind")
	  String setThreeOfKind();
	  @DefaultMessage("straightflush")
	  String setStraightFlush();
	  @DefaultMessage("Sign In")
	  String setSignin();
	  @DefaultMessage("Please sign in to your Google Account to access the Three-Card-Poker Game Application.")
	  String setSigninInfo();
	  @DefaultMessage("Rank: ")
	  String setRank();
	  @DefaultMessage("Play with computer")
	  String setAIPlay();
	  @DefaultMessage("Now you play with computer. Your ID is 1")
	  String setAIInfo();
}

