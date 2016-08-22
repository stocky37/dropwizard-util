package com.github.stocky37.util.db;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface ResourceDAO<E, I extends Serializable> {
	List<E> listAll();
	E create(E entity);
	Optional<E> find(I id);
	Optional<E> delete(I id);
	E update(E updated);
}
