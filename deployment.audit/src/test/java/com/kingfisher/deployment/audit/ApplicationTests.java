package com.kingfisher.deployment.audit;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {
	@LocalServerPort
	private int port;
	@Test
	public void testApplicationHealth() {	        
		 given().
		 port(port).
		    when().
		        get("/actuator/health").
		    then().
		        assertThat().
		        statusCode(200).
		        body("status", equalTo("UP")).
		    and().
		        contentType(ContentType.JSON);
	}
}
