package com.nelioalves.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.nelioalves.cursomc.services.DBService;

/*A ANOTACAO @Configuration INDICA QUE E UMA CLASSE DE CONFIGURACAO
 * E A ANOTACAO @Profile INDICA QUE PERTENCE A UM PROFILE ESPECIFICO QUE E DEFINIDO EM SEU ARQUIVO.PROPERTIES DE MESMO NOME
 * QUE NESTE CASO E test*/
@Configuration
@Profile("test")
public class TestConfig {
	
	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {		
		dbService.instantiateTestDatabase();		
		return true;
	}

	
}
