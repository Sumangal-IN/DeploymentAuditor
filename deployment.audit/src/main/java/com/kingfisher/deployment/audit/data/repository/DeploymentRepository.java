package com.kingfisher.deployment.audit.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kingfisher.deployment.audit.data.model.Deployment;

@Repository
public interface DeploymentRepository extends JpaRepository<Deployment, Integer> {

	@Query(value = "select distinct application_name from deployment where environment=:env order by application_name asc", nativeQuery = true)
	List<String> findApplicationNameByEnvironment(@Param("env") String env);

	@Query(value = "select d.* from (select max(created_time)max_created_time, instance_name ,integration_server from deployment where application_name=:app and environment=:env group by instance_name ,integration_server) p join (select * from deployment where application_name=:app and environment=:env) d on p.max_created_time=d.created_time and p.instance_name=d.instance_name and p.integration_server=d.integration_server order by d.integration_server", nativeQuery = true)
	List<Deployment> findLatestDeploymentByApplicationNameAndEnvironment(@Param("env") String env, @Param("app") String app);
}
