package com.kingfisher.deployment.audit.rest.client;

import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import com.kingfisher.deployment.audit.constant.ApplicationConstant;
import com.kingfisher.deployment.audit.rest.response.model.IIBDeploymentStatus;

@Component
public class IIBRestClient {

	@Value("${IIBapi.username}")
	private String username;

	@Value("${IIBapi.password}")
	private String password;

	/**
	 * 
	 * @param ExecutionGroup
	 * @param ApplicationName
	 * @throws URISyntaxException 
	 */
	public IIBDeploymentStatus getDeployemntProperties(String executionGroup, String applicationName, String apiHost, int apiPort) throws URISyntaxException  {
		RestTemplate restTemplate=new RestTemplate();
		URI uri = new URI(ApplicationConstant.IIB_API_URL_EG_APP
				.replace(ApplicationConstant.IIB_API_PLACEHOLDER_HOST, apiHost)
				.replace(ApplicationConstant.IIB_API_PLACEHOLDER_PORT, Integer.toString(apiPort))
				.replace(ApplicationConstant.IIB_API_PLACEHOLDER_EG, executionGroup)
				.replace(ApplicationConstant.IIB_API_PLACEHOLDER_APP, applicationName));
		HttpEntity<String> httpEntity = new HttpEntity<>(getHeaderWithAuth());
		ResponseEntity<IIBDeploymentStatus> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, IIBDeploymentStatus.class);
		return response.getBody();
	}

	private HttpHeaders getHeaderWithAuth() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(username, password);
		return headers;
	}

}
