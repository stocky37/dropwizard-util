package com.github.stocky37.util.cache.jersey;

import com.google.common.cache.Cache;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;


@Priority(Priorities.HEADER_DECORATOR - 1)
@ParametersAreNonnullByDefault
public class InvalidateCacheFilter implements ContainerResponseFilter {
	private final Map<String, Cache<RequestKey, Response>> caches;
	private final String cacheKey;

	public InvalidateCacheFilter(Map<String, Cache<RequestKey, Response>> caches, String cacheKey) {
		this.caches = caches;
		this.cacheKey = cacheKey;
	}

	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
		@Nullable final Cache<RequestKey, Response> cache = caches.get(cacheKey);
		if(cache != null && response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
			cache.asMap().keySet().stream()
				.filter(k -> keyMatches(k, request, response))
				.forEach(cache::invalidate);
		}
	}

	private boolean keyMatches(RequestKey key, ContainerRequestContext request, ContainerResponseContext response) {
		return key.getUri().equals(isBlank(response.getHeaderString(HttpHeaders.LOCATION))
			? request.getUriInfo().getAbsolutePath()
			: response.getLocation()
		);
	}
}
