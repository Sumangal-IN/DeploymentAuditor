package com.kingfisher.deployment.audit.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kingfisher.deployment.audit.security.codec.CustomPasswordEncoder;
import com.kingfisher.deployment.audit.user.model.User;
import com.kingfisher.deployment.audit.user.model.UserRole;
import com.kingfisher.deployment.audit.user.repository.UserRepository;
import com.kingfisher.deployment.audit.user.repository.UserRoleRepository;

import io.swagger.annotations.Api;

@RestController
@Api(value = "User")
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	CustomPasswordEncoder customPasswordEncoder;

	@GetMapping(value = "/createUser", produces = "application/json")
	public void createUser(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("roles") List<String> roles) {
		// Remove the user and his role if already exists
		if (!userRepository.findById(username).equals(Optional.empty()))
			removeUser(username);
		// create the user
		userRepository.save(new User(username, customPasswordEncoder.encode(password)));
		// assign role to the user
		for (String role : roles) {
			userRoleRepository.save(new UserRole(username, role));
		}
	}

	@GetMapping(value = "/removeUser", produces = "application/json")
	public void removeUser(@RequestParam("username") String username) {
		// Remove the user and his role if already exists
		if (!userRepository.findById(username).equals(Optional.empty())) {
			// remove the roles assigned to the user
			userRoleRepository.deleteByUsername(username);
			// remove the user master data
			userRepository.deleteById(username);
		}
	}

}
