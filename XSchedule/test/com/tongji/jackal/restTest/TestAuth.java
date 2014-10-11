package com.tongji.jackal.restTest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class TestAuth {

	@Test
	public void Test() {
		try {

			final HttpResponse resp;
			final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", "test"));
			params.add(new BasicNameValuePair("password", "test"));
			final HttpEntity entity;
			try {
				entity = new UrlEncodedFormEntity(params);
			} catch (final UnsupportedEncodingException e) {
				// this should never happen.
				throw new IllegalStateException(e);
			}

			final HttpPost post = new HttpPost(
					"http://localhost/XSchedule/webservice/auth/test");
			post.addHeader(entity.getContentType());
			post.setEntity(entity);
			HttpClient httpClient = new DefaultHttpClient();
			resp = httpClient.execute(post);
			String authToken = null;
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream istream = (resp.getEntity() != null) ? resp
						.getEntity().getContent() : null;
				if (istream != null) {
					BufferedReader ireader = new BufferedReader(
							new InputStreamReader(istream));
					authToken = ireader.readLine().trim();
				}
			}

//			Client client = Client.create();
//
//			WebResource webResource = client
//					.resource("http://localhost:80/XSchedule/webservice/auth/test?u");
//
//			ClientResponse response = webResource.accept("application/json")
//					.get(ClientResponse.class);
//
//			if (response.getStatus() != 200) {
//				throw new RuntimeException("Failed : HTTP error code : "
//						+ response.getStatus());
//			}
//
//			String output = response.getEntity(String.class);

			System.out.println("Output from Server .... \n");
			System.out.println(authToken);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

}
