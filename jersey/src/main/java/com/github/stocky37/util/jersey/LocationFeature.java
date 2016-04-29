package com.github.stocky37.util.jersey;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.jackson.Jackson;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;

@ParametersAreNonnullByDefault
public class LocationFeature implements DynamicFeature {
	private final ObjectMapper mapper;

	public LocationFeature() {
		this(Jackson.newObjectMapper());
	}

	public LocationFeature(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public void configure(ResourceInfo info, FeatureContext context) {
		final Location locationAnnotation = info.getResourceMethod().getAnnotation(Location.class);
		if(locationAnnotation != null) {
			context.register(new LocationHeaderFilter(mapper, locationAnnotation));
		}
	}

	@Priority(Priorities.HEADER_DECORATOR)
	private static final class LocationHeaderFilter implements ContainerResponseFilter {
		private final ObjectMapper mapper;
		private final Location locationAnnotation;

		public LocationHeaderFilter(ObjectMapper mapper, Location locationAnnotation) {
			this.mapper = mapper;
			this.locationAnnotation = locationAnnotation;
		}

		@Override
		public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
			if(response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
				response.getHeaders().putSingle(HttpHeaders.LOCATION, buildLocationURI(request, response));
		}

		private URI buildLocationURI(ContainerRequestContext request, ContainerResponseContext response) {
			if(isBlank(locationAnnotation.path()))
				return request.getUriInfo().getAbsolutePathBuilder().build();
			UriBuilder builder = locationAnnotation.isAbsolute()
				? request.getUriInfo().getAbsolutePathBuilder()
				: request.getUriInfo().getBaseUriBuilder();
			if(!isBlank(locationAnnotation.path()))
				builder.path(locationAnnotation.path());

			Arrays.stream(locationAnnotation.params())
				.filter(x -> x.type() == SourceParam.ParamType.QUERY)
				.forEach(param -> {
					if(param.source().type() == SourceParam.ParamType.PATH) {
						builder.queryParam(param.key(), getPathParam(param.source(), request));
					} else if(param.source().type() == SourceParam.ParamType.QUERY) {
						builder.queryParam(param.key(), getQueryParam(param.source(), request));
					} else if(param.source().type() == SourceParam.ParamType.JSON) {
						builder.queryParam(param.key(), getJsonParam(param.source(), response));
					}
				});

			return builder.buildFromMap(pathParams(request, response));
		}

		private Map<String, Object> pathParams(ContainerRequestContext request, ContainerResponseContext response) {
			final ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
			Arrays.stream(locationAnnotation.params())
				.filter(x -> x.type() == SourceParam.ParamType.PATH)
				.forEach(param -> {
					if(param.source().type() == SourceParam.ParamType.PATH) {
						builder.put(param.key(), getPathParam(param.source(), request));
					} else if(param.source().type() == SourceParam.ParamType.QUERY) {
						builder.put(param.key(), getQueryParam(param.source(), request));
					} else if(param.source().type() == SourceParam.ParamType.JSON) {
						builder.put(param.key(), getJsonParam(param.source(), response));
					}
				});
			return builder.build();
		}

		private static String getPathParam(SourceParam source, ContainerRequestContext request) {
			return defaultString(request.getUriInfo().getPathParameters().getFirst(source.key()), source.fallback());
		}

		private static String getQueryParam(SourceParam source, ContainerRequestContext request) {
			return defaultString(request.getUriInfo().getPathParameters().getFirst(source.key()), source.fallback());
		}

		private String getJsonParam(SourceParam source, ContainerResponseContext response) {
			final JsonNode node = mapper.valueToTree(response.getEntity());
			if(node == null || node.isNull())
				return source.fallback();
			final JsonNode valueNode = node.get(source.key());
			if(valueNode == null || valueNode.isNull())
				return source.fallback();
			return defaultString(valueNode.textValue(), source.fallback());
		}

	}
}
