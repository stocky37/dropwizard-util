package com.github.stocky37.util.testing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.common.base.Throwables;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.apache.commons.io.FilenameUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

public abstract class RepresentationTest<T> {
	private final ObjectMapper mapper = objectMapper();

	protected abstract TypeReference<T> type();
	protected abstract Collection<Fixture<T>> fixtures();

	@Test
	public void equalsContract() {
		equalsVerifier().verify();
	}

	@Test
	public void deserialisation() throws IOException {
		fixtures().stream()
			.filter(f -> f.getSerialisationMode() != SerialisationMode.SERIALISE)
			.forEach(this::assertDeserialisation);
	}

	@Test
	public void serialisation() throws JsonProcessingException {
		fixtures().stream()
			.filter(f -> f.getSerialisationMode() != SerialisationMode.DESERIALISE)
			.forEach(this::assertSerialisation);
	}

	@Test
	public void validation() {
		fixtures().stream()
			.filter(f -> f.getValidationMode() != ValidationMode.NONE)
			.forEach(this::assertValidation);
	}

	private void assertDeserialisation(Fixture<T> f) {
		try {
			assertThat(
				f.getFixture(),
				deserialise(fixture(f.getFixture())),
				deserialiseMatcher(f.getRepresentation())
			);
		} catch(IOException e) {
			Throwables.propagate(e);
		}
	}

	protected Matcher<T> deserialiseMatcher(T representation) {
		return is(representation);
	}

	private void assertSerialisation(Fixture<T> f) {
		try {
			assertThat(
				f.getFixture(),
				serialise(f.getRepresentation()),
				serialiseMatcher(fixture(f.getFixture()))
			);
		} catch(IOException e) {
			Throwables.propagate(e);
		}
	}

	protected Matcher<? super String> serialiseMatcher(String fixture) {
		return sameJSONAs(fixture);
	}

	private void assertValidation(Fixture<T> f) {
		try {
			Set<ConstraintViolation<T>> violations = validator().validate(deserialise(fixture(f.getFixture())));
			if(f.getValidationMode() == ValidationMode.INVALIDATE) {
				assertThat("Expected violations, none found", violations, not(empty()));
			} else {
				assertThat(violationsMessage(violations), violations, empty());
			}
		} catch(IOException e) {
			Throwables.propagate(e);
		}
	}

	protected String violationsMessage(Collection<ConstraintViolation<T>> violations) {
		return String.join("\n", violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet()));
	}

	protected String serialise(T representation) throws JsonProcessingException {
		return mapper.writeValueAsString(representation);
	}

	protected T deserialise(String fixture) throws IOException {
		return mapper.readValue(fixture, type());
	}

	protected String fixture(String fixture) {
		return FixtureHelpers.fixture(FilenameUtils.concat(fixtureDir(), fixture));
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getClassType() {
		Type t = type().getType();
		return (t instanceof ParameterizedType)
			? (Class<T>) ((ParameterizedType) t).getRawType()
			: (Class<T>) t;
	}

	protected String fixtureDir() {
		return "";
	}

	protected EqualsVerifier<T> equalsVerifier() {
		return EqualsVerifier.forClass(getClassType());
	}

	protected ObjectMapper objectMapper() {
		return Jackson.newObjectMapper()
			.registerModule(new ParameterNamesModule());
	}

	protected Validator validator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}

	protected static final class Fixture<T> {

		private final String fixture;
		private final T representation;
		private SerialisationMode serialisationMode = SerialisationMode.ALL;
		private ValidationMode validationMode = ValidationMode.NONE;

		public static <T> Fixture<T> from(String fixture, T representation) {
			return new Fixture<>(fixture, representation);
		}

		private Fixture(String fixture, T representation) {
			this.fixture = fixture;
			this.representation = representation;
		}

		public String getFixture() {
			return fixture;
		}

		public T getRepresentation() {
			return representation;
		}

		public SerialisationMode getSerialisationMode() {
			return serialisationMode;
		}

		public ValidationMode getValidationMode() {
			return validationMode;
		}

		public Fixture<T> serialisationMode(SerialisationMode serialisationMode) {
			this.serialisationMode = serialisationMode;
			return this;
		}

		public Fixture<T> serialise() {
			return serialisationMode(SerialisationMode.SERIALISE);
		}

		public Fixture<T> deserialise() {
			return serialisationMode(SerialisationMode.DESERIALISE);
		}

		public Fixture<T> validationMode(ValidationMode validationMode) {
			this.validationMode = validationMode;
			return this;
		}

		public Fixture<T> validate() {
			return validationMode(ValidationMode.VALIDATE);
		}

		public Fixture<T> invalidate() {
			return validationMode(ValidationMode.INVALIDATE);
		}
	}


	private enum SerialisationMode {
		SERIALISE, DESERIALISE, ALL
	}


	private enum ValidationMode {
		NONE, VALIDATE, INVALIDATE
	}
}
