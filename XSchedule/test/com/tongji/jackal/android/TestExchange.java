package com.tongji.jackal.android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.tongji.jackal.webservice.server.android.Constants;
import com.tongji.jackal.webservice.server.android.ScheduleEvent4JSON;

public class TestExchange {

	@Test
	public void Test() {
		try {

			Client client = Client.create();

			String resourceUri = Constants.WS_BASE_URL + "calendar/sync";

			WebResource webResource = client.resource(resourceUri);

			ObjectMapper mapper = new ObjectMapper(); // can reuse, share
														// globally
			// ScheduleEvent4JSON scheduleEvent = mapper.readValue(new
			// File("user.json"), User.class);
			ScheduleEvent4JSON scheduleEvent = new ScheduleEvent4JSON(012, 38);
			scheduleEvent.setLocation("1lou");
			ScheduleEvent4JSON scheduleEvent2 = new ScheduleEvent4JSON(012, 39);
			scheduleEvent2.setLocation("2lou");
			List<ScheduleEvent4JSON> eventList = new ArrayList<ScheduleEvent4JSON>();
			eventList.add(scheduleEvent);
			eventList.add(scheduleEvent2);
			String input = mapper.writeValueAsString(eventList);

			final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("account", "092927"));
			params.add(new BasicNameValuePair("authtoken", "helloworld"));
			params.add(new BasicNameValuePair("syncmark", "0"));
			params.add(new BasicNameValuePair("content", input));

			HttpEntity entity = new UrlEncodedFormEntity(params);

			// System.out.println(input);

			// String input =
			// "{\"singer\":\"Metallica\",\"title\":\"Fade To Black\"}";

			final HttpPost post = new HttpPost(resourceUri);
			post.addHeader(entity.getContentType());
			post.setEntity(entity);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(post);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}
			// * resp = getHttpClient().execute(post); String authToken = null;
			// if
			// * (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// * InputStream istream = (resp.getEntity() != null) ?
			// * resp.getEntity().getContent() : null; if (istream != null) {
			// * BufferedReader ireader = new BufferedReader(new
			// * InputStreamReader(istream)); authToken =
			// * ireader.readLine().trim(); } }
			String content = null;
			InputStream iStream = (response.getEntity() != null) ? response
					.getEntity().getContent() : null;
			if (iStream != null) {
				BufferedReader iReader = new BufferedReader(
						new InputStreamReader(iStream));
				content = iReader.readLine().trim();

			}
			
			if (content != null && content.length() > 0) {
				List<ScheduleEvent4JSON> events = mapper.readValue(content,
						new TypeReference<List<ScheduleEvent4JSON>>() {
						});
				for (ScheduleEvent4JSON scheduleEvent4JSON : events) {
					System.out.println("event: " + scheduleEvent4JSON.getClientId()
							+ " " + scheduleEvent4JSON.getServerId());
				}
			}else {
				
			}
			
			//
			// ClientResponse response = webResource.type("application/json")
			// .post(ClientResponse.class, input);

			// if (response.getStatus() != 200) {
			// throw new RuntimeException("Failed : HTTP error code : "
			// + response.getStatus());
			// }
			//
			// System.out.println("Output from Server .... \n");
			// String output = response.getEntity(String.class);
			//
			// List<ScheduleEvent4JSON> events = mapper.readValue(output,
			// new TypeReference<List<ScheduleEvent4JSON>>() {});
			// for (ScheduleEvent4JSON scheduleEvent4JSON : events) {
			// System.out.println("event: " + scheduleEvent4JSON.getClientId()
			// + " " + scheduleEvent4JSON.getServerId());
			// }
			// ScheduleEvent4JSON event =
			// mapper.readValue(output,ScheduleEvent4JSON.class);
			// System.out.println(event);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}
