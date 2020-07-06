package com.nelioalves.cursomc.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nelioalves.cursomc.domain.enums.Perfil;

public class UserSS implements UserDetails{
	
	private static final long serialVersionUID = 1L;
	/*
	 * CLASSE CRIADA PARA QUE SE POSSA IMPLEMENTAR A CLASSE DE CONTRATO REQUERIDA PELO SPRING SECURITY PARA 
	 * SE TRABALHAR COM GERENCIMENTO DE USUÁRIOS (UserDetails) 
	 */
	private Integer id;
	private String email;
	private String senha;
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserSS() {		
	}
	
	public UserSS(Integer id, String email, String senha, Set<Perfil> perfis) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		//transformando a lista de perfis para o tipo que o Spring requer
		this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority(x.getDescricao())).collect(Collectors.toList());
	}



	public Integer getId() {
		return id;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		//colocamos por default apenas porque nao queremos implementar isso agora
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		//colocamos por default apenas porque nao queremos implementar isso agora
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		//colocamos por default apenas porque nao queremos implementar isso agorab
		return true;
	}

	@Override
	public boolean isEnabled() {
		//colocamos por default apenas porque nao queremos implementar isso agora
		return true;
	}
	
}
