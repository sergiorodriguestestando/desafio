package br.desafio.test;

import static io.restassured.RestAssured.given;
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

public class UsuarioTest {
	
	String URL = "https://serverest.dev";
	public static Response response;
	public static String _ID;
	public static String TOKEN;
	public static Map<String, Object> params;




	public  String getTOKEN() throws SQLException {

		Map<String, String> login = new HashMap<>();
		login.put("email", "testeQA100.testeQA100@hotmail.com");
		login.put("password", "teste");
		response =
				given()
						.contentType(ContentType.JSON)
						.body(login)
						.when()
						.post(URL+"/login");
		response.then()
				.log()
				.all()
				.statusCode(200);
        return TOKEN = response.jsonPath().getString("authorization");
    }

	@Test
	public  void validarLoginUsuariosInvalido() throws SQLException {

		Map<String, String> login = new HashMap<>();

		login.put("email", "fulano@qa.com");
		login.put("password", "testeQA");
		response =
				given()
						.contentType(ContentType.JSON)
						.body(login)
				.when()
						.post(URL+"/login");
		response.then()
						.log()
						.all()
						.statusCode(401)
						.body("message",is("Email e/ou senha inválidos"))
		;

	}


	@Test
	public  void validarGetUsuarios() throws SQLException {
		
		given()
			.log()
			.all()		
		.when()
			.get(URL+"/usuarios")
		.then()
			.log()
			.all()
			.statusCode(200)
				.body("usuarios._id",is(not(nullValue())))
		;
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
				.body("message",is("Usuário não encontrado"))
		;
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
				.body("id",is("id deve ter exatamente 16 caracteres alfanuméricos"))
		;
	}



	@Test
	public  void validarCadastroUsuarioEmailExistente() throws SQLException {



		Map<String, String> login = new HashMap<>();

		login.put("nome", "Sérgio QA");
		login.put("email", "beltrano@qa.com.br");
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


	}



	@Test
	public  void validarCadastroExclusaoUsuario() throws SQLException {
		int randomNum = (int)(Math.random() * 1001);
		Map<String, String> login = new HashMap<>();

		login.put("nome", "sergio");
		login.put("email", "sergio"+randomNum+"@qa.com.br");
		login.put("password", "teste");
		login.put("administrador", "false");

		response =
				given()
						.contentType(ContentType.JSON)
						.body(login)
						.when()
						.post(URL+"/usuarios");
		response.then()
				.log()
				.all()
				.statusCode(201)
				.body("message",is("Cadastro realizado com sucesso"));
		_ID = response.jsonPath().getString("_id");

		given()
				.pathParam("_id", _ID)
				.log()
				.all()
		.when()
				.delete(URL+"/usuarios/{_id}")
		.then()
				.log()
				.all()
				.statusCode(200)
				.body("message",is("Registro excluído com sucesso"));
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
