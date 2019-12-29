package com.nelioalves.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.nelioalves.cursomc.services.DBService;
import com.nelioalves.cursomc.services.EmailService;
import com.nelioalves.cursomc.services.MockEmailService;
import com.nelioalves.cursomc.services.SmtpEmailService;

/*A ANOTACAO @Configuration INDICA QUE E UMA CLASSE DE CONFIGURACAO
 * E A ANOTACAO @Profile INDICA QUE PERTENCE A UM PROFILE ESPECIFICO QUE E DEFINIDO EM SEU ARQUIVO.PROPERTIES DE MESMO NOME
 * QUE NESTE CASO E dev*/
@Configuration
@Profile("dev")
public class DevConfig {
	
	@Autowired
	private DBService dbService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		
		if(!"create".equals(strategy)) {
			return false;
		}
		
		dbService.instantiateTestDatabase();		
		return true;
	}
	
	/*BEAN CRIADO PARA QUE SE POSSA UTILIZAR O ENVIO DE EMAIL DE CONFIRMACAO DE PEDIDO NA CLASSE PedidoService*/
	@Bean
	public SmtpEmailService emailService() {
		return new SmtpEmailService();
	}
	
}
