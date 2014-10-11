package com.tongji.jackal.google.autho.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Collections;

import javax.jdo.JDOHelper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.jdo.auth.oauth2.JdoCredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;

public class GoogleAuthoService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1582990812694255597L;
	
	private static final String CALENDARSCOPE = "https://www.googleapis.com/auth/calendar";
	private static final String CALLBACK_URL = "http://warlock333.xicp.net/XSchedule/google_response.faces";
	private static final String CLIENT_ID = "369292658168.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "taafYZ5UqIeWseNbyeKHg8ul";
	
	public GoogleAuthoService(){
		
	}
	
	public String getAuthorizaionUrlString() throws IOException{
		//TODO:
		String authorizeUrl = "";
		AuthorizationCodeFlow authorizationCodeFlow = initializeFlow();
		Credential credential = authorizationCodeFlow
				.loadCredential("092927");
		if (credential == null) {

			authorizeUrl = authorizationCodeFlow.newAuthorizationUrl().setState("hello 092927")
					.setRedirectUri(getRedirectUri()).build();
			System.out.println("Paste this url in your browser: ");
			System.out.println(authorizeUrl);

		}
		return authorizeUrl;
	}
	
	public String getRedirectUri(){
//		GenericUrl url = new GenericUrl(req.getRequestURL().toString());
//		url.setRawPath("/GoogleCalendar");
//		System.out.println(url.build());
//		return url.build();
		
		return CALLBACK_URL;
	}

	public AuthorizationCodeFlow initializeFlow() throws IOException {
		return new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(),
				new JacksonFactory(), CLIENT_ID, CLIENT_SECRET,
				Collections.singleton(CALENDARSCOPE))
//				.setCredentialStore(
//						new JdoCredentialStore(
//								JDOHelper
//										.getPersistenceManagerFactory("transactions-optional")))
				.build();
	}

	
	
}
