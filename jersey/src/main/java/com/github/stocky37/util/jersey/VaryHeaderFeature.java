package com.github.stocky37.util.jersey;

import com.google.common.collect.ImmutableList;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.List;


public class VaryHeaderFeature implements DynamicFeature {
	@Override
	public void configure(ResourceInfo info, FeatureContext context) {
		final Vary vary = info.getResourceMethod().getAnnotation(Vary.class);
		if(vary != null) {
			context.register(new VaryHeaderFilter(vary.value()));
		}
	}

	@Priority(Priorities.HEADER_DECORATOR)
	private static final class VaryHeaderFilter implements ContainerResponseFilter {
		private final List<Object> vary;

		VaryHeaderFilter(String... vary) {
			this.vary = ImmutableList.copyOf(vary);
		}

		@Override
		public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
			response.getHeaders().put(HttpHeaders.VARY, vary);
		}
	}
}
