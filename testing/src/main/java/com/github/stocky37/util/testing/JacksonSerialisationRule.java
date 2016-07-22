package com.github.stocky37.util.testing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.common.base.Throwables;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import org.apache.commons.io.FilenameUtils;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

public class JacksonSerialisationRule<T> extends RepresentationRule<T> {
	private static final ObjectMapper DEFAULT_OM = Jackson.newObjectMapper()
			.registerModule(new ParameterNamesModule());

	private final ObjectMapper mapper;
	private final String fixtureDir;

	public JacksonSerialisationRule(Class<T> type, Object target) {
		this(type, target, DEFAULT_OM, "");
	}

	public JacksonSerialisationRule(Class<T> type, Object target, String fixtureDir) {
		this(type, target, DEFAULT_OM, fixtureDir);
	}

	public JacksonSerialisationRule(Class<T> type, Object target, ObjectMapper mapper, String fixtureDir) {
		super(type, target);
		this.mapper = mapper;
		this.fixtureDir = fixtureDir;
	}

	@Override
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				final JacksonVerification config = description.getAnnotation(JacksonVerification.class);
				if(config != null) {
					final T representation = getRepresentation(config.representation());
					if(config.serialise()) {
						checkSerialisation(representation, config.fixture());
					}
					if(config.deserialise()) {
						checkDeserialisation(representation, config.fixture());
					}
				}
				base.evaluate();
			}
		};
	}

	private void checkSerialisation(final T representation, final String fixture) {
		assertThat("Failed serialisation", serialise(representation), sameJSONAs(fixture(fixture)));
	}

	private void checkDeserialisation(final T representation, final String fixture) {
		assertThat("Failed deserialisation", deserialise(fixture(fixture)), is(representation));
	}

	private String serialise(T representation) {
		try {
			return mapper.writeValueAsString(representation);
		} catch(JsonProcessingException e) {
			throw Throwables.propagate(e);
		}
	}

	private T deserialise(String fixture) {
		try {
			return mapper.readValue(fixture, getType());
		} catch(IOException e) {
			throw Throwables.propagate(e);
		}
	}

	private String fixture(String fixture) {
		return FixtureHelpers.fixture(FilenameUtils.concat(fixtureDir, fixture));
	}
}
