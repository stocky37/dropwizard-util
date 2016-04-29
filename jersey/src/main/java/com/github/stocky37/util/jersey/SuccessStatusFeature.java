package com.github.stocky37.util.jersey;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.annotation.Annotation;

import static com.google.common.base.Preconditions.checkArgument;


public class SuccessStatusFeature implements DynamicFeature {

	@Override
	public void configure(ResourceInfo info, FeatureContext context) {
		Status status = getStatusAnnotation(info);
		if(status != null)
			context.register(new SuccessStatusFilter(status.value()));
	}

	private Status getStatusAnnotation(ResourceInfo info) {
		for(Annotation annotation : info.getResourceMethod().getAnnotations()) {
			final Status statusAnnotation = annotation.getClass().getAnnotation(Status.class);
			if(statusAnnotation != null)
				return statusAnnotation;
		}
		return null;
	}

	@Priority(Priorities.USER)
	private static final class SuccessStatusFilter implements ContainerResponseFilter {
		private final Response.Status status;

		public SuccessStatusFilter(Response.Status status) {
			checkArgument(status.getFamily() == Response.Status.Family.SUCCESSFUL);
			this.status = status;
		}

		@Override
		public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
			if(response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
				response.setStatusInfo(status);
			}
		}
	}
}
