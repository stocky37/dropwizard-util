package me.stockwells.util.cache.redis;

import com.google.common.cache.AbstractLoadingCache;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.UncheckedExecutionException;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;


public abstract class StandardLoadingCache<K extends Serializable, V extends Serializable> extends AbstractLoadingCache<K, V>  {

	private final CacheLoader<K, V> loader;

	public StandardLoadingCache(CacheLoader<K, V> loader) {
		this.loader = loader;
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

	@Override
	public void refresh(K key) {
		try {
			put(key, loader.load(key));
		} catch(Exception e) {
			System.out.printf("Exception refreshing key '%s': ", key);
		}
	}

	private static ExecutionException convertAndThrow(Throwable t) throws ExecutionException {
		if(t instanceof InterruptedException) {
			Thread.currentThread().interrupt();
			throw new ExecutionException(t);
		} else if(t instanceof RuntimeException) {
			throw new UncheckedExecutionException(t);
		} else if(t instanceof Exception) {
			throw new ExecutionException(t);
		} else {
			throw new ExecutionError((Error)t);
		}
	}
}
