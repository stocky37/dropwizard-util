package com.github.stocky37.util.cache.jersey;

import com.google.common.cache.Cache;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.Response;
import java.lang.annotation.Annotation;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class InvalidateCacheableFeature implements DynamicFeature {
	private final Map<String, Cache<RequestKey, Response>> caches;

	public InvalidateCacheableFeature(Map<String, Cache<RequestKey, Response>> caches) {
		this.caches = caches;
	}

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		final InvalidateCacheable invalidation = getAnnotation(resourceInfo, InvalidateCacheable.class);
		if(invalidation != null) {
			context.register(new InvalidateCacheFilter(caches, invalidation.cache()));
		}
	}

	private static <T extends Annotation> T getAnnotation(ResourceInfo info, Class<T> annotation) {
		return info.getResourceMethod().getAnnotation(annotation);
	}
}
