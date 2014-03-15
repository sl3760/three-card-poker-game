package com.poker.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.poker.client.LoginInfo;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.HasRpcToken;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.XsrfToken;
import com.google.gwt.user.client.rpc.XsrfTokenService;
import com.google.gwt.user.client.rpc.XsrfTokenServiceAsync;

public class ThreeCardPokerEntryPoint implements EntryPoint {
	private LoginInfo loginInfo = null;
	public void onModuleLoad() {
		     
		     final Presenter presenter = new Presenter();
		     final Graphics graphics = new Graphics(presenter);	
		     //String userId;
		    //presenter.initializeHistory();	
		     Cookies.setCookie("JSESSIONID", "JSESSIONID", null, null, "/", false);
		     XsrfTokenServiceAsync xsrf = (XsrfTokenServiceAsync)GWT.create(XsrfTokenService.class);
		     ((ServiceDefTarget)xsrf).setServiceEntryPoint(GWT.getModuleBaseURL() + "xsrf");
		     xsrf.getNewXsrfToken(new AsyncCallback<XsrfToken>() {
		    	 public void onSuccess(final XsrfToken token) {
		    		    LoginServiceAsync loginService = GWT.create(LoginService.class);
		    		    ((HasRpcToken) loginService).setRpcToken(token);	    		   
		    		    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
		  	                public void onFailure(Throwable error) {
		  	            	  Window.alert(error.getMessage());
		  	                }
		  	                  
		  	                public void onSuccess(LoginInfo result) {
		  	            	  
		  	                 loginInfo = result;
		  	                 if(loginInfo.isLoggedIn()) {	 
		  	                	presenter.setSignOutUrl(loginInfo.getLogoutUrl());
		  	                	presenter.setId(loginInfo.getEmailAddress());
		  	                	presenter.setNickName(loginInfo.getNickname());
		  	                	Window.alert(Graphics.messages.setWelcome()+loginInfo.getEmailAddress());	                	
		                        presenter.openGame(loginInfo.getToken(),token);
		  	                
		                      } else {
		                      	LoginPanel loginpanel=  new LoginPanel(loginInfo.getLoginUrl());
		                      	presenter.setSignOutUrl(loginInfo.getLogoutUrl());
		                          loginpanel.center();                      
		                  }
		                }
		  	           });
		    		  }

		    		  public void onFailure(Throwable caught) {
		    		    try {
		    		      throw caught;
		    		    } catch (RpcTokenException e) {
		    		      
		    		    } catch (Throwable e) {
		    		    
		    		    }
		    		  }
		     });
		     
	         
	         presenter.initState(graphics);
	         RootPanel.get().add(graphics);    
    }
}
