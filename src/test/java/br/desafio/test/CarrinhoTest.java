package br.desafio.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CarrinhoTest {
	
	String URL = "https://serverest.dev";
	public static Response response;
	public static String _ID;
	public static String TOKEN;
	public static Map<String, Object> params;
	public UsuarioTest usuarioTest = new UsuarioTest();
	public ProdutoTest produtoTest = new ProdutoTest();


	@Test
	public  void validarGetCarrinhos() throws SQLException {
		
		given()
			.headers("authorization", usuarioTest.getTOKEN())
			.log()
			.all()		
		.when()
			.get(URL+"/carrinhos")
		.then()
			.log()
			.all()
			.statusCode(200)
			.body("carrinhos.produtos._idProduto",is(not(nullValue())));
	}

	@Test
	public  void validarGetCarrinhosPorId() throws SQLException {

		given()
				.headers("authorization", usuarioTest.getTOKEN())
				.pathParam("_id", "qbMqntef4iTOwWfg")
				.log()
				.all()
		.when()
				.get(URL+"/carrinhos/{_id}")
		.then()
				.log()
				.all()
				.statusCode(200)
				.body("_id",is(not(nullValue())));
	}

	@Test
	public  void validarGetCarrinhosPorIdInexisitente() throws SQLException {

		given()
				.headers("authorization", usuarioTest.getTOKEN())
				.pathParam("_id", "qbMqntef4iTOwWfh")
				.log()
				.all()
		.when()
				.get(URL+"/carrinhos/{_id}")
		.then()
				.log()
				.all()
				.statusCode(400)
				.body("message",is("Carrinho não encontrado"));
	}



	@Test
	public  void validarCadastrarCarrinho() throws SQLException {
		int randomNum = (int)(Math.random() * 1001);
		Map<String, String> prod = new HashMap<>();
		prod.put("nome", "monitor"+randomNum);
		prod.put("preco", "200");
		prod.put("descricao", "monitor"+randomNum);
		prod.put("quantidade", "10");

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
						_ID = produtoTest.getProdutoID("TgIISt75ogJMvkOT");

		Map<String, String> carrinho = new HashMap<>();
		carrinho.put("idProduto", _ID);
		carrinho.put("quantidade", "2");


				given()
						.headers("authorization", usuarioTest.getTOKEN())
						.contentType(ContentType.JSON)
						.body(carrinho)
				.when()
						.post(URL + "/carrinhos");
	responseProd.then()
				.log()
				.all()
				.statusCode(201);

	}


	@Test
	public  void validarExcluirProdutoNoCarrinho() throws SQLException {


		given()
				.headers("authorization", usuarioTest.getTOKEN())
				.log()
				.all()
		.when()
				.delete(URL+"/carrinhos/concluir-compra")
		.then()
				.log()
				.all()
				.statusCode(200);
	}
}
