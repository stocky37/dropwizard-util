package com.github.stocky37.util.cache.jersey;

import com.github.stocky37.util.jersey.JerseyExtrasBundle;
import com.github.stocky37.util.jersey.Location;
import com.github.stocky37.util.jersey.LocationParam;
import com.github.stocky37.util.jersey.SourceParam;
import com.google.common.base.Throwables;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.jersey.caching.CacheControl;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public class TestApp extends Application<Configuration> {

	public static void main(String[] args) throws Exception {
		new TestApp().run(args);
	}

	private static final class TestJson {
		private String id;

		public TestJson(String id) {
			this.id = id;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}

	@Singleton
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public static final class TestResource {
		@GET
		@Path("{id}")
		@Cacheable(cache = "test")
		@CacheControl(maxAge = 60)
		public TestJson echo(@PathParam("id") String value) {
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				throw Throwables.propagate(e);
			}
			return new TestJson(value);
		}

		@DELETE
		@Path("{id}/delete")
		@InvalidateCacheable(cache = "test")
		@Location(path = "{id}", isAbsolute = true, params = {
			@LocationParam(key = "id", source = @SourceParam(key = "id"))
		})
		public TestJson echoTwo(@PathParam("id") String value) {
			return new TestJson(value);
		}
	}

	@Override
	public void initialize(Bootstrap<Configuration> bootstrap) {
		bootstrap.addBundle(new JerseyExtrasBundle());
		bootstrap.addBundle(new CachingBundle<>());
	}

	@Override
	public void run(Configuration configuration, Environment environment) throws Exception {
		environment.jersey().register(new TestResource());
	}
}
