package com.nelioalves.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private Environment env;
	
	//DEFINICAO DE QUAIS CONTEXTOS ESTARAO LIBERADOS	
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**"			
	};
	
	//DEFINICAO DOS RECURSOS QUE SERAO PERMITIDOS APENAS GET
	private static final String[] PUBLIC_MATCHERS_GET = {			
			"/produtos/**",
			"/categorias/**",
			"/clientes/**"
	};
	
	//SOBRESCREVENDO O METODO DE CONFIGURACAO
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		
		//NECESSARIO PARA ACESSAR O BANCO H2, ISTO E DEFINIDO PELO FRAMEWORK
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		//CHAMADA DO METODO corsConfigurationSource ABAIXO E DESABILITANDO A PROTECAO DE ATAQUES CSRF
		http.cors().and().csrf().disable();
		http.authorizeRequests()
			//PERMITE OS CONTEXTOS DO VETOR ACIMA SEM AUTENTICACAO SOMENTE PARA REQUESTS DO TIPO GET
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
			//PERMITE OS CONTEXTOS DO VETOR ACIMA SEM AUTENTICACAO
			.antMatchers(PUBLIC_MATCHERS).permitAll()
			//PARA TODOS OUTROS NECESSITA AUTENTICACAO
			.anyRequest().authenticated();
		
		//COLOCAMOS ESSA CONFIGURACAO PARA QUE O SPRING SECURITY NAO CRIE SESSAO DE USUARIO E NAO POSSIBILITE O ATAQUE CSRF
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	//NECESSARIO PARA PODER RECEBER REQUEST DE MULTIPLAS ORIGENS
	@Bean
	CorsConfigurationSource corsConfigurationSource(){
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
	
	//BEAN CRIADO PARA PODER FAZER A ENCRIPTACAO DA SENHA DO USUARIO
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
