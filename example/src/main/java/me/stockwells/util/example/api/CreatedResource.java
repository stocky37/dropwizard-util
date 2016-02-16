package me.stockwells.util.example.api;

import me.stockwells.util.example.representations.Example;
import me.stockwells.util.jersey.Created;
import io.swagger.annotations.Api;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api
@Produces(MediaType.APPLICATION_JSON)
public interface CreatedResource {
	@POST
	@Created("value")
	Example created();

	@POST
	@Path("invalid")
	@Created("invalid")
	Example invalid();
}
