package com.tongji.jackal.webservice.server.test.services;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;

import com.sun.jersey.api.client.ClientResponse.Status;

@Path("auth")
public class TestAuth {

	@POST
	@Path("test")
	public Response authorization(@FormParam("username") String username,
			@FormParam("password") String password
			){
		
		if ("test".equals(username)) {
			if ("test".equals(password)) {
				
				return Response.status(Status.OK).entity("helloworld").build();
			}
		}
		return Response.status(Status.UNAUTHORIZED).entity("fail").build();
	}
}
