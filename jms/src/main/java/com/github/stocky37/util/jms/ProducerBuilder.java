package com.github.stocky37.util.jms;

import com.google.common.base.Throwables;

import javax.jms.BytesMessage;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import java.io.Serializable;
import java.time.Duration;
import java.util.Map;

public class ProducerBuilder implements AutoCloseable {
	private final Session session;
	private final MessageProducer producer;

	public ProducerBuilder(Session session, MessageProducer producer) {
		this.session = session;
		this.producer = producer;
	}

	public ProducerBuilder enableId() throws JMSException {
		producer.setDisableMessageID(false);
		return this;
	}

	public ProducerBuilder disableId() throws JMSException {
		producer.setDisableMessageID(true);
		return this;
	}

	public ProducerBuilder enableTimestamp() throws JMSException {
		producer.setDisableMessageTimestamp(false);
		return this;
	}

	public ProducerBuilder disableTimestamp() throws JMSException {
		producer.setDisableMessageTimestamp(true);
		return this;
	}

	public ProducerBuilder nonPersistent() throws JMSException {
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		return this;
	}

	public ProducerBuilder persistent() throws JMSException {
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		return this;
	}

	public ProducerBuilder ttl(Duration duration) throws JMSException {
		producer.setTimeToLive(duration.toMillis());
		return this;
	}

	public ProducerBuilder priority(int defaultPriority) throws JMSException {
		producer.setPriority(defaultPriority);
		return this;
	}

	public Messager message() throws JMSException {
		return new Messager(producer, session.createMessage());
	}

	public Messager message(String text) throws JMSException {
		return new Messager(producer, session.createTextMessage(text));
	}

	public Messager message(Serializable object) throws JMSException {
		return new Messager(producer, session.createObjectMessage(object));
	}

	public Messager message(byte[] bytes) throws JMSException {
		final BytesMessage message = session.createBytesMessage();
		message.writeBytes(bytes);
		return new Messager(producer, message);
	}

	public Messager message(Map<String, Object> map) throws JMSException {
		final MapMessage message = session.createMapMessage();
		try {
			map.forEach((k, v) -> {
				try {
					if(v instanceof Boolean) {
						message.setBoolean(k, (Boolean)v);
					} else if(v instanceof Short) {
						message.setShort(k, (Short)v);
					} else if(v instanceof Integer) {
						message.setInt(k, (Integer)v);
					} else if(v instanceof Long) {
						message.setLong(k, (Long)v);
					} else if(v instanceof Float) {
						message.setFloat(k, (Float)v);
					} else if(v instanceof Double) {
						message.setDouble(k, (Double)v);
					} else if(v instanceof Character) {
						message.setChar(k, (Character)v);
					} else if(v instanceof String) {
						message.setString(k, (String)v);
					} else if(v instanceof Byte) {
						message.setByte(k, (Byte)v);
					} else if(v instanceof byte[]) {
						message.setBytes(k, (byte[])v);
					} else {
						message.setObject(k, v);
					}
				} catch(JMSException e) {
					throw Throwables.propagate(e);
				}
			});
		} catch(RuntimeException e) {
			Throwables.propagateIfInstanceOf(e.getCause(), JMSException.class);
			throw e;
		}
		return new Messager(producer, message);
	}

	@Override
	public void close() throws JMSException {
		session.close();
	}


}
