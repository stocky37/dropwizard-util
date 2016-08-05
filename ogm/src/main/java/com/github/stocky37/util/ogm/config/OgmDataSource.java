package com.github.stocky37.util.ogm.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.net.HostAndPort;
import io.dropwizard.jackson.Discoverable;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.ogm.cfg.OgmProperties;
import org.hibernate.service.ServiceRegistry;

import java.util.Collection;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class OgmDataSource implements Discoverable {
	private static final Joiner HOST_JOINER = Joiner.on(",").skipNulls();

	private Collection<HostAndPort> hosts = ImmutableList.of();
	private Optional<String> database = Optional.empty();
	private Optional<Boolean> createDatabase = Optional.empty();
	private Optional<String> username = Optional.empty();
	private Optional<String> password = Optional.empty();

	public Collection<HostAndPort> getHosts() {
		return hosts;
	}

	public OgmDataSource setHosts(Collection<HostAndPort> hosts) {
		this.hosts = hosts;
		return this;
	}

	public Optional<String> getDatabase() {
		return database;
	}

	public OgmDataSource setDatabase(Optional<String> database) {
		this.database = database;
		return this;
	}

	public Optional<Boolean> getCreateDatabase() {
		return createDatabase;
	}

	public OgmDataSource setCreateDatabase(Optional<Boolean> createDatabase) {
		this.createDatabase = createDatabase;
		return this;
	}

	public Optional<String> getUsername() {
		return username;
	}

	public OgmDataSource setUsername(Optional<String> username) {
		this.username = username;
		return this;
	}

	public Optional<String> getPassword() {
		return password;
	}

	public OgmDataSource setPassword(Optional<String> password) {
		this.password = password;
		return this;
	}

	public abstract String getDatastoreProvider();

	public ServiceRegistry build() {
		return builder().build();
	}

	protected StandardServiceRegistryBuilder builder() {
		final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
			.applySetting(OgmProperties.ENABLED, true)
			.applySetting(OgmProperties.DATASTORE_PROVIDER, getDatastoreProvider());

		if(!CollectionUtils.isEmpty(hosts)) {
			builder.applySetting(OgmProperties.HOST, HOST_JOINER.join(hosts));
		}

		database.ifPresent(x -> builder.applySetting(OgmProperties.DATABASE, x));
		createDatabase.ifPresent(x -> builder.applySetting(OgmProperties.CREATE_DATABASE, x));
		username.ifPresent(x -> builder.applySetting(OgmProperties.USERNAME, x));
		password.ifPresent(x -> builder.applySetting(OgmProperties.PASSWORD, x));

		return builder;
	}
}
