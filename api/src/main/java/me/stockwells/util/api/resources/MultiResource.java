package me.stockwells.util.api.resources;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Collection;

public interface MultiResource<T, I> {
	@Valid Collection<T> list();
	@Valid T create(T obj);
	Resource<T, ?> find(I id);
}
