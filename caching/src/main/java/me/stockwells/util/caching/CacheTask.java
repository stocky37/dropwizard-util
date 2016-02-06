package me.stockwells.util.caching;

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
			act(cache, parameters.get(KEY_PROPERTY).asList().get(0));
		} else {
			actOnAll(cache);
		}
	}

	protected abstract void actOnAll(C cache) throws Exception;
	protected abstract void act(C cache, String key) throws Exception;
}
