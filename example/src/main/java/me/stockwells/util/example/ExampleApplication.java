package me.stockwells.util.example;

import me.stockwells.util.cache.InvalidateCacheTask;
import me.stockwells.util.cache.LoadingCacheCacheLoader;
import me.stockwells.util.cache.ProxyCache;
import me.stockwells.util.cache.RefreshCacheTask;
import me.stockwells.util.cache.loaders.AsyncCacheLoader;
import me.stockwells.util.cache.redis.RedisCache;
import me.stockwells.util.jersey.CreatedDynamicFeature;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import redis.clients.jedis.JedisPool;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class ExampleApplication extends Application<Configuration> {

	public static void main(String[] args) throws Exception {
		new ExampleApplication().run(args);
	}

	@ParametersAreNonnullByDefault
	private static final class Loader extends AsyncCacheLoader<String, String> {

		public Loader(ListeningExecutorService service) {
			super(service);
		}

		@Override
		public String load(String key) throws Exception {
			System.out.println("Retrieving from loader...");
			for(int i = 0; i < 5; i++) {
				System.out.printf("%s...\n", i+1);
				Thread.sleep(1000);
			}
			return "default";
		}
	}


	@Override
	public void run(Configuration configuration, Environment environment) throws Exception {
		ListeningExecutorService primaryService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
		ListeningExecutorService secondaryService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

		LoadingCache<String, String> secondaryCache = new RedisCache(
			new Loader(secondaryService),
			new JedisPool("192.168.1.37"),
			Duration.ofMinutes(1)
		);

		LoadingCache<String, String> primaryCache = CacheBuilder.newBuilder()
			.refreshAfterWrite(20, TimeUnit.SECONDS)
			.build(new LoadingCacheCacheLoader<>(primaryService, secondaryCache));


		environment.jersey().register(new CreatedDynamicFeature(environment.getObjectMapper()));
		environment.jersey().register(new CreatedResourceImpl());
		environment.jersey().register(new CachedResource(new ProxyCache<>(primaryCache, secondaryCache)));

		environment.admin().addTask(new InvalidateCacheTask<String, String>(primaryCache) {
			@Override
			protected String key(@Nonnull String param) {
				return param;
			}
		});
		environment.admin().addTask(new RefreshCacheTask<String, String>(primaryCache) {
			@Override
			protected String key(@Nonnull String param) {
				return param;
			}
		});
	}
}
