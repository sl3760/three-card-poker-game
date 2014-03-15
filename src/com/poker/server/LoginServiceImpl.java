package com.poker.server;

import com.poker.client.LoginInfo;
import com.poker.client.LoginService;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet; 

public class LoginServiceImpl extends XsrfProtectedServiceServlet implements
		LoginService {
	private static final long serialVersionUID = 1L;
	ChannelService channelService = ChannelServiceFactory.getChannelService();
	UserService userService = UserServiceFactory.getUserService();

	@Override
	public LoginInfo login(String requestUri) {
		User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();
		//judge if user exists 
		if (user != null) {
			loginInfo.setLoggedIn(true);
			loginInfo.setEmailAddress(user.getEmail());
			loginInfo.setNickname(user.getNickname());
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
			String token = channelService.createChannel(loginInfo
					.getEmailAddress());
			loginInfo.setToken(token);
		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
		}
		return loginInfo;
	}

}