package com.kingfisher.deployment.audit.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kingfisher.deployment.audit.data.model.Deployment;

@Repository
public interface DeploymentRepository extends JpaRepository<Deployment, Integer> {

}
