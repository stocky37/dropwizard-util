package com.github.stocky37.util.core.paging;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

public class Tmp extends Application<Configuration> {

	public static void main(String[] args) throws Exception {
		new Tmp().run(args);
	}

	@Override
	public void initialize(Bootstrap<Configuration> bootstrap) {
		bootstrap.addBundle(new PagingBundle());
	}

	@Override
	public void run(Configuration configuration, Environment environment) throws Exception {
		environment.jersey().register(TmpResource.class);
	}

	@Path("/")
	public static final class TmpResource {
		private final HttpServletResponse response;

		public TmpResource(@Context HttpServletResponse response) {
			this.response = response;
		}

		@GET
		@Path("test")
		@Produces(MediaType.APPLICATION_JSON)
		public String test(@PagingParam Page page) {
			final String entity = String.format("{\"page\": \"%s\", \"size\": \"%s\"}", page.index(), page.size());
//			response.addHeader(PagingFeature.COUNT_HEADER, String.valueOf(100));
			PagingFeature.TOTAL_COUNT.set(100);
			return entity;
		}
	}
}
