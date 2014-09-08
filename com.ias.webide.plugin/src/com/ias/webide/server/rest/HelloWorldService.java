package com.ias.webide.server.rest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/db")
public class HelloWorldService {

	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg) {

		String output = "Jersey say : " + msg;

		return Response.status(200).entity(output).build();

	}

	@Path("smooth")
	@GET
	public Response smooth(@DefaultValue("2") @QueryParam("step") int step, @DefaultValue("true") @QueryParam("min-m") boolean hasMin,
			@DefaultValue("true") @QueryParam("max-m") boolean hasMax, @DefaultValue("true") @QueryParam("last-m") boolean hasLast) {
		return null;
	}
}