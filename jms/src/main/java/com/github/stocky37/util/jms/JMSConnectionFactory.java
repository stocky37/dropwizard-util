package com.github.stocky37.util.jms;

import io.dropwizard.jackson.Discoverable;

import javax.jms.ConnectionFactory;

public interface JMSConnectionFactory extends Discoverable {
	ConnectionFactory build();
}
