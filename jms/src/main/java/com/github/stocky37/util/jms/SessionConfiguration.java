package com.github.stocky37.util.jms;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.annotation.ParametersAreNullableByDefault;
import javax.jms.Session;
import java.util.Optional;

@ParametersAreNullableByDefault
public class SessionConfiguration {
	private final boolean transacted;
	private final int acknowledgeMode;

	public SessionConfiguration() {
		this(null, null);
	}

	@JsonCreator
	public SessionConfiguration(Integer acknowledgeMode, Boolean transacted) {
		this.acknowledgeMode = Optional.ofNullable(acknowledgeMode).orElse(Session.AUTO_ACKNOWLEDGE);
		this.transacted = Optional.ofNullable(transacted).orElse(false);
	}

	public int getAcknowledgeMode() {
		return acknowledgeMode;
	}

	public boolean isTransacted() {
		return transacted;
	}
}
