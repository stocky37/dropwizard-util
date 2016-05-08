package com.github.stocky37.util.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public class SessionBuilder implements AutoCloseable {
	private final Connection connection;
	private boolean transacted = false;
	private int acknowledgeMode = Session.AUTO_ACKNOWLEDGE;

	public SessionBuilder(Connection connection) {
		this.connection = connection;
	}

	public SessionBuilder transacted() {
		return setTransacted(true);
	}

	public SessionBuilder notTransacted() {
		return setTransacted(false);
	}

	public SessionBuilder setTransacted(boolean transacted) {
		this.transacted = transacted;
		return this;
	}

	public SessionBuilder autoAcknowledge() {
		return this.setAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
	}

	public SessionBuilder duplicatesOk() {
		return setAcknowledgeMode(Session.DUPS_OK_ACKNOWLEDGE);
	}

	public SessionBuilder clientAcknowledge() {
		return setAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
	}

	public SessionBuilder setAcknowledgeMode(int acknowledgeMode) {
		this.acknowledgeMode = Session.CLIENT_ACKNOWLEDGE;
		return this;
	}

	public DestinationSession queue() throws JMSException {
		final Session session = createSession();
		return new DestinationSession(session, session.createTemporaryQueue());
	}

	public DestinationSession queue(String name) throws JMSException {
		final Session session = createSession();
		return new DestinationSession(session, session.createQueue(name));
	}

	public DestinationSession topic() throws JMSException {
		final Session session = createSession();
		return new DestinationSession(session, session.createTemporaryTopic());
	}

	public DestinationSession topic(String name) throws JMSException {
		final Session session = createSession();
		return new DestinationSession(session, session.createTopic(name));
	}

	private Session createSession() throws JMSException {
		return connection.createSession(transacted, acknowledgeMode);
	}

	@Override
	public void close() throws Exception {
		connection.close();
	}
}
