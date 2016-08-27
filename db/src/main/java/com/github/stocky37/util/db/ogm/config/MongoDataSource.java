package com.github.stocky37.util.db.ogm.config;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Converter;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.mongodb.MongoClientOptions;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.ogm.datastore.mongodb.MongoDB;
import org.hibernate.ogm.datastore.mongodb.MongoDBProperties;
import org.hibernate.ogm.datastore.mongodb.options.AssociationDocumentStorageType;
import org.hibernate.ogm.datastore.mongodb.options.AuthenticationMechanismType;
import org.hibernate.ogm.datastore.mongodb.options.ReadPreferenceType;
import org.hibernate.ogm.datastore.mongodb.options.WriteConcernType;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Optional;

@JsonTypeName("mongo")
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class MongoDataSource extends DocumentDataSource {
	private static final Joiner PROP_JOINER = Joiner.on(".").skipNulls();

	@ParametersAreNonnullByDefault
	private static final Converter<MongoClientOptions, Map<String, Object>> OPTIONS_CONVERTER =
		new Converter<MongoClientOptions, Map<String, Object>>() {
			@Override
			protected Map<String, Object> doForward(MongoClientOptions config) {
				final ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();

				put(builder, "description", config.getDescription());
				put(builder, "codecRegistry", config.getCodecRegistry());

				put(builder, "minConnectionsPerHost", config.getMinConnectionsPerHost());
				put(builder, "maxConnectionsPerHost", config.getConnectionsPerHost());
				put(builder, "threadsAllowedToBlockForConnectionMultiplier", config.getThreadsAllowedToBlockForConnectionMultiplier());
				put(builder, "serverSelectionTimeout", config.getServerSelectionTimeout());
				put(builder, "maxWaitTime", config.getMaxWaitTime());
				put(builder, "maxConnectionIdleTime", config.getMaxConnectionIdleTime());
				put(builder, "maxConnectionLifeTime", config.getMaxConnectionLifeTime());
				put(builder, "connectTimeout", config.getConnectTimeout());
				put(builder, "socketTimeout", config.getSocketTimeout());
				put(builder, "socketKeepAlive", config.isSocketKeepAlive());
				put(builder, "sslEnabled", config.isSslEnabled());
				put(builder, "sslInvalidHostNameAllowed", config.isSslInvalidHostNameAllowed());
				put(builder, "alwaysUseMBeans", config.isAlwaysUseMBeans());

				put(builder, "heartbeatFrequency", config.getHeartbeatFrequency());
				put(builder, "minHeartbeatFrequency", config.getMinHeartbeatFrequency());
				put(builder, "heartbeatConnectTimeout", config.getHeartbeatConnectTimeout());
				put(builder, "heartbeatSocketTimeout", config.getHeartbeatSocketTimeout());
				put(builder, "localThreshold", config.getLocalThreshold());

				put(builder, "requiredReplicaSetName", config.getRequiredReplicaSetName());
				put(builder, "dbDecoderFactory", config.getDbDecoderFactory());
				put(builder, "dbEncoderFactory", config.getDbEncoderFactory());
				put(builder, "socketFactory", config.getSocketFactory());
				put(builder, "cursorFinalizerEnabled", config.isCursorFinalizerEnabled());

				return builder.build();
			}

			@Override
			protected MongoClientOptions doBackward(Map<String, Object> stringObjectMap) {
				throw new UnsupportedOperationException();
			}

			private void put(ImmutableMap.Builder<String, Object> builder, String property, @Nullable Object value) {
				if(value != null) builder.put(key(property), value);
			}
		};

	@Valid
	@NotNull
	private MongoConfiguration config = new MongoConfiguration();
	private Optional<WriteConcernType> writeConcern = Optional.empty();
	private Optional<ReadPreferenceType> readPreference = Optional.empty();
	private Optional<AssociationDocumentStorageType> associationDocumentStorage = Optional.empty();
	private Optional<AuthenticationMechanismType> authenticationMechanism = Optional.empty();

	private static String key(final String property) {
		return PROP_JOINER.join(MongoDBProperties.MONGO_DRIVER_SETTINGS_PREFIX, property);
	}

	public MongoConfiguration getConfig() {
		return config;
	}

	public MongoDataSource setConfig(MongoConfiguration config) {
		this.config = config;
		return this;
	}

	public Optional<WriteConcernType> getWriteConcern() {
		return writeConcern;
	}

	public MongoDataSource setWriteConcern(Optional<WriteConcernType> writeConcern) {
		this.writeConcern = writeConcern;
		return this;
	}

	public Optional<ReadPreferenceType> getReadPreference() {
		return readPreference;
	}

	public MongoDataSource setReadPreference(Optional<ReadPreferenceType> readPreference) {
		this.readPreference = readPreference;
		return this;
	}

	public Optional<AssociationDocumentStorageType> getAssociationDocumentStorage() {
		return associationDocumentStorage;
	}

	public MongoDataSource setAssociationDocumentStorage(Optional<AssociationDocumentStorageType> associationDocumentStorage) {
		this.associationDocumentStorage = associationDocumentStorage;
		return this;
	}

	public Optional<AuthenticationMechanismType> getAuthenticationMechanism() {
		return authenticationMechanism;
	}

	public MongoDataSource setAuthenticationMechanism(Optional<AuthenticationMechanismType> authenticationMechanism) {
		this.authenticationMechanism = authenticationMechanism;
		return this;
	}

	@Override
	public String getDatastoreProvider() {
		return MongoDB.DATASTORE_PROVIDER_NAME;
	}

	@Override
	protected StandardServiceRegistryBuilder builder() {
		final StandardServiceRegistryBuilder builder = super.builder();
		builder.applySettings(OPTIONS_CONVERTER.convert(config.build()));
		writeConcern.ifPresent(x -> builder.applySetting(MongoDBProperties.WRITE_CONCERN, x));
		readPreference.ifPresent(x -> builder.applySetting(MongoDBProperties.READ_PREFERENCE, x));
		associationDocumentStorage.ifPresent(x -> builder.applySetting(MongoDBProperties.ASSOCIATION_DOCUMENT_STORAGE, x));
		authenticationMechanism.ifPresent(x -> builder.applySetting(MongoDBProperties.AUTHENTICATION_MECHANISM, x));
		return builder;
	}
}
