package com.poker.client;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
public interface GameImages extends ClientBundle{
	  @Source("resources/ThreeCardPoker_table.jpg")
	  ImageResource pokertable();
	  @Source("resources/playerplace.jpg")
	  ImageResource playerplace();
}
