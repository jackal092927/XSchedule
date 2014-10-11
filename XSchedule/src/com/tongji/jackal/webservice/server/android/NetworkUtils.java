package com.tongji.jackal.webservice.server.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.sun.jersey.api.client.ClientResponse.Status;

@Path("calendar")
public class NetworkUtils {

	@POST
	@Path("sync")
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response syncEvents(@FormParam("account") String account,
			@FormParam("authtoken") String authToken,
			@FormParam("syncmark") String syncMark,
			@FormParam("content") String content)
			throws JsonGenerationException, JsonMappingException, IOException {
		
		if (syncMark == null || syncMark.length() < 1) {
			syncMark = "0";
		}
		SyncManager syncManager = new SyncManager(account, authToken,
				Long.valueOf(syncMark));

		if (!syncManager.authorize()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		List<ScheduleEvent4JSON> clientDirtyList = new ArrayList<ScheduleEvent4JSON>();
		ObjectMapper mapper = new ObjectMapper();
		if (content == null || content.length() < 1) {
			
		}else {
			
			clientDirtyList = mapper.readValue(content,
					new TypeReference<List<ScheduleEvent4JSON>>() {
					});
		}
		
		
		syncManager.setClientDirtyList(clientDirtyList);
		syncManager.sync();
		List<ScheduleEvent4JSON> events = syncManager.getServerDirtyList();

		String output = mapper.writeValueAsString(events);
		
		return Response.status(Status.OK).entity(output).build();

	}
}
