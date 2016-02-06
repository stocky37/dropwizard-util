package me.stockwells.util.caching;

import me.stockwells.util.caching.ser.Serializer;
import com.google.common.cache.AbstractLoadingCache;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.UncheckedExecutionException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class RedisCache<K, V> extends AbstractLoadingCache<K, V> implements Serializable {

	private final JedisPool pool;
	private final CacheLoader<K, V> loader;
	private final Serializer<K> keySerializer;
	private final Serializer<V> valueSerializer;
	private final Duration expiry;

	public RedisCache(JedisPool pool, CacheLoader<K, V> loader, Serializer<K> keySerializer, Serializer<V> valueSerializer, Duration expiry) {
		this.pool = pool;
		this.loader = loader;
		this.keySerializer = keySerializer;
		this.valueSerializer = valueSerializer;
		this.expiry = expiry;
	}

	@Override
	public V get(@Nonnull K key) throws ExecutionException {
		return get(key, () -> loader.load(key));
	}

	@Override
	public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
		Optional<V> value = Optional.ofNullable(this.getIfPresent(key));
		if(value.isPresent()) {
			return value.get();
		}

		try {
			value = Optional.ofNullable(valueLoader.call());
			put(key, value.orElseThrow(() -> new CacheLoader.InvalidCacheLoadException("loader returned null")));
		} catch(Exception e) {
			throw convertAndThrow(e);
		}

		return value.get();
	}

	@Nullable
	@Override
	public V getIfPresent(@Nonnull Object key) {
		try(Jedis jedis = pool.getResource()) {
			return valueSerializer.deserialize(
				jedis.get(keySerializer.serialize(key))
			);
		}
	}

	@Override
	public void put(K key, V value) {
		byte[] keyBytes = keySerializer.serialize(key);
		byte[] valueBytes = valueSerializer.serialize(value);
		try(Jedis jedis = pool.getResource()) {
			jedis.setex(keyBytes, (int)expiry.getSeconds(), valueBytes);
		}
	}

	@Override
	public void refresh(K key) {
		try {
			put(key, loader.load(key));
		} catch(Exception e) {
			System.out.printf("Exception refreshing key '%s': ", key);
		}
	}

	@Override
	public void invalidate(Object key) {
		try(Jedis jedis = pool.getResource()) {
			jedis.del(keySerializer.serialize(key));
		}
	}

	@Override
	public void invalidateAll(Iterable<?> keys) {
		try(Jedis jedis = pool.getResource()) {
			jedis.del(Iterables.toArray(StreamSupport.stream(keys.spliterator(), false)
				.map(keySerializer::serialize)
				.collect(Collectors.toSet()), byte[].class));
		}
	}

	private static ExecutionException convertAndThrow(Throwable t) throws ExecutionException {
		if (t instanceof InterruptedException) {
			Thread.currentThread().interrupt();
			throw new ExecutionException(t);
		} else if (t instanceof RuntimeException) {
			throw new UncheckedExecutionException(t);
		} else if (t instanceof Exception) {
			throw new ExecutionException(t);
		} else {
			throw new ExecutionError((Error) t);
		}
	}
}
