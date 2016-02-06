package me.stockwells.util.caching.ser;

import javax.annotation.ParametersAreNullableByDefault;


@ParametersAreNullableByDefault
public interface Serializer<T> {
	byte[] serialize(Object obj);
	T deserialize(byte[] bytes);
}
