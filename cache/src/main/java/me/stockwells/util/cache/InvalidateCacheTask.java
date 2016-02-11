package me.stockwells.util.cache;

import com.google.common.cache.Cache;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMultimap;
import io.dropwizard.servlets.tasks.Task;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.PrintWriter;


@ParametersAreNonnullByDefault
public abstract class InvalidateCacheTask<K, V> extends Task {
	public static final String KEY_PARAM = "keys";

	private final Cache<K, V> cache;

	public InvalidateCacheTask(Cache<K, V> cache) {
		super("invalidate-cache");
		this.cache = cache;
	}

	public InvalidateCacheTask(Cache<K, V> cache, String name) {
		super(name);
		this.cache = cache;
	}

	@Override
	public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
		if(parameters.containsKey(KEY_PARAM)) {
			cache.invalidateAll(Collections2.transform(parameters.get(KEY_PARAM), this::key));
		} else {
			cache.invalidateAll();
		}
	}

	protected abstract K key(String param);
}
