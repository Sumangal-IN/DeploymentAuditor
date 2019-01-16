package com.kingfisher.deployment.audit;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
	@Test
	public void testApplicationHealth() {	        
		 given().
		    when().
		        get("http://localhost:9090/actuator/health").
		    then().
		        assertThat().
		        statusCode(200).
		        body("status", equalTo("UP")).
		    and().
		        contentType(ContentType.JSON);
	}
}
