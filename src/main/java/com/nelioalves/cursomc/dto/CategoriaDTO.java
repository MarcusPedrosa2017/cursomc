package com.nelioalves.cursomc.dto;

import java.io.Serializable;

import com.nelioalves.cursomc.domain.Categoria;

public class CategoriaDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	
	public CategoriaDTO() {		
	}

	/*
	 * construtor criado para que no CategoriaResource no metodo de listar as categorias seja realizada a mudanca de Categoria 
	 * para CategoriaDTO, pois ao retornar uma lista de categorias, devido ao relacionamento e retornado os produtos dessas categorias
	 * e so queremos ver as categorias, nao seus produtos
	*/
	public CategoriaDTO(Categoria obj) {		
		this.id = obj.getId();
		this.nome = obj.getNome();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
}
