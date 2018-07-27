/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : BaseClosableHttpClient.java
 * @Description : 
 *
 * @Author : LGCNS
 * @Since : 2017. 4. 20.
 *
 * @Copyright (c) 2018 EX All rights reserved.
 *-------------------------------------------------------------
 *              Modification Information
 *-------------------------------------------------------------
 * 날짜            수정자             변경사유 
 *-------------------------------------------------------------
 * 2018. 6. 22.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.http;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;

public class BaseClosableHttpClient implements FactoryBean<CloseableHttpClient>, InitializingBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseClosableHttpClient.class);

	private static final String[] _sslProtocols = {"TLSv1.1", "TLSv1.2"};

	private String[] sslProtocols;
	private boolean ssl;
	private String keyStoreLocation;
	private String trustStoreLocation;
	private String password;
	private int maxPool;
	private int connectTimeout;
	private int readTimeout;
	private List<HttpRequestInterceptor> firstInterceptors;
	private List<HttpRequestInterceptor> lastInterceptors;

	@Override
	public CloseableHttpClient getObject() throws Exception {

		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		
		/** Add intercepter **/
		if (firstInterceptors != null) {
			for (HttpRequestInterceptor interceptor : firstInterceptors) {
				clientBuilder.addInterceptorFirst(interceptor);
			}
		}

		if (lastInterceptors != null) {
			for (HttpRequestInterceptor interceptor : firstInterceptors) {
				clientBuilder.addInterceptorLast(interceptor);
			}
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Add Interceptor end");
		}

		SSLContext sslContext = null;
		SSLConnectionSocketFactory sslSocketFactory = null;

		/** Not validate certification **/
		if (ssl) {
			sslContext = SSLContextBuilder
	                .create()
	                .loadKeyMaterial(ResourceUtils.getFile(keyStoreLocation), password.toCharArray(), password.toCharArray())
	                .loadTrustMaterial(ResourceUtils.getFile(trustStoreLocation), password.toCharArray())
	                .build();

	        sslSocketFactory = new SSLConnectionSocketFactory(
	        		sslContext,
	        		sslProtocols,
	                null,
	                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		}
		/** Validate Certifiaction **/
		else {
			/** This is not validate certificate **/
			sslContext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
			sslSocketFactory = new SSLConnectionSocketFactory(
	        		sslContext,
	        		sslProtocols,
	                null,
	                NoopHostnameVerifier.INSTANCE);

		}

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	            .register("http", PlainConnectionSocketFactory.getSocketFactory())
	            .register("https", sslSocketFactory)
	            .build();

        HttpComponentsClientHttpRequestFactory requestFactory =  new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager( socketFactoryRegistry);
        connMgr.setMaxTotal(maxPool);
        clientBuilder.setConnectionManager(connMgr);
        CloseableHttpClient httpClient = clientBuilder.build();

        return httpClient;
	}

	@Override
	public Class<?> getObjectType() {
		return CloseableHttpClient.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	/**
	 * 
	 *<pre>
	 * Create HTTP Client without certification
	 *</pre>
	 * @return
	 * @throws Exception
	 */
	public CloseableHttpClient noCertValidate() throws Exception {
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();

		/** This is not validate certificate **/
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();

        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
        		sslContext,
        		sslProtocols,
                null,
                NoopHostnameVerifier.INSTANCE);

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	            .register("http", PlainConnectionSocketFactory.getSocketFactory())
	            .register("https", sslSocketFactory)
	            .build();

        HttpComponentsClientHttpRequestFactory requestFactory =  new HttpComponentsClientHttpRequestFactory();
//      requestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager( socketFactoryRegistry);
        connMgr.setMaxTotal(maxPool);
//        connMgr.setDefaultConnectionConfig(requestFactory);
        clientBuilder.setConnectionManager(connMgr);
        CloseableHttpClient httpClient = clientBuilder.build();

        return httpClient;
	}

	/**
	 * 
	 *<pre>
	 * Create HTTP client with certification 
	 *</pre>
	 * @return CloseableHttpClient
	 * @throws Exception
	 */
	public CloseableHttpClient certValidate() throws Exception {
		

		HttpClientBuilder clientBuilder = HttpClientBuilder.create();

        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadKeyMaterial(ResourceUtils.getFile(keyStoreLocation), password.toCharArray(), password.toCharArray())
                .loadTrustMaterial(ResourceUtils.getFile(trustStoreLocation), password.toCharArray())
                .build();

		/** This is not validate certificate **/
//		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//		keyStore.load(new FileInputStream(new File(keyStoreLoc)), password.toCharArray());
//      SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy())
//                .loadKeyMaterial(keyStore, "password".toCharArray()).build();

        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
        		sslContext,
        		sslProtocols,
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	            .register("http", PlainConnectionSocketFactory.getSocketFactory())
	            .register("https", sslSocketFactory)
	            .build();

        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager( socketFactoryRegistry);
        connMgr.setMaxTotal(maxPool);

        clientBuilder.setConnectionManager(connMgr);
        CloseableHttpClient httpClient = clientBuilder.build();
        return httpClient;

	}

	@Required
	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	public void setSslProtocols(String[] sslProtocols) {
		this.sslProtocols = sslProtocols;
		if (sslProtocols == null) {
			this.sslProtocols = _sslProtocols;
		}
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setMaxPool(int maxPool) {
		if (maxPool == 0) {
			this.maxPool = 100;
		}
		else {
			this.maxPool = maxPool;
		}
	}

	public void setKeyStoreLocation(String keyStoreLocation) {
		this.keyStoreLocation = keyStoreLocation;
	}

	public void setTrustStoreLocation(String trustStoreLocation) {
		this.trustStoreLocation = trustStoreLocation;
	}

	public void setConnectTimeout(int connectTimeout) {
		if (connectTimeout == 0) {
			this.connectTimeout = 5000;
		}
		else {
			this.connectTimeout = connectTimeout;
		}
	}

	public void setReadTimeout(int readTimeout) {
		if (readTimeout == 0) {
			this.readTimeout = 5000;
		}
		else {
			this.readTimeout = readTimeout;
		}
	}

	public void setFirstInterceptors(List<HttpRequestInterceptor> firstInterceptors) {
		this.firstInterceptors = firstInterceptors;
	}

	public void setLastInterceptors(List<HttpRequestInterceptor> lastInterceptors) {
		this.lastInterceptors = lastInterceptors;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(ssl) {
			if (sslProtocols == null || keyStoreLocation == null || trustStoreLocation == null || password == null) {
				LOGGER.error("ssl protocol, key store, trust store and password should not be null, Please check your configuaration");
				throw new RuntimeException("ssl protocol, key store, trust store and password should not be null");
			}
		}
		
	}
}
