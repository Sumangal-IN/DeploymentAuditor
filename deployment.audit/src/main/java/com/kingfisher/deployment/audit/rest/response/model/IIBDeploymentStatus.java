package com.kingfisher.deployment.audit.rest.response.model;

import java.util.List;

public class IIBDeploymentStatus {
	private String uri;
	private List<DeploymentProperties> basicProperties;
	private List<DeploymentProperties> advancedProperties;
	private List<DeploymentProperties> deployedProperties;

	public IIBDeploymentStatus() {
	}

	public IIBDeploymentStatus(String uri, List<DeploymentProperties> basicProperties, List<DeploymentProperties> advancedProperties, List<DeploymentProperties> deployedProperties) {
		super();
		this.uri = uri;
		this.basicProperties = basicProperties;
		this.advancedProperties = advancedProperties;
		this.deployedProperties = deployedProperties;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public List<DeploymentProperties> getBasicProperties() {
		return basicProperties;
	}

	public void setBasicProperties(List<DeploymentProperties> basicProperties) {
		this.basicProperties = basicProperties;
	}

	public List<DeploymentProperties> getAdvancedProperties() {
		return advancedProperties;
	}

	public void setAdvancedProperties(List<DeploymentProperties> advancedProperties) {
		this.advancedProperties = advancedProperties;
	}

	public List<DeploymentProperties> getDeployedProperties() {
		return deployedProperties;
	}

	public void setDeployedProperties(List<DeploymentProperties> deployedProperties) {
		this.deployedProperties = deployedProperties;
	}

}
