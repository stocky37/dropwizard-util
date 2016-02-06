package me.stockwells.util.cache.ser;

import org.apache.commons.lang3.SerializationUtils;

import javax.annotation.ParametersAreNullableByDefault;
import java.io.Serializable;

@ParametersAreNullableByDefault
public class SimpleSerializer<T extends Serializable> implements Serializer<T> {
	@Override
	public byte[] serialize(Object obj) {
		return obj == null ? null : SerializationUtils.serialize((Serializable)obj);
	}

	@Override
	public T deserialize(byte[] bytes) {
		return bytes == null ? null : SerializationUtils.deserialize(bytes);
	}
}
