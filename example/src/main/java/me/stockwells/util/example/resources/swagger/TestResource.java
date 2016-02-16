package me.stockwells.util.example.resources.swagger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


@Api("test")
@Path("test")
public class TestResource {

	@Context private HttpServletResponse response;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Just returns the value shown in the test query paramater", response = String.class)
	@ApiResponses({
		@ApiResponse(code = 200, message = "Returns the test query param", responseHeaders = @ResponseHeader(name = "test", description = "contains the test query param", response = String.class)),
		@ApiResponse(code = 204, message = "If no value for the query param is provided"),
		@ApiResponse(code = 500, message = "If something fucks up")
	})
	public String getStuff(@ApiParam("The query param to return") @QueryParam("test") final String test) {
		response.addHeader("test", test);
		return test;
	}

	@Path("sub")
	public Class<Subresource> sub() {
		return Subresource.class;
	}

}
