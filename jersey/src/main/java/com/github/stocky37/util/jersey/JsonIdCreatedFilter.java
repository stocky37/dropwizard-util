package com.github.stocky37.util.jersey;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class JsonIdCreatedFilter extends CreatedFilter {

	private final String field;
	private final ObjectMapper mapper;

	public JsonIdCreatedFilter(String field, ObjectMapper mapper) {
		this.field = field;
		this.mapper = mapper;
	}

	@Override
	protected Optional<URI> location(UriInfo uri, ContainerResponseContext response) {
		JsonNode node = mapper.valueToTree(response.getEntity());
		if(node == null)
			return Optional.empty();

		JsonNode valueNode = node.get(field);
		if(valueNode == null || valueNode.isNull())
			return Optional.empty();

		return Optional.of(uri.getAbsolutePathBuilder().
			path(valueNode.asText()).build());
	}
}
