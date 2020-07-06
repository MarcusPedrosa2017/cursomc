package com.nelioalves.cursomc.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelioalves.cursomc.dto.CredenciasDTO;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	/*
	 * classe criada para que o spring saiba que precisa interceptar as requisicoes de login para o "/login"
	 * por isso ela extende UsernamePasswordAuthenticationFilter
	 */	
	
	private AuthenticationManager authenticationManager;
    
    private JWTUtil jwtUtil;
	
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
    	//setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			CredenciasDTO creds = new ObjectMapper().readValue(request.getInputStream(), CredenciasDTO.class);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());
	        
			/*
			 * aqui e onde o spring verifica se o usuario e senha sao validos de acordo com as configuracoes que implementamos
			 * nas classes UserSS e UserDetailServiceImpl	
			*/
	        Authentication auth = authenticationManager.authenticate(authToken);
	        return auth;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}	
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		
		//fazendo casting do usuario autenticado para o modelo UserSS
		String username = ((UserSS) auth.getPrincipal()).getUsername();
		//gerando o token para o usuario
        String token = jwtUtil.generateToken(username);
        //acrescentando o token no header do response
        response.addHeader("Authorization", "Bearer " + token);
	}
}
