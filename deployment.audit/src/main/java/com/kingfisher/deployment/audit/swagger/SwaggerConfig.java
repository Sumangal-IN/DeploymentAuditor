package com.kingfisher.deployment.audit.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket deploymentAuditApi() {
		List<SecurityScheme> schemeList = new ArrayList<>();
		schemeList.add(new BasicAuth("basicAuth"));
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.kingfisher.deployment.audit.controller")).build().apiInfo(metaData()).securitySchemes(schemeList).useDefaultResponseMessages(false);
	}

	private ApiInfo metaData() {
		return new ApiInfo("Deployment Audit API", "Swagger Definition for the Inventory Application API", "1.0.0", "", new Contact("Sumangal Mandal", "http://kingfisher.com", "sumangal.mandal@kingfisher.com"), "", "", new ArrayList<>());
	}
}