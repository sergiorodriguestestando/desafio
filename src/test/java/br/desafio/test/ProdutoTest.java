package br.desafio.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProdutoTest {
	
	String URL = "https://serverest.dev";
	public static Response response;
	public static String _ID;
	public static String TOKEN;
	public static Map<String, Object> params;
	public UsuarioTest usuarioTest = new UsuarioTest();




	@Test
	public  void validarGetProdutos() throws SQLException {
		
		given()
			.log()
			.all()		
		.when()
			.get(URL+"/produtos")
		.then()
			.log()
			.all()
			.statusCode(200)
				.body("produtos._id",is(not(nullValue())))
		;
	}


	@Test
	public  void validarCadastrarProduto() throws SQLException {
		int randomNum = (int)(Math.random() * 1001);
		Map<String, String> prod = new HashMap<>();
		prod.put("nome", "monitor"+randomNum);
		prod.put("preco", "200");
		prod.put("descricao", "monitor"+randomNum);
		prod.put("quantidade", "2");

		Response responseProd =
					   given()
							.headers("authorization", usuarioTest.getTOKEN())
							.contentType(ContentType.JSON)
							.body(prod)
						.when()
								.post(URL + "/produtos");
		responseProd.then()
								.log()
								.all()
								.statusCode(201);
		_ID = responseProd.jsonPath().getString("_id");

		given()
				.pathParam("_id", _ID)
				.headers("authorization", usuarioTest.getTOKEN())
				.log()
				.all()
				.when()
				.delete(URL+"/produtos/{_id}")
				.then()
				.log()
				.all()
				.statusCode(200)
				.body("message",is("Registro excluído com sucesso"));

	}


	@Test
	public  void validarExcluirProdutoNoCarrinho() throws SQLException {


		given()
				.pathParam("_id", "K6leHdftCeOJj8BJ")
				.headers("authorization", usuarioTest.getTOKEN())
				.log()
				.all()
		.when()
				.delete(URL+"/produtos/{_id}")
		.then()
				.log()
				.all()
				.statusCode(400)
				.body("message",is("Não é permitido excluir produto que faz parte de carrinho"));

	}

	@Test
	public  void validarEditarProduto() throws SQLException {


		Map<String, String> produtoAtualizado = new HashMap<>();
		produtoAtualizado.put("nome", "caneta");
		produtoAtualizado.put("preco", "111");
		produtoAtualizado.put("descricao", "caneta");
		produtoAtualizado.put("quantidade", "22");


		given()
				.headers("authorization", usuarioTest.getTOKEN())
				.pathParam("_id", "K6leHdftCeOJj8BJ")
				.log()
				.all()
		.when()
				.contentType(ContentType.JSON)
				.body(produtoAtualizado)
				.put(URL+"/produtos/{_id}")
		.then()
				.log()
				.all()
				.statusCode(200)
				.body("message",is("Registro alterado com sucesso"));
	}



	public  String getProdutoID(String _id) throws SQLException {


		Response response = given()
				.headers("authorization", usuarioTest.getTOKEN())
				.pathParam("_id", _id)
				.log()
				.all()
		.when()
				.contentType(ContentType.JSON)
				.get(URL+"/produtos/{_id}");
response.then()
				.log()
				.all()
				.statusCode(200);
				//.body("message",is("Produto encontrado"));
				return  _ID = response.jsonPath().getString("_id");
	}

	@Test
	public  void validarExcluirTokenExpirado() throws SQLException {

		given()
				.pathParam("_id", "K6leHdftCeOJj8BJ")
				.headers("authorization", TOKEN+"XXXX")
				.log()
				.all()
		.when()
				.delete(URL+"/produtos/{_id}")
		.then()
				.log()
				.all()
				.statusCode(401)
				.body("message",is("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"));

	}




	@Test
	public  void validarBuscarUsuariosPorId() throws SQLException {

		given()
				.pathParam("_id", "0uxuPY0cbmQhpEz1")
				.log()
				.all()
		.when()
				.get(URL+"/usuarios/{_id}")
		.then()
				.log()
				.all()
				.statusCode(200);
	}


	@Test
	public  void validarBuscarUsuariosPorIdInvalido() throws SQLException {

		given()
				.pathParam("_id", "0uxuPY0cbmQhpEzz")
				.log()
				.all()
		.when()
				.get(URL+"/usuarios/{_id}")
		.then()
				.log()
				.all()
				.statusCode(400)
				.body("message",is("Usuário não encontrado"));
	}


	@Test
	public  void validarBuscarUsuariosPorIdQuantidadeMaiorQueDezesseis() throws SQLException {

		given()
				.pathParam("_id", "0uxuPY0cbmQhpEzzAAAAAAAAA")
				.log()
				.all()
		.when()
				.get(URL+"/usuarios/{_id}")
		.then()
				.log()
				.all()
				.statusCode(400)
				.body("id",is("id deve ter exatamente 16 caracteres alfanuméricos"));
	}

	@Test
	public  void validarCadastroUsuarioEmailExistente() throws SQLException {


		Map<String, String> login = new HashMap<>();
		login.put("nome", "sergio da Silva");
		login.put("email", "sergio@qa.com.br");
		login.put("password", "teste");
		login.put("administrador", "true");

		response =
				given()
						.contentType(ContentType.JSON)
						.body(login)
				.when()
						.post(URL+"/usuarios");
		response.then()
				.log()
				.all()
				.statusCode(400)
				.body("message",is("Este email já está sendo usado"));
		;

	}




	@Test
	public  void validarExcluirUsuarioComCarinho() throws SQLException {

		given()
				.pathParam("_id", "0uxuPY0cbmQhpEz1")
				.log()
				.all()
		.when()
				.delete(URL+"/usuarios/{_id}")
		.then()
				.log()
				.all()
				.statusCode(400)
				.body("message",is("Não é permitido excluir usuário com carrinho cadastrado"));
	}



	@Test
	public  void validarExcluirUsuarioInexistente() throws SQLException {

		given()
				.pathParam("_id", "0uxuPY0cbmQhpEz1XXXXXXX")
				.log()
				.all()
		.when()
				.delete(URL+"/usuarios/{_id}")
		.then()
				.log()
				.all()
				.statusCode(200)
				.body("message",is("Nenhum registro excluído"));
	}




	@Test
	public  void validarEditarUsuario() throws SQLException {


		Map<String, String> usuarioAtualizado = new HashMap<>();
		usuarioAtualizado.put("nome", "teste2");
		usuarioAtualizado.put("email", "teste2@qa.com.br");
		usuarioAtualizado.put("password", "teste");
		usuarioAtualizado.put("administrador", "true");


		given()
				.pathParam("_id", "0uxuPY0cbmQhpEz1")
				.log()
				.all()
		.when()
				.contentType(ContentType.JSON)
				.body(usuarioAtualizado)
				.put(URL+"/usuarios/{_id}")
		.then()
				.log()
				.all()
				.statusCode(200)
				.body("message",is("Registro alterado com sucesso"));

	}


}
