package com.github.stocky37.util.cache.jersey;

import com.github.stocky37.util.jersey.Vary;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.Response;
import java.lang.annotation.Annotation;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@ParametersAreNonnullByDefault
public class CacheableFeature implements DynamicFeature {

	private final Map<String, Cache<RequestKey, Response>> caches;

	public CacheableFeature() {
		this(Maps.newHashMap());
	}

	public CacheableFeature(Map<String, Cache<RequestKey, Response>> caches) {
		this.caches = Maps.newHashMap(caches);
	}

	public Map<String, Cache<RequestKey, Response>> getCaches() {
		return Collections.unmodifiableMap(caches);
	}

	@Override
	public void configure(ResourceInfo info, FeatureContext context) {
		final Cacheable cacheable = getAnnotation(info, Cacheable.class);
		if(cacheable == null)
			return;

		final Vary varyAnnotation = getAnnotation(info, Vary.class);
		final CacheControl cacheControl = getCacheControl(
			getAnnotation(info, io.dropwizard.jersey.caching.CacheControl.class)
		);

		if(shouldCache(cacheControl)) {
			context.register(new CachingFilter(
				getCache(cacheable.cache(), cacheControl),
				cacheControl,
				getVaryHeaders(varyAnnotation))
			);
		}
	}

	private static <T extends Annotation> T getAnnotation(ResourceInfo info, Class<T> annotation) {
		return info.getResourceMethod().getAnnotation(annotation);
	}

	private Cache<RequestKey, Response> getCache(String key, CacheControl cacheControl) {
		caches.computeIfAbsent(key, s -> {
			final CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
			final int expiry = getExpiry(cacheControl);
			if(expiry > 0) {
				builder.expireAfterWrite(expiry, TimeUnit.SECONDS);
			}
			return builder.build();
		});
		return caches.get(key);
	}

	private static Collection<String> getVaryHeaders(@Nullable Vary vary) {
		return vary == null ? ImmutableSet.of() : ImmutableSet.copyOf(vary.value());
	}

	private static int getExpiry(CacheControl cacheControl) {
		if(cacheControl.isPrivate()) {
			return cacheControl.getMaxAge();
		}
		return cacheControl.getSMaxAge() == -1 ? cacheControl.getMaxAge() : cacheControl.getSMaxAge();
	}

	private static boolean shouldCache(CacheControl cacheControl) {
		return !(cacheControl.isNoCache()
			|| cacheControl.isNoStore()
			|| getExpiry(cacheControl) == 0);
	}

	private static CacheControl getCacheControl(@Nullable io.dropwizard.jersey.caching.CacheControl control) {
		final javax.ws.rs.core.CacheControl cacheControl = new javax.ws.rs.core.CacheControl();
		if(control != null) {
			cacheControl.setPrivate(control.isPrivate());
			cacheControl.setNoCache(control.noCache());
			cacheControl.setNoStore(control.noStore());
			cacheControl.setNoTransform(control.noTransform());
			cacheControl.setMustRevalidate(control.mustRevalidate());
			cacheControl.setProxyRevalidate(control.proxyRevalidate());
			cacheControl.setMaxAge((int) control.maxAgeUnit().toSeconds(control.maxAge()));
			cacheControl.setSMaxAge((int) control.sharedMaxAgeUnit()
				.toSeconds(control.sharedMaxAge()));
			if(control.immutable())
				cacheControl.setMaxAge((int) Duration.of(1, ChronoUnit.YEARS).getSeconds());
		}
		return cacheControl;
	}
}
