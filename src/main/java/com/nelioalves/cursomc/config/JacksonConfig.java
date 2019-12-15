package com.nelioalves.cursomc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelioalves.cursomc.domain.PagamentoComBoleto;
import com.nelioalves.cursomc.domain.PagamentoComCartao;

/*
 * ESSA CLASSE DE CONFIGURACAO FOI CRIADA PARA QUE O JACKSON TENHA UM MAPPER COM OS TIPOS DE PAGAMENTO QUE ELE VAI GERENCIAR*/
@Configuration
public class JacksonConfig {
	
	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {
			public void configure(ObjectMapper objectMapper) {
				objectMapper.registerSubtypes(PagamentoComBoleto.class);
				objectMapper.registerSubtypes(PagamentoComCartao.class);
				super.configure(objectMapper);
			}
		};
		return builder;
	}
}
