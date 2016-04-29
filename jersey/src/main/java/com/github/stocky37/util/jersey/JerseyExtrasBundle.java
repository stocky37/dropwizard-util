package com.github.stocky37.util.jersey;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class JerseyExtrasBundle implements Bundle {
	@Override
	public void initialize(Bootstrap<?> bootstrap) {}

	@Override
	public void run(Environment environment) {
		environment.jersey().register(LocationFeature.class);
		environment.jersey().register(VaryHeaderFeature.class);
		environment.jersey().register(SuccessStatusFeature.class);
	}
}
