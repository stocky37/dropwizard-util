package com.github.stocky37.util.cache.jersey;

import com.github.stocky37.util.jersey.VaryHeaderFeature;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class CachingBundle<T extends Configuration> implements ConfiguredBundle<T> {
	@Override
	public void initialize(Bootstrap<?> bootstrap) {}

	@Override
	public void run(T configuration, Environment environment) throws Exception {
		final CacheableFeature cacheableFeature = new CacheableFeature();
		environment.jersey().register(VaryHeaderFeature.class);
		environment.jersey().register(cacheableFeature);
		environment.jersey().register(new InvalidateCacheableFeature(cacheableFeature.getCaches()));
	}
}
