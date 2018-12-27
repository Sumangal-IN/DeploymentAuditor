package com.kingfisher.deployment.audit.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.kingfisher.deployment.audit.constant.ApplicationConstant;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	DataSource dataSource;

	@Autowired
	CustomPasswordEncoder customPasswordEncoder;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/h2/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http
         .httpBasic()
         .and()
         .authorizeRequests()
         .antMatchers("/deployment/audit").hasAnyRole("ADMIN","JENKINS")
         .antMatchers("/deployment/report*").hasAnyRole("ADMIN","AUDITOR")
         .antMatchers("/user/**").hasRole("ADMIN")
         .anyRequest().authenticated();
	} 	

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery("select username,password, enabled from users where username=?").authoritiesByUsernameQuery("select username, role from user_roles where username=?").passwordEncoder(customPasswordEncoder);
	}

}