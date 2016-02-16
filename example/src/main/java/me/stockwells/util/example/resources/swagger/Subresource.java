package me.stockwells.util.example.resources.swagger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api
public class Subresource {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "returns testing")
	@ApiResponses({@ApiResponse(code = 200, message="Returns testing")})
	public String test() {
		return "testing";
	}
}
