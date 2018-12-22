package com.kingfisher.deployment.audit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingfisher.deployment.audit.repository.DeploymentRepository;

@Service
public class DeploymentAuditService {
	
	@Autowired
	DeploymentRepository deploymentRepository;

}
