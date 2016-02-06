package me.stockwells.util.example;

import me.stockwells.util.cache.InvalidateCacheTask;
import me.stockwells.util.cache.RedisCache;
import me.stockwells.util.cache.RefreshCacheTask;
import me.stockwells.util.cache.ser.SimpleSerializer;
import me.stockwells.util.jersey.CreatedDynamicFeature;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import redis.clients.jedis.JedisPool;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;


public class ExampleApplication extends Application<Configuration> {

	public static void main(String[] args) throws Exception {
		new ExampleApplication().run(args);
	}

	@ParametersAreNonnullByDefault
	private static final class Loader extends CacheLoader<String, String> {
		@Override
		public String load(String key) throws Exception {
			System.out.println("Retrieving from loader...");
			Thread.sleep(Duration.ofSeconds(5).toMillis());
			return "default";
		}
	}


	@Override
	public void run(Configuration configuration, Environment environment) throws Exception {
		LoadingCache<String, String> cache = new RedisCache<>(
			new JedisPool("192.168.1.37"),
			new Loader(),
			new SimpleSerializer<>(),
			new SimpleSerializer<>(),
			Duration.ofMinutes(5)
		);

		environment.jersey().register(new CreatedDynamicFeature(environment.getObjectMapper()));
		environment.jersey().register(new CreatedResourceImpl());
		environment.jersey().register(new CachedResource(cache));
		environment.admin().addTask(new InvalidateCacheTask<>(cache));
		environment.admin().addTask(new RefreshCacheTask<>(cache));
	}
}
