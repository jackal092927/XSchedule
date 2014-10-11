package com.tongji.jackal.restTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

public class TestGoogleCalendar {

//	private static final String SCOPE = "https://www.googleapis.com/auth/urlshortener";
	private static final String SCOPE2 = "https://www.googleapis.com/auth/calendar";
	// private static final String CALLBACK_URL =
	// GoogleOAuthConstants.OOB_REDIRECT_URI;
	private static final String CALLBACK_URL = "http://warlock333.xicp.net/TestREST/rest/hello/jackal";
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static String userId = "jackal092927@gmail.com";

	// FILL THESE IN WITH YOUR VALUES FROM THE API CONSOLE
	private static final String CLIENT_ID = "369292658168.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "taafYZ5UqIeWseNbyeKHg8ul";

	@Test
	public void Test() {
		try {

			// Generate the URL to which we will direct users
			// String authorizeUrl = new
			// GoogleAuthorizationRequestUrl(CLIENT_ID,
			// CALLBACK_URL, SCOPE).build();
			List<String> scopes = new ArrayList<String>();
//			scopes.add(SCOPE);
			scopes.add(SCOPE2);

			// String authorizeUrl = new
			// GoogleAuthorizationCodeRequestUrl(CLIENT_ID, CLIENT_SECRET,
			// scopes).build();
			// System.out.println("Paste this url in your browser: " +
			// authorizeUrl);
			String authorizeUrl = "NULL";

			// Wait for the authorization code
			// System.out.println("Type the code you received here: ");
			// BufferedReader in = new BufferedReader(new
			// InputStreamReader(System.in));
			// String authorizationCode = in.readLine();
			//
			AuthorizationCodeFlow authorizationCodeFlow = new GoogleAuthorizationCodeFlow(
					TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, scopes);
			Credential credential = authorizationCodeFlow
					.loadCredential(userId);
			if (credential == null) {

				authorizeUrl = authorizationCodeFlow.newAuthorizationUrl()
						.setRedirectUri(CALLBACK_URL).build();
				System.out.println("Paste this url in your browser: ");
				System.out.println(authorizeUrl);

				System.out.println("Type the code you received here: ");
				BufferedReader in = new BufferedReader(new InputStreamReader(
						System.in));
				String authorizationCode = in.readLine();

				AuthorizationCodeTokenRequest authorizationCodeTokenRequest = authorizationCodeFlow
						.newTokenRequest(authorizationCode);
				authorizationCodeTokenRequest.setRedirectUri(CALLBACK_URL);//why?

				TokenResponse tokenResponse = authorizationCodeTokenRequest
						.execute();

				credential = authorizationCodeFlow.createAndStoreCredential(
						tokenResponse, authorizationCode);

				System.out.println("Access Token: "
						+ credential.getAccessToken());

			}

			// Exchange for an access and refresh token
			/*
			 * GoogleAuthorizationCodeGrant authRequest = new
			 * GoogleAuthorizationCodeGrant( TRANSPORT, JSON_FACTORY, CLIENT_ID,
			 * CLIENT_SECRET, authorizationCode, CALLBACK_URL);
			 * 
			 * authRequest.useBasicAuthorization = false; AccessTokenResponse
			 * authResponse = authRequest.execute(); String accessToken =
			 * authResponse.accessToken; GoogleAccessProtectedResource access =
			 * new GoogleAccessProtectedResource( accessToken, TRANSPORT,
			 * JSON_FACTORY, CLIENT_ID, CLIENT_SECRET,
			 * authResponse.refreshToken); HttpRequestFactory rf =
			 * TRANSPORT.createRequestFactory(access);
			 * System.out.println("Access token: " + authResponse.accessToken);
			 */

			// Make an authenticated request
			/*
			 * GenericUrl shortenEndpoint = new GenericUrl(
			 * "https://www.googleapis.com/urlshortener/v1/url"); String
			 * requestBody =
			 * "{\"longUrl\":\"http://farm6.static.flickr.com/5281/5686001474_e06f1587ff_o.jpg\"}"
			 * ; HttpRequest request = rf.buildPostRequest(shortenEndpoint,
			 * ByteArrayContent.fromString("application/json", requestBody));
			 * HttpResponse shortUrl = request.execute(); BufferedReader output
			 * = new BufferedReader(new InputStreamReader(
			 * shortUrl.getContent()));
			 * System.out.println("Shorten Response: "); for (String line =
			 * output.readLine(); line != null; line = output .readLine()) {
			 * System.out.println(line); }
			 * 
			 * // Refresh a token (SHOULD ONLY BE DONE WHEN ACCESS TOKEN
			 * EXPIRES) access.refreshToken();
			 * System.out.println("Original Token: " + accessToken +
			 * " New Token: " + access.getAccessToken());
			 */
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
