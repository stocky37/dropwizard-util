package com.github.stocky37.util.testing;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

public class ValidationRule<T> extends RepresentationRule<T> {
	private final Validator validator;

	public ValidationRule(Class<T> type, Object target) {
		this(type, target, Validation.buildDefaultValidatorFactory().getValidator());
	}

	public ValidationRule(Class<T> type, Object target, Validator validator) {
		super(type, target);
		this.validator = validator;
	}

	public Validator getValidator() {
		return validator;
	}

	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				final ValidationVerification config = description.getAnnotation(ValidationVerification.class);
				if(config != null) {
					final T representation = getRepresentation(config.representation());
					if(config.isValid()) {
						checkValid(representation);
					} else {
						checkInvalid(representation);
					}
				}
				base.evaluate();
			}
		};
	}

	private void checkValid(T representation) {
		final Set<ConstraintViolation<T>> violations = validator.validate(representation);
		assertThat(violationsMessage(violations), violations, empty());
	}

	private void checkInvalid(T representation) {
		final Set<ConstraintViolation<T>> violations = validator.validate(representation);
		assertThat("", violations, not(empty()));
	}

	private String violationsMessage(Collection<ConstraintViolation<T>> violations) {
		return String.join("\n", violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet()));
	}
}
