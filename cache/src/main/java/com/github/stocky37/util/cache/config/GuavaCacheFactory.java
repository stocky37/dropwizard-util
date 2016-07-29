package com.github.stocky37.util.cache.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;

@JsonTypeName("guava")
public class GuavaCacheFactory<K, V> implements CacheFactory<K, V> {
	private final CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder();

	@JsonProperty
	public GuavaCacheFactory recordStats(boolean recordStats) {
		if(recordStats) cacheBuilder.recordStats();
		return this;
	}

	@JsonProperty
	public GuavaCacheFactory initialCapacity(int initialCapacity) {
		cacheBuilder.initialCapacity(initialCapacity);
		return this;
	}

	@JsonProperty
	public GuavaCacheFactory concurrencyLevel(int concurrencyLevel) {
		cacheBuilder.concurrencyLevel(concurrencyLevel);
		return this;
	}

	@JsonProperty
	public GuavaCacheFactory maximumSize(long size) {
		cacheBuilder.maximumSize(size);
		return this;
	}

	@JsonProperty
	public GuavaCacheFactory maximumWeight(long weight) {
		cacheBuilder.maximumWeight(weight);
		return this;
	}

	@JsonProperty
	public GuavaCacheFactory weakKeys(boolean weakKeys) {
		if(weakKeys) cacheBuilder.weakKeys();
		return this;
	}

	@JsonProperty
	public GuavaCacheFactory weakValues(boolean weakValues) {
		if(weakValues) cacheBuilder.weakValues();
		return this;
	}

	@JsonProperty
	public GuavaCacheFactory softValues(boolean softValues) {
		if(softValues) cacheBuilder.softValues();
		return this;
	}

	@JsonProperty
	public GuavaCacheFactory expireAfterWrite(Duration duration) {
		cacheBuilder.expireAfterWrite(duration.getQuantity(), duration.getUnit());
		return this;
	}

	@JsonProperty
	public GuavaCacheFactory expireAfterAccess(Duration duration) {
		cacheBuilder.expireAfterAccess(duration.getQuantity(), duration.getUnit());
		return this;
	}

	@JsonIgnore
	public CacheBuilder<Object, Object> getCacheBuilder() {
		return cacheBuilder;
	}

	@Override
	public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
		return cacheBuilder.build();
	}
}
