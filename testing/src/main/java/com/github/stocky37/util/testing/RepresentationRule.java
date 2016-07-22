package com.github.stocky37.util.testing;

import com.google.common.base.Throwables;
import org.junit.rules.TestRule;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class RepresentationRule<T> implements TestRule {
	private final Class<T> type;
	private final Object target;

	public RepresentationRule(Class<T> type, Object target) {
		this.type = type;
		this.target = target;
	}

	public Class<T> getType() {
		return type;
	}

	public Object getTarget() {
		return target;
	}

	protected T getRepresentation(final String name) {
		return findRepresentationInFields(name)
			.orElseGet(() -> findRepresentationInMethods(name)
			.orElseThrow(() -> new UnsupportedOperationException("No representation found with name " + name)));
	}

	@SuppressWarnings("unchecked")
	private Optional<T> findRepresentationInFields(final String name) {
		final Optional<Field> conformingField = Arrays.stream(target.getClass().getFields())
			.filter(f -> fieldConformsToSignature(f) && fieldMatches(f, name))
			.findAny();

		try {
			return Optional.ofNullable(conformingField.isPresent() ? (T)conformingField.get().get(target) : null);
		} catch(IllegalAccessException e) {
			throw Throwables.propagate(e);
		}
	}

	@SuppressWarnings("unchecked")
	private Optional<T> findRepresentationInMethods(final String name) {
		final Optional<Method> conformingMethod = Arrays.stream(target.getClass().getMethods())
			.filter(m -> methodConformsToSignature(m) && methodMatches(m, name))
			.findAny();

		try {
			return Optional.ofNullable(conformingMethod.isPresent() ? (T)conformingMethod.get().invoke(target) : null);
		} catch(InvocationTargetException | IllegalAccessException e) {
			throw Throwables.propagate(e);
		}
	}

	private boolean methodConformsToSignature(final Method m) {
		final Representation r = m.getAnnotation(Representation.class);
		boolean conforms = r != null
			&& m.getReturnType().isAssignableFrom(type)
			&& m.getParameterTypes().length == 0;
		if(!conforms && r != null) {
			throw new UnsupportedOperationException("Method " + m.getName() +
				" does not conform required method signature 'public T xxx()'");
		}
		return conforms;
	}

	private boolean fieldConformsToSignature(final Field f) {
		final Representation r = f.getAnnotation(Representation.class);
		boolean conforms = r != null && f.getType().isAssignableFrom(type);
		if(!conforms && r != null) {
			throw new UnsupportedOperationException("Field " + f.getName() + " is not of type " + type.getName());
		}
		return conforms;
	}

	private boolean methodMatches(Method m, String name) {
		if(isBlank(name)) return true;
		final Representation r = m.getAnnotation(Representation.class);
		return isBlank(r.value())
			? Objects.equals(m.getName(), name)
			: Objects.equals(r.value(), name);
	}

	private boolean fieldMatches(Field f, String name) {
		if(isBlank(name)) return true;
		final Representation r = f.getAnnotation(Representation.class);
		return isBlank(r.value())
			? Objects.equals(f.getName(), name)
			: Objects.equals(r.value(), name);
	}
}
