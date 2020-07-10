package com.nelioalves.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nelioalves.cursomc.security.JWTAuthenticationFilter;
import com.nelioalves.cursomc.security.JWTAuthorizationFilter;
import com.nelioalves.cursomc.security.JWTUtil;

@Configuration
@EnableWebSecurity
//esta anotacao permiti utilizar anotacoes nos metodos para fazer pre validacoes de perfis de acesso
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private Environment env;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	//DEFINICAO DE QUAIS CONTEXTOS ESTARAO LIBERADOS	
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**"			
	};
	
	//DEFINICAO DOS RECURSOS QUE SERAO PERMITIDOS APENAS GET
	private static final String[] PUBLIC_MATCHERS_GET = {			
			"/produtos/**",
			"/categorias/**"			
	};
	
	//DEFINICAO DOS RECURSOS QUE SERAO PERMITIDOS PARA USUARIOS NAO CADASTRADOS VIA POST
	private static final String[] PUBLIC_MATCHERS_POST = {			
			"/clientes",			
			"/auth/forgot/**"	
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
			//PERMITE OS CONTEXTOS DO VETOR ABAIXO SEM AUTENTICACAO SOMENTE PARA REQUESTS DO TIPO GET
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
			//PERMITE OS CONTEXTOS DO VETOR ABAIXO SEM AUTENTICACAO
			.antMatchers(PUBLIC_MATCHERS).permitAll()
			//PERMITE OS CONTEXTOS DO VETOR ABAIXO SEM AUTENTICACAO PARA POST 
			.antMatchers(PUBLIC_MATCHERS_POST).permitAll()
			//PARA TODOS OUTROS NECESSITA AUTENTICACAO
			.anyRequest().authenticated();
		
		//ADICIONANDO O FILTRO DO /LOGIN
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		
		//ADICIONANDO O FILTRO DO AUTORIZADOR
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		
		//COLOCAMOS ESSA CONFIGURACAO PARA QUE O SPRING SECURITY NAO CRIE SESSAO DE USUARIO E NAO POSSIBILITE O ATAQUE CSRF
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{		
		/*  este metodo esta sendo sobrescrito para que possamos indicar para o Spring qual o algoritmo de encriptacao vamos
		 *  usar e qual UserDetail
		 *  Injetamos a interface do Spring userDetailsService, porem ele ira automaticamente usar a implementacao do projeto
		 *  para esta interface que e: userDetailsServiceImpl
		 */
		auth.userDetailsService(userDetailsService).passwordEncoder(this.bCryptPasswordEncoder());
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
