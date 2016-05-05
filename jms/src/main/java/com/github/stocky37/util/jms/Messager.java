package com.github.stocky37.util.jms;

import com.google.common.base.Throwables;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

public class Messager implements AutoCloseable {
	private final MessageProducer producer;
	private final Message message;

	public Messager(MessageProducer producer, Message message) {
		this.producer = producer;
		this.message = message;
	}

	public Messager correlationId(String correlationID) throws JMSException {
		message.setJMSCorrelationID(correlationID);
		return this;
	}

	public Messager correlationId(byte[] correlationID) throws JMSException {
		message.setJMSCorrelationIDAsBytes(correlationID);
		return this;
	}

	public Messager nonPersistent() throws JMSException {
		message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
		return this;
	}

	public Messager persistent() throws JMSException {
		message.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
		return this;
	}

	public Messager expiration(Duration expiration) throws JMSException {
		message.setJMSExpiration(expiration.toMillis());
		return this;
	}

	public Messager id(String id) throws JMSException {
		message.setJMSMessageID(id);
		return this;
	}

	public Messager priority(int priority) throws JMSException {
		message.setJMSPriority(priority);
		return this;
	}

	public Messager notRedelivered() throws JMSException {
		message.setJMSRedelivered(false);
		return this;
	}

	public Messager redelivered() throws JMSException {
		message.setJMSRedelivered(true);
		return this;
	}

	public Messager replyTo(Destination replyTo) throws JMSException {
		message.setJMSReplyTo(replyTo);
		return this;
	}

	public Messager timestamp(Date date) throws JMSException {
		message.setJMSTimestamp(date.getTime());
		return this;
	}

	public Messager type(String type) throws JMSException {
		message.setJMSType(type);
		return this;
	}

	public Messager properties(Map<String, Object> properties) throws JMSException {
		try {
			properties.forEach((name, value) -> {
				try {
					property(name, value);
				} catch(JMSException e) {
					throw Throwables.propagate(e);
				}
			});
		} catch(RuntimeException e) {
			Throwables.propagateIfInstanceOf(e.getCause(), JMSException.class);
			throw e;
		}
		return this;
	}

	public Messager property(String name, Object value) throws JMSException {
		if(value instanceof Boolean) {
			message.setBooleanProperty(name, (Boolean)value);
		} else if(value instanceof Short) {
			message.setShortProperty(name, (Short)value);
		} else if(value instanceof Integer) {
			message.setIntProperty(name, (Integer)value);
		} else if(value instanceof Long) {
			message.setLongProperty(name, (Long)value);
		} else if(value instanceof Float) {
			message.setFloatProperty(name, (Float)value);
		} else if(value instanceof Double) {
			message.setDoubleProperty(name, (Double)value);
		} else if(value instanceof String) {
			message.setStringProperty(name, (String)value);
		} else if(value instanceof Byte) {
			message.setByteProperty(name, (Byte)value);
		} else {
			message.setObjectProperty(name, value);
		}
		return this;
	}

	public void send() throws JMSException {
		producer.send(message);
	}

	@Override
	public void close() throws Exception {
		producer.close();
	}
}
