package me.stockwells.util.cache;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMultimap;
import io.dropwizard.servlets.tasks.Task;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.PrintWriter;


@ParametersAreNonnullByDefault
public abstract class RefreshCacheTask<K, V> extends Task {
	public static final String KEY_PARAM = "keys";

	private final LoadingCache<K, V> cache;

	public RefreshCacheTask(LoadingCache<K, V> cache) {
		super("refresh-cache");
		this.cache = cache;
	}

	public RefreshCacheTask(LoadingCache<K, V> cache, String name) {
		super(name);
		this.cache = cache;
	}

	@Override
	public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
		parameters.get(KEY_PARAM).forEach(p -> cache.refresh(parseKey(p)));
	}

	protected abstract K parseKey(String param);
}
