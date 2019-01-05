package com.kingfisher.deployment.audit.data.model;

public class Instance {
	private String tier;
	private String instanceName;
	private String host;

	public Instance() {
	}

	public Instance(String tier, String instanceName, String host) {
		super();
		this.tier = tier;
		this.instanceName = instanceName;
		this.host = host;
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

}
