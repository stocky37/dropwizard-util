package me.stockwells.util.cache;

import com.google.common.cache.Cache;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;


@ParametersAreNonnullByDefault
public class InvalidateCacheTask<K, V> extends CacheTask<K,V, Cache<K,V>> {

	public InvalidateCacheTask(Cache<K, V> cache) {
		this(Optional.<String>empty(), cache);
	}

	public InvalidateCacheTask(Optional<String> name, Cache<K, V> cache) {
		super(name.orElse("invalidate-cache"), cache);
	}

	@Override
	protected void actOnAll(Cache<K, V> cache) throws Exception {
		cache.invalidateAll();
	}

	@Override
	protected void act(Cache<K, V> cache, String key) throws Exception {
		cache.invalidate(key);
	}
}
