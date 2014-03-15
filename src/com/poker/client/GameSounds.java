package com.poker.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;

public interface GameSounds extends ClientBundle {
	    @Source ("resources/Start.ogg")
	    DataResource StartOgg();
	    
	    @Source ("resources/Start.mp3")
	    DataResource StartMp3();
	    
	    @Source ("resources/Start.wav")
	    DataResource StartWav();
	    
	    @Source ("resources/Restart.ogg")
	    DataResource RestartOgg();
	    
	    @Source ("resources/Restart.mp3")
	    DataResource RestartMp3();
	    
	    @Source ("resources/Restart.wav")
	    DataResource RestartWav();
	    
	    @Source ("resources/Choose.ogg")
	    DataResource ChooseOgg();
	    
	    @Source ("resources/Choose.mp3")
	    DataResource ChooseMp3();
	    
	    @Source ("resources/Choose.wav")
	    DataResource ChooseWav();
	    
	    @Source ("resources/Decision.ogg")
	    DataResource DecisionOgg();	
	    
	    @Source ("resources/Decision.mp3")
	    DataResource DecisionMp3();
	    
	    @Source ("resources/Decision.wav")
	    DataResource DecisionWav();
	    
	    @Source ("resources/Warning.ogg")
	    DataResource WarningOgg();
	    
	    @Source ("resources/Warning.mp3")
	    DataResource WarningMp3();
	    
	    @Source ("resources/Warning.wav")
	    DataResource WarningWav();
}
