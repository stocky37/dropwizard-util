package me.stockwells.util.example.resources;

import me.stockwells.util.example.api.CachedResource;
import me.stockwells.util.example.api.CreatedResource;
import me.stockwells.util.example.api.ExampleResource;
import me.stockwells.util.example.representations.Example;
import com.google.common.cache.LoadingCache;


public class ExampleResourceImpl implements ExampleResource {

	private final LoadingCache<String, String> cache;

	public ExampleResourceImpl(LoadingCache<String, String> cache) {
		this.cache = cache;
	}

	@Override
	public CachedResource cache() {
		return new CachedResourceImpl(cache);
	}

	@Override
	public CreatedResource created(String value) {
		return new CreatedResourceImpl(new Example(value));
	}
}
