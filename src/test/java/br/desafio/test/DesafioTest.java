package br.desafio.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.object.IsCompatibleType;
import org.junit.Test;


import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class DesafioTest {
	
	String URL = "https://dummyjson.com";
	public static Response response;
	public static Long ID;
	public static String TOKEN;
	public static Map<String, Object> params;
	
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
			.statusCode(200);
	}
	
	@Test
	public  void validarTestRotaIncorreta() throws SQLException {
		
		
		given()
			.log()
			.all()		
		.when()
			.get(URL+"/testes")
		.then()
			.log()
			.all()
			.statusCode(404);
	}
	
	@Test
	public  void buscarTokenOK() throws SQLException {
		
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
		            .statusCode(200);
	}
	

	@Test
	public  void buscarTokenUsuarioRotaErrada403() throws SQLException {
	
	Map<String, String> login = new HashMap<>();
	
	login.put("username", "emilys");
	login.put("password", "emilyspass");
	response = 
			given()
				.contentType(ContentType.JSON)
				.body(login)
		   .when()
		        .post("https://dummyjson.com/auth");
	response.then()
		         .log()
	             .all()
	             .statusCode(403)
	             ;	
	            TOKEN = response.jsonPath().getString("refreshToken");
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
			.all();
	
}
	
	@Test
	public void validarBuscarProdutosStatus200TokenRefresh() throws SQLException {
		
		
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
		            TOKEN = response.jsonPath().getString("refreshToken");
		
		
	given()
	        .contentType(ContentType.JSON)
			.header("Authorization","Bearer "+TOKEN)
	.when()

			.get(URL+"/auth/products")
	.then()
			.log()
			.all()
			.statusCode(200);
	
}
	
	@Test
	public void BugBuscarProdutosRotaIncorreta() throws SQLException {
		
		
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
			//auth/products
			.get(URL+"/auth/product")
	.then()
			.log()
			.all();
		
	given()
	        .contentType(ContentType.JSON)
			.header("Authorization","Bearer "+TOKEN)
	.when()

			.get(URL+"/auth/product")
	.then()
			.log()
			.all()
			.statusCode(200);
	
}
	
	@Test
	public void validarBuscarProdutosTipoAutorizacaoStatus500() throws SQLException {
		
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
	        //Bearer
			.header("Authorization","JWT "+TOKEN)
	.when()
			.get(URL+"/auth/products")
	.then()
			.log()
			.all()
			.statusCode(500);
	
}
	
	@Test
	public void validarBuscarProdutosComUsuárioInexiste401() throws SQLException {
		
		
	Map<String, String> login = new HashMap<>();
		
		login.put("username", "emilysXXX");
		login.put("password", "emilyspass");
		response = 
				given()
					.contentType(ContentType.JSON)
					.body(login)
			   .when()
			        .post("https://dummyjson.com/auth/login");
			response.then();
			         
		             	
		            TOKEN = response.jsonPath().getString("token");
		
		
	given()
	        .contentType(ContentType.JSON)
			.header("Authorization","Bearer "+TOKEN)
	.when()

			.get(URL+"/auth/products")
	.then()
			.log()
			.all()
			.statusCode(401)
			 .body("name",is("JsonWebTokenError"))
             .body("message",is("Invalid/Expired Token!"));
	
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
		             .body("id",is(not(nullValue())))
		             .body("title",is("Perfume Oil"))
		             .body("category",is("fragrances"));
		           
	}
	
	
	@Test
	public void BugvAddProdutosCamposNumericosComString() throws SQLException {
		
		params = new HashMap<String, Object>();
		params.put("title", "Perfume Oil");
		params.put("description", "Mega Discount, Impression of A...");
		params.put("price", "teste1");
		params.put("discountPercentage", "teste1");
		params.put("rating", "teste1");
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
		             .body("price", equalTo(13.2))
		             .body("rating",equalTo(13.5));
		           
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
			.body("id",is(1))
			.body("tags", hasItem("beauty"));
	}
	
	@Test
	public  void validarBuscarTodosProdutosPorIdInexistente() throws SQLException {
		
		given()
			.pathParam("id", 10000)
			.log()
			.all()		
		.when()
			.get(URL+"/products/{id}")
		.then()
			.log()
			.all()
			.statusCode(404)
			.body("message",is("Product with id '10000' not found"));
	}
	
	
	

	
	
}
