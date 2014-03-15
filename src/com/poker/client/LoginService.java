package com.poker.client;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.XsrfProtectedService;

@RemoteServiceRelativePath("login")     
public interface LoginService extends XsrfProtectedService{
           public LoginInfo login(String requestUri);

}