package com.poker.client;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginPanel extends PopupPanel {
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(Graphics.messages.setSigninInfo());
	private Anchor signInLink = new Anchor(Graphics.messages.setSignin());
	public LoginPanel(String url){		
			signInLink.setHref(url);
		    loginPanel.add(loginLabel);
		 	loginPanel.add(signInLink);
		 	this.setGlassEnabled(true);
		 	this.setAnimationEnabled(true);
		 	this.setWidget(loginPanel);

	}
}
