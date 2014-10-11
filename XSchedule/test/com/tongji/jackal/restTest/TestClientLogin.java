package com.tongji.jackal.restTest;

import com.google.api.client.googleapis.*;
import com.google.api.client.googleapis.auth.clientlogin.*;
import com.google.api.client.googleapis.json.*;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.*;

import org.junit.Test;

public class TestClientLogin {

	@Test
	public void Test() {

		// HttpTransport transport = new NetHttpTransport();
		// try {
		// // authenticate with ClientLogin
		// ClientLogin authenticator = new ClientLogin();
		// authenticator.authTokenType = "ndev";
		// authenticator.username = "...";
		// authenticator.password = "...";
		// authenticator.authenticate().setAuthorizationHeader(transport);
		// // make query request
		// HttpRequest request = transport.
		// request.setUrl("https://www.googleapis.com/bigquery/v1/query");
		// request.url.put("q",
		// "select count(*) from [bigquery/samples/shakespeare];");
		// System.out.println(request.execute().toString());
		// } catch (HttpResponseException e) {
		// System.err.println(e.response.parseAsString());
		// throw e;
		// }

		Client client = Client.create();

		WebResource webResource = client
				.resource("https://www.googleapis.com/calendar/v3/users/me/calendarList?access_token=ya29.AHES6ZTdg7UbmVevbMYY-AIC9OlmyFVXON1fi2eIDHqKoA76f6YDaQ");

		ClientResponse response = webResource.accept("application/json").get(
				ClientResponse.class);

		// if (response.getStatus() != 200) {
		// throw new RuntimeException("Failed : HTTP error code : "
		// + response.getStatus());
		// }

		String output = response.getEntity(String.class);

		System.out.println("Output from Server .... \n");
		System.out.println(output);
	}

}
