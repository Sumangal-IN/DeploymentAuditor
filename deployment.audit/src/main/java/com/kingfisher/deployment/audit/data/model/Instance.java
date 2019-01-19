package com.kingfisher.deployment.audit.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Instance")
public class Instance {
	@Id
	@Column(name = "instance_name")
	private String instanceName;

	@Column(name = "tier")
	private String tier;

	@Column(name = "host")
	private String host;

	@Column(name = "api_host")
	private String apiHost;

	@Column(name = "api_port")
	private int apiPort;

	public Instance() {
	}

	public Instance(String instanceName, String tier, String host, String apiHost, int apiPort) {
		super();
		this.instanceName = instanceName;
		this.tier = tier;
		this.host = host;
		this.apiHost = apiHost;
		this.apiPort = apiPort;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getApiHost() {
		return apiHost;
	}

	public void setApiHost(String apiHost) {
		this.apiHost = apiHost;
	}

	public int getApiPort() {
		return apiPort;
	}

	public void setApiPort(int apiPort) {
		this.apiPort = apiPort;
	}

}
