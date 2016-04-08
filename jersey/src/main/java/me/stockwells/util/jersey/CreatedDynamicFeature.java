package me.stockwells.util.jersey;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.springframework.core.annotation.AnnotationUtils;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;


public class CreatedDynamicFeature implements DynamicFeature {

	private final ObjectMapper mapper;

	public CreatedDynamicFeature() {
		this(Jackson.newObjectMapper());
	}

	public CreatedDynamicFeature(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		Created annotation = AnnotationUtils.findAnnotation(resourceInfo.getResourceMethod(), Created.class);
		if(annotation != null){
			context.register(new JsonCreatedFilter(annotation.value(), mapper));
		}
	}
}
