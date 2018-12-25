package com.kingfisher.deployment.audit.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kingfisher.deployment.audit.data.model.Deployment;

@Repository
public interface DeploymentRepository extends JpaRepository<Deployment, Integer> {
	
	@Query(
			  value = "select d.* from (SELECT application_name, instance_name, max(created_time) as max_created_time FROM DEPLOYMENT where environment=:env group by application_name,instance_name) r " + 
			  		"join " + 
			  		"(select * from DEPLOYMENT where environment=:env) d on " + 
			  		"(r.application_name=d.application_name and r.max_created_time=d.created_time and r.instance_name=d.instance_name)", 
			  nativeQuery = true)
	List<Deployment> getLatestStatByEnvironment(@Param("env") String env);
}
