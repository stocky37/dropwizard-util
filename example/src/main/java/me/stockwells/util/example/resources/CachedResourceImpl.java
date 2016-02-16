package me.stockwells.util.example.resources;

import me.stockwells.util.example.api.CachedResource;
import com.google.common.base.Throwables;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;

public class CachedResourceImpl implements CachedResource {
	private final LoadingCache<String, String> cache;

	public CachedResourceImpl(LoadingCache<String, String> cache) {
		this.cache = cache;
	}

	@Override
	public void get(String key, String value) {
		cache.put(key, value);
	}

	@Override
	public String get(String key) {
		try {
			return cache.get(key);
		} catch(ExecutionException e) {
			throw Throwables.propagate(e);
		}
	}
}
