package com.github.stocky37.util.core;

import com.github.stocky37.util.api.resources.Resource;

public interface ResourceFactory<T, I> {
	Resource<T> build(I id);
}
