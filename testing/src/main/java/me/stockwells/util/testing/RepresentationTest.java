package me.stockwells.util.testing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import io.dropwizard.jackson.Jackson;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;


public abstract class RepresentationTest<T> {
	private final ObjectMapper mapper = objectMapper();

	protected abstract TypeReference<T> type();
	protected abstract Collection<Fixture> fixtures();


	@Test
	public void equalsContract() {
		equalsVerifier().verify();
	}

	@Test
	public void deserialisation() throws IOException {
		fixtures().stream()
			.filter(f -> f.getDirection() != Direction.SERIALISE)
			.forEach(this::assertDeserialisation);
	}

	@Test
	public void serialisation() throws JsonProcessingException {
		fixtures().stream()
			.filter(f -> f.getDirection() != Direction.DESERIALISE)
			.forEach(this::assertSerialisation);
	}

	public void assertDeserialisation(Fixture f) {
		try {
			assertThat(
				f.getFixture(),
				deserialise(fixture(f.getFixture())),
				is(f.getRepresentation())
			);
		} catch(IOException e) {
			Throwables.propagate(e);
		}
	}

	public void assertSerialisation(Fixture f) {
		try {
			assertThat(
				f.getFixture(),
				serialise(f.getRepresentation()),
				sameJSONAs(fixture(f.getFixture()))
			);
		} catch(IOException e) {
			Throwables.propagate(e);
		}
	}

	protected String serialise(T representation) throws JsonProcessingException {
		return mapper.writeValueAsString(representation);
	}

	protected T deserialise(String fixture) throws IOException {
		return mapper.readValue(fixture, type());
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getClassType() {
		Type t = type().getType();
		return (t instanceof ParameterizedType)
			? (Class<T>) ((ParameterizedType)t).getRawType()
			: (Class<T>) t;
	}

	protected EqualsVerifier<T> equalsVerifier() {
		return EqualsVerifier.forClass(getClassType());
	}

	protected ObjectMapper objectMapper() {
		return Jackson.newObjectMapper();
	}

	private enum Direction {
		SERIALISE, DESERIALISE, ALL;
	}

	protected final class Fixture {
		private final String fixture;
		private final T representation;
		private Direction direction = Direction.ALL;
		private Optional<Boolean> validation = Optional.empty();

		public Fixture(String fixture, T representation) {
			this.fixture = fixture;
			this.representation = representation;
		}

		public Fixture(String fixture, T representation, Direction direction, Optional<Boolean> validation) {
			this.fixture = fixture;
			this.representation = representation;
			this.direction = direction;
			this.validation = validation;
		}

		public String getFixture() {
			return fixture;
		}

		public T getRepresentation() {
			return representation;
		}

		public Direction getDirection() {
			return direction;
		}

		public Optional<Boolean> getValidation() {
			return validation;
		}

		public Fixture serialiseOnly() {
			direction = Direction.SERIALISE;
			return this;
		}

		public Fixture deserialiseOnly() {
			direction = Direction.DESERIALISE;
			return this;
		}

		public Fixture validate() {
			validation = Optional.of(true);
			return this;
		}

		public Fixture invalidate() {
			validation = Optional.of(false);
			return this;
		}
	}
}
