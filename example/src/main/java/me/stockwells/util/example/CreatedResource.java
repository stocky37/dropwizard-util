package me.stockwells.util.example;

import me.stockwells.util.jersey.Created;

import javax.ws.rs.POST;
import javax.ws.rs.Path;


@Path("created")
public interface CreatedResource {

	@POST
	@Created("key")
	CreatedResourceImpl.TestJsonRepr created();

	@POST
	@Path("wrong")
	@Created("wrongkey")
	CreatedResourceImpl.TestJsonRepr wrongcreated();

	@POST
	@Path("not")
	CreatedResourceImpl.TestJsonRepr notCreated();
}
