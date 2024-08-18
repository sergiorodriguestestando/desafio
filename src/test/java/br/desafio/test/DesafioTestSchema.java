package br.desafio.test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


import static org.hamcrest.Matchers.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matcher;
import org.hamcrest.object.IsCompatibleType;
import org.junit.Test;


import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class DesafioTestSchema {
	
	String URL = "https://dummyjson.com";
	public static Response response;
	public static Long ID;
	public static String TOKEN;
	public static Map<String, Object> params;
	

	
	@Test
	public  void validarBuscarToken() throws SQLException {
		//teste
		
		Map<String, String> login = new HashMap<>();
		
		login.put("username", "emilys");
		login.put("password", "emilyspass");
		
				given()
					.contentType(ContentType.JSON)
					.body(login)
			   .when()
			        .post("https://dummyjson.com/auth/login")
				.then()
			         .log()
		             .all()
		            .statusCode(200)
		            .assertThat().body(matchesJsonSchemaInClasspath("getToken.json"));
	}
	

	
	
}
