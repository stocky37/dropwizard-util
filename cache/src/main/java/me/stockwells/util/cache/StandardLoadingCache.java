package me.stockwells.util.cache;

import com.google.common.cache.AbstractLoadingCache;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.util.concurrent.Uninterruptibles;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@ParametersAreNonnullByDefault
public abstract class StandardLoadingCache<K extends Serializable, V extends Serializable>
	extends AbstractLoadingCache<K, V> {

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
			if(!value.isPresent()) {
				throw new CacheLoader.InvalidCacheLoadException("loader returned null");
			}
			put(key, value.get());
			return value.get();
		} catch(Exception e) {
			throw convertAndThrow(e);
		}
	}

	@Override
	public void refresh(K key) {
		try {
			put(key, Uninterruptibles.getUninterruptibly(loader.reload(key, get(key))));
		} catch(Exception e) {
			System.out.printf("Exception refreshing parseKey '%s': ", key);
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
			throw new ExecutionError((Error) t);
		}
	}
}
