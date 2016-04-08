package me.stockwells.util.jersey;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class JsonCreatedFilter extends CreatedFilter {

	private final String field;
	private final ObjectMapper mapper;

		public JsonCreatedFilter(String field, ObjectMapper mapper) {
		this.field = field;
		this.mapper = mapper;
	}

	@Override
	protected Optional<URI> location(UriInfo uri, ContainerResponseContext response) {
		JsonNode node = mapper.valueToTree(response.getEntity());
		JsonNode valueNode = node.get(field);
		if(valueNode == null || valueNode.isNull()) {
			return Optional.empty();
		}
		return Optional.of(uri.getAbsolutePathBuilder().path(valueNode.asText()).build());
	}
}
