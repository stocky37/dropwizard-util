package com.github.stocky37.util.jms;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.util.Duration;

import javax.jms.Connection;
import javax.jms.JMSException;


public abstract class AbstractProducerFactory implements ProducerFactory {
	private SessionConfiguration sessionConfiguration = new SessionConfiguration();
	private Boolean messageIdDisabled = null;
	private Boolean timestampDisabled = null;
	private Integer deliveryMode = null;
	private Duration ttl = null;
	private Integer priority = null;

	public int getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(int deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	public Duration getTtl() {
		return ttl;
	}

	public void setTtl(Duration ttl) {
		this.ttl = ttl;
	}

	public boolean isMessageIdDisabled() {
		return messageIdDisabled;
	}

	public void setMessageIdDisabled(boolean messageIdDisabled) {
		this.messageIdDisabled = messageIdDisabled;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isTimestampDisabled() {
		return timestampDisabled;
	}

	public void setTimestampDisabled(boolean timestampDisabled) {
		this.timestampDisabled = timestampDisabled;
	}

	public SessionConfiguration getSessionConfiguration() {
		return sessionConfiguration;
	}

	@JsonProperty("session")
	public void setSessionConfiguration(SessionFactory sessionConfiguration) {
		this.sessionConfiguration = sessionConfiguration;
	}

	@Override
	public ProducerBuilder build(Connection connection) throws JMSException {
		return configureProducer(buildProducer(configureSession(connection)));
	}

	protected abstract ProducerBuilder buildProducer(SessionBuilder sessionBuilder) throws JMSException;

	protected SessionBuilder configureSession(Connection connection) {
		return new SessionBuilder(connection)
			.setAcknowledgeMode(sessionConfiguration.getAcknowledgeMode())
			.setTransacted(sessionConfiguration.isTransacted());
	}

	protected ProducerBuilder configureProducer(ProducerBuilder builder) throws JMSException {
		if(messageIdDisabled != null) {
			builder.disableId(messageIdDisabled);
		}
		if(timestampDisabled != null) {
			builder.disableTimestamp(timestampDisabled);
		}
		if(deliveryMode != null) {
			builder.deliveryMode(deliveryMode);
		}
		if(ttl != null) {
			builder.ttl(java.time.Duration.ofMillis(ttl.toMilliseconds()));
		}
		if(priority != null) {
			builder.priority(priority);
		}
		return builder;
	}
}
