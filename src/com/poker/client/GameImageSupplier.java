package com.poker.client;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
public class GameImageSupplier {
	  private final GameImages gameImages;
	  
	  public GameImageSupplier() {
	    gameImages = GWT.create(GameImages.class);
	  }
	  
	  public Image getPokerTable() {
	    return new Image(gameImages.pokertable());
	  }
	  
	  public Image getPlayerplace() {
	    return new Image(gameImages.playerplace());
	  }
}
