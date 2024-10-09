package br.desafio.util;

import java.util.Random;

public class Utils {
	
	
	public String ordemCompraAleatoria(String tipoPedido) {
		Random aleatorio = new Random(); 
		int nrInicial = 0;
		int nrFinal = 1000000;
		int sorteio = aleatorio.nextInt(nrFinal - nrInicial); // Tamanho do intervalo
		return tipoPedido+sorteio + nrInicial;
		//testando
	}
	
	
	

}
