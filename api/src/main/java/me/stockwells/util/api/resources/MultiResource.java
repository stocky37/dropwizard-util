package me.stockwells.util.api.resources;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Collection;

public interface MultiResource<T, I extends Serializable> {
	@Valid Collection<T> all();
	@Valid T add(T obj);
	Resource<T, ?> find(I id);
}
