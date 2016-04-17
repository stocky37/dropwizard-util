package com.github.stocky37.util.cache.redis;

import com.github.stocky37.util.cache.StandardLoadingCache;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Iterables;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ParametersAreNonnullByDefault
public class RedisCache extends StandardLoadingCache<String, String> {

	private final JedisPool pool;
	@Nullable private final Duration expiry;

	public RedisCache(CacheLoader<String, String> loader, JedisPool pool, @Nullable Duration expiry) {
		super(loader);
		this.pool = pool;
		this.expiry = expiry;
	}

	@Nullable
	@Override
	public String getIfPresent(@Nonnull Object key) {
		try(Jedis jedis = pool.getResource()) {
			return jedis.get(Objects.toString(key));
		}
	}

	@Override
	public void put(String key, String value) {
		try(Jedis jedis = pool.getResource()) {
			if(expiry == null) {
				jedis.set(key, value);
			} else {
				jedis.setex(key, (int)expiry.getSeconds(), value);
			}
		}
	}

	@Override
	public void invalidate(Object key) {
		try(Jedis jedis = pool.getResource()) {
			jedis.del(Objects.toString(key));
		}
	}

	@Override
	public void invalidateAll(Iterable<?> keys) {
		try(Jedis jedis = pool.getResource()) {
			jedis.del(Iterables.toArray(StreamSupport.stream(keys.spliterator(), false)
				.map(Objects::toString)
				.collect(Collectors.toSet()), String.class));
		}
	}
}
