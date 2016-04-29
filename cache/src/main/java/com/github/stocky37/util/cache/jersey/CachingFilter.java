package com.github.stocky37.util.cache.jersey;

import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableSet;
import org.glassfish.jersey.internal.util.collection.StringKeyIgnoreCaseMultivaluedMap;
import org.glassfish.jersey.message.internal.OutboundJaxrsResponse;
import org.glassfish.jersey.message.internal.OutboundMessageContext;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collection;


@Priority(Priorities.HEADER_DECORATOR + 1)
@ParametersAreNonnullByDefault
public class CachingFilter implements ContainerRequestFilter, ContainerResponseFilter {
	private final Cache<RequestKey, Response> cache;
	private final CacheControl cacheControl;
	private final Collection<String> varyHeaders;

	public CachingFilter(Cache<RequestKey, Response> cache, CacheControl cacheControl, Collection<String> varyHeaders) {
		this.cache = cache;
		this.cacheControl = cacheControl;
		this.varyHeaders = ImmutableSet.copyOf(varyHeaders);
	}

	@Override
	public void filter(ContainerRequestContext request) throws IOException {
		@Nullable final Response cachedResponse = cache.getIfPresent(key(request));
		if(cachedResponse != null)
			request.abortWith(OutboundJaxrsResponse.fromResponse(cachedResponse).build());
	}

	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
		if(response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
			cache.asMap().putIfAbsent(key(request), value(response));
	}

	private RequestKey key(ContainerRequestContext request) {
		return new RequestKey(
			request.getMethod(),
			request.getUriInfo().getAbsolutePath(),
			request.getUriInfo().getQueryParameters(),
			getHeaders(request),
			cacheControl.isPrivate() ? request.getSecurityContext().getUserPrincipal() : null
		);
	}

	private MultivaluedMap<String, String> getHeaders(ContainerRequestContext request) {
		MultivaluedMap<String, String> headers = new StringKeyIgnoreCaseMultivaluedMap<>();
		varyHeaders.forEach(h -> headers.addAll(h, request.getHeaders().get(h)));
		return headers;
	}

	private static Response value(ContainerResponseContext response) {
		final Response.StatusType status = response.getStatusInfo();
		final OutboundMessageContext context = new OutboundMessageContext();
		context.getHeaders().putAll(response.getHeaders());
		context.setEntity(response.getEntity());
		return new OutboundJaxrsResponse(status, context);
	}
}
