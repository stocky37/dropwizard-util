package me.stockwells.util.jersey;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;


public class JsonCreatedFilter extends CreatedFilter {

	private final ObjectMapper mapper;
	private final String field;
	private final boolean absolute;

	public JsonCreatedFilter(ObjectMapper mapper, String field, boolean absolute) {
		this.mapper = mapper;
		this.field = field;
		this.absolute = absolute;
	}

	@Override
	protected URI location(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
		JsonNode node = extractJsonNode(response);
		String id = node.get(field).asText();
		return getUriBuilder(request.getUriInfo()).path(id).build();
	}

	private UriBuilder getUriBuilder(UriInfo uri) {
		return absolute ? uri.getAbsolutePathBuilder() : uri.getBaseUriBuilder();
	}

	protected JsonNode extractJsonNode(ContainerResponseContext response) throws IOException {
		return mapper.readTree(mapper.writeValueAsString(response.getEntity()));
	}
}
