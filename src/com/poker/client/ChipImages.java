package com.poker.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

public interface ChipImages extends ClientBundle {
	    @Source ("resources/10.jpg")
	    ImageResource chip10();
	    
	    @Source ("resources/20.jpg")
	    ImageResource chip20();
	    
	    @Source ("resources/50.jpg")
	    ImageResource chip50();
	    
	    @Source ("resources/100.jpg")
	    ImageResource chip100();
	    
	    @Source ("resources/PairsPlus.jpg")
	    ImageResource PairsPlus();
	    
	    @Source ("resources/Ante.jpg")
	    ImageResource Ante();
}
