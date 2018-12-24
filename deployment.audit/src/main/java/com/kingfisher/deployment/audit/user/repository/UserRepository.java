package com.kingfisher.deployment.audit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kingfisher.deployment.audit.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
