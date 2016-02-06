package me.stockwells.util.jersey;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;


public class CreatedDynamicFeature implements DynamicFeature {

	private final ObjectMapper mapper;

	public CreatedDynamicFeature(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		CREATED annotation = resourceInfo.getResourceMethod().getAnnotation(CREATED.class);
		if(annotation != null){
			context.register(new JsonFieldCreatedFilter(annotation.value(), mapper));
		}
	}
}
