package com.github.stocky37.util.paging;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class PagingBundle implements Bundle {
	@Override
	public void initialize(Bootstrap<?> bootstrap) {}

	@Override
	public void run(Environment environment) {
		environment.jersey().register(PageValueFactoryProvider.class);
		environment.jersey().register(PagingFeature.class);
	}
}
