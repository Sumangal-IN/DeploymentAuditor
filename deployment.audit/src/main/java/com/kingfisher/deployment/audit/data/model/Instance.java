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
