package me.stockwells.util.example.api;

import io.swagger.annotations.Api;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Api
@Produces(MediaType.TEXT_PLAIN)
public interface CachedResource {
	@GET
	@Path("{key}")
	String get(@PathParam("key") String key);

	@POST
	@Path("{key}/{value}")
	void get(@PathParam("key") String key, @PathParam("value") String value);
}
