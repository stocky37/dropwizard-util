package me.stockwells.util.cache.redis;

import me.stockwells.util.cache.StandardLoadingCache;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.SerializationUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.Serializable;
import java.time.Duration;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@ParametersAreNonnullByDefault
public class BinaryRedisCache<K extends Serializable, V extends Serializable> extends StandardLoadingCache<K, V> {

	private final JedisPool pool;
	@Nullable private final Duration expiry;

	public BinaryRedisCache(CacheLoader<K, V> loader, JedisPool pool, @Nullable Duration expiry) {
		super(loader);
		this.pool = pool;
		this.expiry = expiry;
	}

	@Nullable
	@Override
	public V getIfPresent(Object key) {
		try(Jedis jedis = pool.getResource()) {
			return SerializationUtils.deserialize(
				jedis.get(SerializationUtils.serialize((Serializable)key))
			);
		}
	}

	@Override
	public void put(K key, V value) {
		try(Jedis jedis = pool.getResource()) {
			final byte[] keyBytes = SerializationUtils.serialize(key);
			final byte[] valueBytes = SerializationUtils.serialize(value);
			if(expiry == null) {
				jedis.set(keyBytes, valueBytes);
			} else {
				jedis.setex(keyBytes, (int)expiry.getSeconds(), valueBytes);
			}
		}
	}

	@Override
	public void invalidate(Object key) {
		try(Jedis jedis = pool.getResource()) {
			jedis.del(SerializationUtils.serialize((Serializable)key));
		}
	}

	@Override
	public void invalidateAll(Iterable<?> keys) {
		try(Jedis jedis = pool.getResource()) {
			jedis.del(Iterables.toArray(StreamSupport.stream(keys.spliterator(), false)
				.map(k -> SerializationUtils.serialize((Serializable)k))
				.collect(Collectors.toSet()), byte[].class));
		}
	}
}
