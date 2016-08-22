package com.github.stocky37.util.api.resources;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

public interface Resources<T, I> {
	@Valid List<T> list();
	@Valid T create(T obj);
	Resource<T> find(@NotNull I id);
}
