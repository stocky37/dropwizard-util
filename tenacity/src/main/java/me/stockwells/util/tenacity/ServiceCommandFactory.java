package me.stockwells.util.tenacity;

import com.netflix.hystrix.HystrixCommand;

import java.util.List;
import java.util.Optional;

public interface ServiceCommandFactory<T, U, I> {
	HystrixCommand<List<T>> list();
	HystrixCommand<T> create(T obj);
	HystrixCommand<Optional<T>> find(I id);
	HystrixCommand<Optional<T>> update(I id, U updated);
	HystrixCommand<Optional<T>> delete(I id);
}
