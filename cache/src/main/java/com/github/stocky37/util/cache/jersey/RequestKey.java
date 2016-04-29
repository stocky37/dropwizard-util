package com.github.stocky37.util.cache.jersey;

import com.google.common.base.MoreObjects;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.ws.rs.core.MultivaluedMap;
import java.net.URI;
import java.security.Principal;
import java.util.Objects;


@ParametersAreNonnullByDefault
final class RequestKey {
	private final String method;
	private final URI uri;
	private final MultivaluedMap<String, String> queryParams;
	private final MultivaluedMap<String, String> headers;
	private final Principal principal;

	public RequestKey(
		String method,
		URI uri,
		MultivaluedMap<String, String> queryParams,
		MultivaluedMap<String, String> headers,
		@Nullable Principal principal
	) {
		this.method = method;
		this.uri = uri;
		this.queryParams = queryParams;
		this.headers = headers;
		this.principal = principal;
	}

	public String getMethod() {
		return method;
	}

	public URI getUri() {
		return uri;
	}

	public MultivaluedMap<String, String> getQueryParams() {
		return queryParams;
	}

	public MultivaluedMap<String, String> getHeaders() {
		return headers;
	}

	public Principal getPrincipal() {
		return principal;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("method", method)
			.add("uri", uri)
			.add("queryParams", queryParams)
			.add("headers", headers)
			.add("principal", principal)
			.toString();
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof RequestKey)) return false;
		final RequestKey that = (RequestKey)o;
		return Objects.equals(method, that.method) &&
			Objects.equals(uri, that.uri) &&
			Objects.equals(queryParams, that.queryParams) &&
			Objects.equals(headers, that.headers) &&
			Objects.equals(principal, that.principal);
	}

	@Override
	public int hashCode() {
		return Objects.hash(method, uri, queryParams, headers, principal);
	}
}
