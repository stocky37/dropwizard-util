package com.github.stocky37.util.jms;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.jms.JMSException;

@JsonTypeName("queue")
public class QueueProducerFactory extends AbstractProducerFactory {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected ProducerBuilder buildProducer(SessionBuilder sessionBuilder) throws JMSException {
		return sessionBuilder.queue(name).producer();
	}
}
