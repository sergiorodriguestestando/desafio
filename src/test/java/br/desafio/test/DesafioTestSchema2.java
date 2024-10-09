package br.desafio.test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class DesafioTestSchema2 {
	
	String URL = "https://dummyjson.com";
	public static Response response;
	public static Long ID;
	public static String TOKEN;
	public static Map<String, Object> params;
	

	
	@Test
	public  void validarBuscarToken() throws SQLException {
		
		
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
	
	
	@Test
	public  void validarBuscarUsuarios() throws SQLException {
		
		
		
		
				given()
					.contentType(ContentType.JSON)
			   .when()
			        .get("https://dummyjson.com/users")
				.then()
			         .log()
		             .all()
		            .statusCode(200)
		            .assertThat().body(matchesJsonSchemaInClasspath("getUsers.json"));
	}
	
	
	@Test
	public  void validarTestRotaCorreta() throws SQLException {
		
		given()
			.log()
			.all()		
		.when()
			.get(URL+"/test")
		.then()
			.log()
			.all()
			.statusCode(200)
			.assertThat().body(matchesJsonSchemaInClasspath("test.json"));
	}
	
	@Test
	public void validarBuscarProdutosStatus200() throws SQLException {
		
		
Map<String, String> login = new HashMap<>();
		
		login.put("username", "emilys");
		login.put("password", "emilyspass");
		response = 
				given()
					.contentType(ContentType.JSON)
					.body(login)
			   .when()
			        .post("https://dummyjson.com/auth/login");
	response.then()
			         .log()
		             .all();
		            TOKEN = response.jsonPath().getString("token");
		
		
	given()
	        .contentType(ContentType.JSON)
			.header("Authorization","Bearer "+TOKEN)
	.when()

			.get(URL+"/auth/products")
	.then()
			.log()
			.all()
			.assertThat().body(matchesJsonSchemaInClasspath("getAuthProducts.json"));
	
}
	
	
	@Test
	public void validarAddProdutosOK() throws SQLException {
		
		params = new HashMap<String, Object>();
		params.put("title", "Perfume Oil");
		params.put("description", "Mega Discount, Impression of A...");
		params.put("price", 13);
		params.put("discountPercentage", 8.4);
		params.put("rating", 4.26);
		params.put("stock", 65);
		params.put("brand", "Impression of Acqua Di Gio");
		params.put("category", "fragrances");
		params.put("thumbnail", "https://i.dummyjson.com/data/products/11/thumnail.jpg");
		
		
				given()
					.contentType(ContentType.JSON)
					.body(params)
			   .when()
			        .post("https://dummyjson.com/products/add")
				.then()
			         .log()
		             .all()
		             .statusCode(201)
		             .assertThat().body(matchesJsonSchemaInClasspath("addProduct.json"))
		             .body("id",is(not(nullValue())))
		             .body("title",is("Perfume Oil"))
		             .body("category",is("fragrances"));
		           
	}
	
	@Test
	public  void validarBuscarTodosProdutos() throws SQLException {
		
		given()
			.log()
			.all()		
		.when()
			.get(URL+"/products")
		.then()
			.log()
			.all()
			.statusCode(200)
			.assertThat().body(matchesJsonSchemaInClasspath("getAllProducts.json"))
			.body("limit",is(30));
	}
	
	
	
	@Test
	public  void validarBuscarTodosProdutosPorId() throws SQLException {
		
		given()
			.pathParam("id", 1)
			.log()
			.all()		
		.when()
			.get(URL+"/products/{id}")
		.then()
			.log()
			.all()
			.statusCode(200)
			.assertThat().body(matchesJsonSchemaInClasspath("getProductById.json"))
			.body("id",is(1))
			.body("tags", hasItem("beauty"));
	}

	
	
}
