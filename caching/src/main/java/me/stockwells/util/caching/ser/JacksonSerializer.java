package me.stockwells.util.caching.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import io.dropwizard.jackson.Jackson;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;


public class JacksonSerializer<T> implements Serializer<T> {

	private final ObjectMapper mapper;
	private final TypeReference<T> type;

	public JacksonSerializer(TypeReference<T> type) {
		this(type, Jackson.newObjectMapper());
	}

	public JacksonSerializer(TypeReference<T> type, ObjectMapper mapper) {
		this.type = type;
		this.mapper = mapper;
	}

	@Override
	public byte[] serialize(Object obj) {
		try {
			return obj == null ? null : SerializationUtils.serialize(mapper.writeValueAsString(obj));
		} catch(JsonProcessingException e) {
			throw Throwables.propagate(e);
		}
	}

	@Override
	public T deserialize(byte[] bytes) {
		try {
			return bytes == null ? null : mapper.readValue(SerializationUtils.<String>deserialize(bytes), type);
		} catch(IOException e) {
			throw Throwables.propagate(e);
		}
	}
}
