package com.github.stocky37.util.jms;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.ConnectionFactory;

public class ApacheMQConnectionFactory  implements JMSConnectionFactory {

	private final String url;

	@JsonCreator
	public ApacheMQConnectionFactory(String url) {
		this.url = url;
	}

	@Override
	public ConnectionFactory build() {
		return new ActiveMQConnectionFactory(url);
	}
}
