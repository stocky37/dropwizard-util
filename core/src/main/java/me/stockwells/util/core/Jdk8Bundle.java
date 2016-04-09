package me.stockwells.util.core;

import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.dropwizard.Bundle;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class Jdk8Bundle implements Bundle {
	@Override
	public void initialize(Bootstrap<?> bootstrap) {
		bootstrap.getObjectMapper().registerModule(new ParameterNamesModule());
		bootstrap.addBundle(new Java8Bundle());
	}


	public void run(Environment environment) {}
}
