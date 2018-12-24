package com.kingfisher.deployment.audit.data.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "deployment")
public class Deployment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_deployment")
	@Column(name = "deployment_id")
	private int deploymentId;

	@Column(name = "application_name")
	private String applicationName;

	@Column(name = "bar_file_name")
	private String barfileName;

	@Column(name = "environment")
	private String environment;

	@Column(name = "instance_name")
	private String instanceName;

	@Column(name = "integration_server")
	private String integrationServer;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deployment_time")
	private Date deploymentTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time")
	private Date createdTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_time")
	private Date updatedTime;

	@Column(name = "bar_release_id")
	private String barReleaseId;

	@Column(name = "bar_checksum")
	private String barChecksum;

	@Column(name = "deployed_by_username")
	private String deployedByUserName;

	@Column(name = "deployed_by_user_id")
	private String deployedByUserId;

	@Column(name = "created_by_process")
	private String createdByProcess;

	public Deployment() {
		super();
	}

	public Deployment(String applicationName, String barfileName, String environment, String instanceName,
			String integrationServer, Date deploymentTime, Date createdTime, Date updatedTime, String barReleaseId,
			String barChecksum, String deployedByUserName, String deployedByUserId, String createdByProcess) {
		super();
		this.applicationName = applicationName;
		this.barfileName = barfileName;
		this.environment = environment;
		this.instanceName = instanceName;
		this.integrationServer = integrationServer;
		this.deploymentTime = deploymentTime;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
		this.barReleaseId = barReleaseId;
		this.barChecksum = barChecksum;
		this.deployedByUserName = deployedByUserName;
		this.deployedByUserId = deployedByUserId;
		this.createdByProcess = createdByProcess;
	}

	public int getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(int deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getBarfileName() {
		return barfileName;
	}

	public void setBarfileName(String barfileName) {
		this.barfileName = barfileName;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getIntegrationServer() {
		return integrationServer;
	}

	public void setIntegrationServer(String integrationServer) {
		this.integrationServer = integrationServer;
	}

	public Date getDeploymentTime() {
		return deploymentTime;
	}

	public void setDeploymentTime(Date deploymentTime) {
		this.deploymentTime = deploymentTime;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getBarReleaseId() {
		return barReleaseId;
	}

	public void setBarReleaseId(String barReleaseId) {
		this.barReleaseId = barReleaseId;
	}

	public String getBarChecksum() {
		return barChecksum;
	}

	public void setBarChecksum(String barChecksum) {
		this.barChecksum = barChecksum;
	}

	public String getDeployedByUserName() {
		return deployedByUserName;
	}

	public void setDeployedByUserName(String deployedByUserName) {
		this.deployedByUserName = deployedByUserName;
	}

	public String getDeployedByUserId() {
		return deployedByUserId;
	}

	public void setDeployedByUserId(String deployedByUserId) {
		this.deployedByUserId = deployedByUserId;
	}

	public String getCreatedByProcess() {
		return createdByProcess;
	}

	public void setCreatedByProcess(String createdByProcess) {
		this.createdByProcess = createdByProcess;
	}

}
