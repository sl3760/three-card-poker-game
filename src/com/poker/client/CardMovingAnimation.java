package com.poker.client;
import com.google.gwt.animation.client.Animation;
//import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

public class CardMovingAnimation extends Animation{
	
	FlowPanel panel;
	Image start, end, moving;
    int startX, startY;
    int endX, endY;

    
    public CardMovingAnimation(FlowPanel panel,Image startImage, Image endImage, int width, int height){
    	this.panel=panel;
    	start = startImage;
        end = endImage;
       
        startX = width;
        startY = height;
        endX = width;
        endY = height;
       
        moving = startImage;
        moving.setPixelSize(startX, startY);
        panel.add(moving);
    }
   
    @Override
    protected void onUpdate(double progress) {
   
    	 int x = (int) (startX + (endX - startX) * progress);
         int y = (int) (startY + (endY - startY) * progress);
         double scale = 1 + 0.5 * Math.sin(progress * Math.PI);
         int width = (int) (startX * scale);
         int height = (int) (startY * scale);
         moving.setPixelSize(width, height);
         x =x- (width - startX) / 2;
         y =y- (height - startY) / 2;
         moving.setPixelSize(width, height);
         panel.add(moving);

    }

}
