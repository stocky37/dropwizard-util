package com.github.stocky37.util.core.paging;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Link;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.Set;

public class PagingFeature implements DynamicFeature {
	public static final ThreadLocal<Integer> TOTAL_COUNT = new ThreadLocal<>();
	private static final String COUNT_HEADER = "X-Total-Count";

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		final PagingParam paging = pagingParam(resourceInfo.getResourceMethod());
		if(paging == null) {
			return;
		}

		context.register(new PagingFilter(new PageParamFactory(paging)));
	}

	private PagingParam pagingParam(Method method) {
		for(Parameter parameter : method.getParameters()) {
			if(parameter.isAnnotationPresent(PagingParam.class) && parameter.getType().isAssignableFrom(Page.class)) {
				return parameter.getAnnotation(PagingParam.class);
			}
		}
		return null;
	}

	@Priority(Priorities.HEADER_DECORATOR - 1)
	private static class PagingFilter implements ContainerResponseFilter {
		private static final Joiner LINK_JOINER = Joiner.on(",").skipNulls();
		private final PageParamFactory pageParamFactory;

		PagingFilter(PageParamFactory pageParamFactory) {
			this.pageParamFactory = pageParamFactory;
		}

		@Override
		public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
			final Optional<Integer> total = Optional.ofNullable(TOTAL_COUNT.get());
			if(total.isPresent()) {
				response.getHeaders().add(HttpHeaders.LINK, LINK_JOINER.join(buildLinks(request, total.get())));
				response.getHeaders().add(COUNT_HEADER, total.get());
				TOTAL_COUNT.remove();
			}
		}

		private Set<Link> buildLinks(ContainerRequestContext req, int total) {
			final Page page = pageParamFactory.build(req);
			final ImmutableSet.Builder<Link> builder = ImmutableSet.builder();

			builder.add(link(req, page.first(), "first"));
			builder.add(link(req, page.last(total), "last"));

			final Optional<Page> previous = page.previous();
			if(previous.isPresent())
				builder.add(link(req, previous.get(), "prev"));

			final Optional<Page> next = page.next(total);
			if(next.isPresent())
				builder.add(link(req, next.get(), "next"));

			return builder.build();
		}

		private Link link(ContainerRequestContext request, Page page, String rel) {
			return page == null ? null : Link.fromUriBuilder(request.getUriInfo().getAbsolutePathBuilder()
				.queryParam(pageParamFactory.getIndexParam(), page.index())
				.queryParam(pageParamFactory.getSizeParam(), page.size())
			).rel(rel).build();
		}
	}
}
