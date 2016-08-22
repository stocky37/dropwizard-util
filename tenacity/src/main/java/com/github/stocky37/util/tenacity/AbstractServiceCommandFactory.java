package com.github.stocky37.util.tenacity;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.github.stocky37.util.core.Service;
import com.github.stocky37.util.tenacity.commands.ServiceCreateCommand;
import com.github.stocky37.util.tenacity.commands.ServiceDeleteCommand;
import com.github.stocky37.util.tenacity.commands.ServiceFindCommand;
import com.github.stocky37.util.tenacity.commands.ServiceListCommand;
import com.github.stocky37.util.tenacity.commands.ServiceUpdateCommand;
import com.netflix.hystrix.HystrixCommand;
import com.yammer.tenacity.core.properties.TenacityPropertyKey;

import java.util.List;
import java.util.Optional;

public abstract class AbstractServiceCommandFactory<T, I> implements ServiceCommandFactory<T, I> {

	private final Service<T, I> delegate;

	public AbstractServiceCommandFactory(Service<T, I> delegate) {
		this.delegate = delegate;
	}

	public Service<T, I> getDelegate() {
		return delegate;
	}

	@Override
	public HystrixCommand<List<T>> list() {
		return new ServiceListCommand<>(getListKey(), delegate);
	}

	@Override
	public HystrixCommand<T> create(T obj) {
		return new ServiceCreateCommand<>(getCreateKey(), delegate, obj);
	}

	@Override
	public HystrixCommand<Optional<T>> find(I id) {
		return new ServiceFindCommand<>(getFindKey(), delegate, id);
	}

	@Override
	public HystrixCommand<Optional<T>> update(I id, JsonMergePatch patch) {
		return new ServiceUpdateCommand<>(getUpdateKey(), delegate, id, patch);
	}

	@Override
	public HystrixCommand<Optional<T>> delete(I id) {
		return new ServiceDeleteCommand<>(getDeleteKey(), delegate, id);
	}

	protected abstract TenacityPropertyKey getListKey();
	protected abstract TenacityPropertyKey getCreateKey();
	protected abstract TenacityPropertyKey getFindKey();
	protected abstract TenacityPropertyKey getUpdateKey();
	protected abstract TenacityPropertyKey getDeleteKey();
}
