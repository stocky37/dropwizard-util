package me.stockwells.util.cache;

import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableMultimap;
import io.dropwizard.servlets.tasks.Task;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.PrintWriter;

@ParametersAreNonnullByDefault
public abstract class CacheTask<K, V, C extends Cache<K,V>> extends Task {

	public static final String KEY_PROPERTY = "key";
	private final C cache;

	public CacheTask(String name, C cache) {
		super(name);
		this.cache = cache;
	}

	@Override
	public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
		if(parameters.containsKey(KEY_PROPERTY)) {
			execute(cache, parameters.get(KEY_PROPERTY));
		} else {
			execute(cache);
		}
	}

	protected abstract void execute(C cache) throws Exception;
	protected abstract void execute(C cache, Iterable<String> keys) throws Exception;
}
