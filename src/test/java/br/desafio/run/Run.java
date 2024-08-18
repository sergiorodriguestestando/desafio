package br.desafio.run;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.desafio.test.DesafioTest;
import br.desafio.test.DesafioTestSchema;



@RunWith(Suite.class)
@SuiteClasses({
	
	DesafioTest.class,
	DesafioTestSchema.class
})

public class Run {



}
