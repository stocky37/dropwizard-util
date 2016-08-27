package com.github.stocky37.util.api.resources;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface Resources<T, I> {
	List<T> list();
	T create(@Valid @NotNull T obj);
	Resource<T> find(I id);
}
