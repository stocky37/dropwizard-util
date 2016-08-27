package com.github.stocky37.util.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.google.common.base.Throwables;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.util.Generics;

import javax.annotation.Nullable;

public abstract class AbstractService<T, I> implements Service<T, I> {
	private final ObjectMapper mapper;
	private final Class<?> type;

	public AbstractService() {
		this(Jackson.newObjectMapper());
	}

	public AbstractService(ObjectMapper mapper) {
		this.mapper = mapper;
		this.type = Generics.getTypeParameter(getClass());
	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	@SuppressWarnings("unchecked")
	public Class<T> getType() {
		return (Class<T>)type;
	}

	@Nullable
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	protected T patch(@Nullable T existing, JsonMergePatch patch) throws JsonPatchException {
		return existing == null ? null : fromNode(patch(patch, mapper.valueToTree(existing)));
	}

	private JsonNode patch(JsonMergePatch patch, JsonNode node) {
		try {
			return patch.apply(node);
		} catch(JsonPatchException e) {
			throw Throwables.propagate(e);
		}
	}

	private T fromNode(JsonNode node) {
		try {
			return mapper.treeToValue(node, getType());
		} catch(JsonProcessingException e) {
			throw Throwables.propagate(e);
		}
	}
}
