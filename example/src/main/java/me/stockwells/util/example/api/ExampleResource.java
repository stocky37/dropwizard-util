package me.stockwells.util.example.api;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;


@Api("Examples")
@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ExampleResource {

	@Path("cache")
	@Produces(MediaType.TEXT_PLAIN)
	CachedResource cache();

	@Path("created")
	CreatedResource created(@QueryParam("value") String key);

	@GET
	@Timed
	@Produces(MediaType.TEXT_PLAIN)
	String test();
}
