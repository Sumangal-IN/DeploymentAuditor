package com.kingfisher.deployment.audit.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	DataSource dataSource;

	@Autowired
	CustomPasswordEncoder customPasswordEncoder;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
		.antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/h2/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/user/**")
		.hasAnyRole("ADMIN")
		.anyRequest()
		.authenticated()
		.and()
		.httpBasic();
		
		http
		.authorizeRequests()
		.antMatchers("/deployment/**")
		.hasAnyRole("ADMIN", "USER")
		.anyRequest()
		.authenticated()
		.and()
		.httpBasic();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.inMemoryAuthentication()
		.withUser("admin")
		.password("$2a$12$BHMZFp1aQs6NJ1zpPS4zjOWkuI3I9FC3AW8k10PUpKk8HnYOkrqUG")
		.roles("ADMIN");
		
		auth
		.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery("select username,password, enabled from users where username=?")
		.authoritiesByUsernameQuery("select username, role from user_roles where username=?")
		.passwordEncoder(customPasswordEncoder);
	}

}