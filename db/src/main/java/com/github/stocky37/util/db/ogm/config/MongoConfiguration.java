package com.github.stocky37.util.db.ogm.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.primitives.Ints;
import com.mongodb.MongoClientOptions;
import io.dropwizard.client.ssl.TlsConfiguration;
import io.dropwizard.util.Duration;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MongoConfiguration {

	@JsonIgnore
	private final MongoClientOptions.Builder builder = new MongoClientOptions.Builder();

	public MongoClientOptions.Builder getBuilder() {
		return builder;
	}

	// have to test this out, if it doesn't work might have to just manually set the
	// javax.net SSL properties from this config instead >.<
	public MongoConfiguration tls(TlsConfiguration tls) {
		builder.sslEnabled(true).socketFactory(new SSLSocketFactoryFactory(tls).getSocketFactory());
		if(tls.isVerifyHostname()) {
			builder.sslInvalidHostNameAllowed(true);
		}
		return this;
	}

	public MongoConfiguration description(String description) {
		builder.description(description);
		return this;
	}

	public MongoConfiguration minConnectionsPerHost(int minConnectionsPerHost) {
		builder.minConnectionsPerHost(minConnectionsPerHost);
		return this;
	}

	public MongoConfiguration connectionsPerHost(int connectionsPerHost) {
		builder.connectionsPerHost(connectionsPerHost);
		return this;
	}

	public MongoConfiguration threadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
		builder.threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier);
		return this;
	}

	public MongoConfiguration serverSelectionTimeout(Duration serverSelectionTimeout) {
		builder.serverSelectionTimeout(Ints.checkedCast(serverSelectionTimeout.toMilliseconds()));
		return this;
	}

	public MongoConfiguration maxWaitTime(Duration maxWaitTime) {
		builder.maxWaitTime(Ints.checkedCast(maxWaitTime.toMilliseconds()));
		return this;
	}

	public MongoConfiguration maxConnectionIdleTime(Duration maxConnectionIdleTime) {
		builder.maxConnectionIdleTime(Ints.checkedCast(maxConnectionIdleTime.toMilliseconds()));
		return this;
	}

	public MongoConfiguration maxConnectionLifeTime(Duration maxConnectionLifeTime) {
		builder.maxConnectionLifeTime(Ints.checkedCast(maxConnectionLifeTime.toMilliseconds()));
		return this;
	}

	public MongoConfiguration connectTimeout(Duration connectTimeout) {
		builder.connectTimeout(Ints.checkedCast(connectTimeout.toMilliseconds()));
		return this;
	}

	public MongoConfiguration socketTimeout(Duration socketTimeout) {
		builder.socketTimeout(Ints.checkedCast(socketTimeout.toMilliseconds()));
		return this;
	}

	public MongoConfiguration socketKeepAlive(boolean socketKeepAlive) {
		builder.socketKeepAlive(socketKeepAlive);
		return this;
	}

	public MongoConfiguration cursorFinalizerEnabled(boolean cursorFinalizerEnabled) {
		builder.cursorFinalizerEnabled(cursorFinalizerEnabled);
		return this;
	}

	public MongoConfiguration alwaysUseMBeans(boolean alwaysUseMBeans) {
		builder.alwaysUseMBeans(alwaysUseMBeans);
		return this;
	}

	public MongoConfiguration heartbeatFrequency(Duration heartbeatFrequency) {
		builder.heartbeatFrequency(Ints.checkedCast(heartbeatFrequency.toMilliseconds()));
		return this;
	}

	public MongoConfiguration minHeartbeatFrequency(Duration minHeartbeatFrequency) {
		builder.minHeartbeatFrequency(Ints.checkedCast(minHeartbeatFrequency.toMilliseconds()));
		return this;
	}

	public MongoConfiguration heartbeatConnectTimeout(Duration connectTimeout) {
		builder.heartbeatConnectTimeout(Ints.checkedCast(connectTimeout.toMilliseconds()));
		return this;
	}

	public MongoConfiguration heartbeatSocketTimeout(Duration socketTimeout) {
		builder.heartbeatSocketTimeout(Ints.checkedCast(socketTimeout.toMilliseconds()));
		return this;
	}

	public MongoConfiguration localThreshold(Duration localThreshold) {
		builder.localThreshold(Ints.checkedCast(localThreshold.toMilliseconds()));
		return this;
	}

	public MongoConfiguration requiredReplicaSetName(String requiredReplicaSetName) {
		builder.requiredReplicaSetName(requiredReplicaSetName);
		return this;
	}

	public MongoClientOptions build() {
		return builder.build();
	}
}
