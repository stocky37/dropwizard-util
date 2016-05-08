package com.github.stocky37.util.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public class SessionFactory extends SessionConfiguration {
	public Session build(Connection connection) throws JMSException {
		return connection.createSession(isTransacted(), getAcknowledgeMode());
	}
}
