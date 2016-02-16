package me.stockwells.util.example.api;

import com.codahale.metrics.annotation.Timed;
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
	@Timed
	String get(@PathParam("key") String key);

	@POST
	@Path("{key}/{value}")
	@Timed
	void get(@PathParam("key") String key, @PathParam("value") String value);
}
