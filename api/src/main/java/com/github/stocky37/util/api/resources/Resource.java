package com.github.stocky37.util.api.resources;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface Resource<T> {
	@Valid Optional<T> get();
	@Valid Optional<T> update(@NotNull JsonMergePatch patch);
	@Valid Optional<T> delete();
}
