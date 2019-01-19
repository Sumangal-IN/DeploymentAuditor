package com.kingfisher.deployment.audit.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kingfisher.deployment.audit.data.model.Instance;

@Repository
public interface InstanceRepository extends JpaRepository<Instance, String> {
	List<Instance> findByTier(String tier);
	Instance findByInstanceName(String instanceName);
}
