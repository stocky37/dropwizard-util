package com.github.stocky37.util.tenacity;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.netflix.hystrix.HystrixCommand;

import java.util.List;
import java.util.Optional;

public interface ServiceCommandFactory<T, I> {
	HystrixCommand<List<T>> list();
	HystrixCommand<T> create(T obj);
	HystrixCommand<Optional<T>> find(I id);
	HystrixCommand<Optional<T>> update(I id, JsonMergePatch patch);
	HystrixCommand<Optional<T>> delete(I id);
}
