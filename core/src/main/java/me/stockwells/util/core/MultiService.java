package me.stockwells.util.core;

import java.io.Serializable;
import java.util.Collection;

public interface MultiService<T, I extends Serializable> {
	Collection<T> all();
	T add(T obj);
	Service<T, ?> find(I id);
}
