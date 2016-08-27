package com.github.stocky37.util.core;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Service<T, I> {
	List<T> list();
	T create(T obj);
	Optional<T> find(I id);
	Optional<T> delete(I id);
	Optional<T> update(I id, JsonMergePatch patch);
}
