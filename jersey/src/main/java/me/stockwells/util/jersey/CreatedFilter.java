package me.stockwells.util.jersey;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;


public abstract class CreatedFilter implements ContainerResponseFilter {
	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
		if(response.getStatusInfo() == Response.Status.OK) {
			response.setStatusInfo(Response.Status.CREATED);
			response.getHeaders().putSingle(HttpHeaders.LOCATION, location(request, response));
		}
	}

	protected abstract URI location(ContainerRequestContext request, ContainerResponseContext response) throws IOException;
}
