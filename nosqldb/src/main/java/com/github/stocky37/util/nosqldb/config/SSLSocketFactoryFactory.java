package com.github.stocky37.util.nosqldb.config;

import io.dropwizard.client.ssl.TlsConfiguration;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLInitializationException;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.TrustStrategy;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

public class SSLSocketFactoryFactory {
	private final TlsConfiguration configuration;

	public SSLSocketFactoryFactory(TlsConfiguration configuration) {
		this.configuration = configuration;
	}

	private static KeyStore loadKeyStore(String type, File path, String password) throws Exception {
		final KeyStore keyStore = KeyStore.getInstance(type);
		try(InputStream inputStream = new FileInputStream(path)) {
			keyStore.load(inputStream, password.toCharArray());
		}
		return keyStore;
	}

	public SocketFactory getSocketFactory() throws SSLInitializationException {
		return buildSslContext().getSocketFactory();
	}

	private SSLContext buildSslContext() throws SSLInitializationException {
		final SSLContext sslContext;
		try {
			final org.apache.http.ssl.SSLContextBuilder sslContextBuilder = new org.apache.http.ssl.SSLContextBuilder();
			sslContextBuilder.useProtocol(configuration.getProtocol());
			loadKeyMaterial(sslContextBuilder);
			loadTrustMaterial(sslContextBuilder);
			sslContext = sslContextBuilder.build();
		} catch(Exception e) {
			throw new SSLInitializationException(e.getMessage(), e);
		}
		return sslContext;
	}

	private HostnameVerifier chooseHostnameVerifier() {
		HostnameVerifier hostnameVerifier;
		if(configuration.isVerifyHostname()) {
			hostnameVerifier = SSLConnectionSocketFactory.getDefaultHostnameVerifier();
		} else {
			hostnameVerifier = new NoopHostnameVerifier();
		}
		return hostnameVerifier;
	}

	private void loadKeyMaterial(org.apache.http.ssl.SSLContextBuilder sslContextBuilder) throws Exception {
		if(configuration.getKeyStorePath() != null) {
			final KeyStore keystore = loadKeyStore(configuration.getKeyStoreType(), configuration.getKeyStorePath(),
				configuration.getKeyStorePassword());
			sslContextBuilder.loadKeyMaterial(keystore, configuration.getKeyStorePassword().toCharArray());
		}
	}

	private void loadTrustMaterial(org.apache.http.ssl.SSLContextBuilder sslContextBuilder) throws Exception {
		KeyStore trustStore = null;
		if(configuration.getTrustStorePath() != null) {
			trustStore = loadKeyStore(configuration.getTrustStoreType(), configuration.getTrustStorePath(),
				configuration.getTrustStorePassword());
		}
		TrustStrategy trustStrategy = null;
		if(configuration.isTrustSelfSignedCertificates()) {
			trustStrategy = new TrustSelfSignedStrategy();
		}
		sslContextBuilder.loadTrustMaterial(trustStore, trustStrategy);
	}

}
