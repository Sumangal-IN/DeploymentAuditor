package com.kingfisher.deployment.audit.rest.client;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import com.kingfisher.deployment.audit.constant.ApplicationConstant;
import com.kingfisher.deployment.audit.rest.response.model.IIBDeploymentStatus;
import com.kingfisher.deployment.audit.security.codec.PasswordCodec;

@Component
public class IIBRestClient {

	@Autowired
	RestTemplate restTemplate;

	// @Autowired
	PasswordCodec passwordCodec;

	@Value("${IIBRestClient.username}")
	private String username;

	@Value("${IIBRestClient.password}")
	private String password;

	@Value("${property.security.key}")
	private String key;

	/**
	 * 
	 * @param ExecutionGroup
	 * @param ApplicationName
	 */
	public IIBDeploymentStatus getDeployemntProperties(String executionGroup, String applicationName)
			throws URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException {
		URI uri = new URI(ApplicationConstant.IIB_API_URL_EG_APP
				.replace(ApplicationConstant.IIB_API_PLACEHOLDER_EG, executionGroup)
				.replace(ApplicationConstant.IIB_API_PLACEHOLDER_APP, applicationName));
		HttpEntity<String> httpEntity = new HttpEntity<>(getHeaderWithAuth());
		ResponseEntity<IIBDeploymentStatus> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity,
				IIBDeploymentStatus.class);
		return response.getBody();
	}

	private HttpHeaders getHeaderWithAuth() throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(passwordCodec.decrypt(key, username), passwordCodec.decrypt(key, password));
		return headers;
	}

	/**
	 * Injecting {@code RestTemplate} with no SSL
	 * 
	 * @return {@code RestTemplate}
	 */
	@Bean
	public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
				.build();
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		return new RestTemplate(requestFactory);
	}

}
