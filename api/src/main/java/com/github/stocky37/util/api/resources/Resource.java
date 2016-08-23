package com.github.stocky37.util.api.resources;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface Resource<T> {
	Optional<T> get();
	Optional<T> update(JsonMergePatch patch);
	Optional<T> delete();
}
