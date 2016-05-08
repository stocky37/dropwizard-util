package com.github.stocky37.util.jms;

import javax.jms.Connection;
import javax.jms.JMSException;

public interface ProducerFactory {
	ProducerBuilder build(Connection connection) throws JMSException;
}
