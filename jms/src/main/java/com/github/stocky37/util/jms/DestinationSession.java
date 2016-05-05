package com.github.stocky37.util.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

public class DestinationSession {
	private final Session session;
	private final Destination destination;

	public DestinationSession(Session session, Destination destination) {
		this.session = session;
		this.destination = destination;
	}

	public ProducerBuilder producer() throws JMSException {
		return new ProducerBuilder(session, session.createProducer(destination));
	}

	public MessageConsumer consumer() throws JMSException {
		return session.createConsumer(destination);
	}

	public MessageConsumer consumer(MessageListener listener) throws JMSException {
		MessageConsumer consumer = session.createConsumer(destination);
		consumer.setMessageListener(listener);
		return consumer;
	}

	public MessageConsumer consumer(MessageListener listener, String selector) throws JMSException {
		MessageConsumer consumer = session.createConsumer(destination, selector);
		consumer.setMessageListener(listener);
		return consumer;
	}

	public Session getSession() {
		return session;
	}

	public Destination getDestination() {
		return destination;
	}
}
