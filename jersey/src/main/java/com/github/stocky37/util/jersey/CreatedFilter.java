package com.github.stocky37.util.jersey;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;


@ParametersAreNonnullByDefault
public abstract class CreatedFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
		if(response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
			response.setStatusInfo(Response.Status.CREATED);
			Optional<URI> uri = location(request.getUriInfo(), response);
			if(uri.isPresent()) {
				response.getHeaders().putSingle(HttpHeaders.LOCATION, uri.get());
			}
		}
	}

	protected abstract Optional<URI> location(UriInfo info, ContainerResponseContext response);
}
