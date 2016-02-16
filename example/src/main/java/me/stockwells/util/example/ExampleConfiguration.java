package me.stockwells.util.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class ExampleConfiguration extends Configuration {
	@Valid
	@NotNull
	@JsonProperty("swagger")
	private SwaggerBundleConfiguration swaggerBundleConfiguration = new SwaggerBundleConfiguration();

	public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
		return swaggerBundleConfiguration;
	}

	public void setSwaggerBundleConfiguration(SwaggerBundleConfiguration swaggerBundleConfiguration) {
		this.swaggerBundleConfiguration = swaggerBundleConfiguration;
	}
}
