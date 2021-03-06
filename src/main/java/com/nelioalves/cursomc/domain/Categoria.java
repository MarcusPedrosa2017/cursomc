package com.nelioalves.cursomc.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

//ANOTAÇÃO PARA INDICAR QUE É UMA CLASSE BASEADA EM JPA
@Entity
public class Categoria implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//ANOTAÇÃO QUE INDICA QUE É A PRIMARY KEY DESTA ENTIDADE DE BANCO
	@Id
	//ANOTAÇÃO QUE INDICA QUE A CHAVE PRIMARYA SERÁ GERÊNCIADA PELO SPRING
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String nome;
	
	/*
	 *COMO JA FOI FEITO O MAPEAMENTO DO RELACIONAMENTO NA TABELA PRODUTO, NAO E NECESSARIO FAZER AQUI NOVAMENTE, BASTA REFERENCIA O ATRIBUTO
	 *NO QUAL FOI MAPEADO NA OUTRA CLASSE COM PELO ATRIBUTO mappedBy NA ASSOCIACAO @ManyToMany 
	 *
	 *A ANOTACAO @JsonManagedReference SERVE PARA EVITAR A REFERENCIA CIRCULAR NA HORA DE SERIALIZAR PARA JSON O RETORNO, ELA TRABALHA EM 
	 *CONJUNTO COM A ANOTACAO @JsonBackReference. NO ATRIBUTO DE RELACIONAMENTO DA OUTRA CLASSE
	 *
	 *@JsonManagedReference FOI RETIRADA DEPOIS PELO AUTOR, POIS EM REQUISICOES DO REST HOUVE PROBLEMAS, FOI RETIRADA E NO LADO QUE TEM
	 *A ANOTACAO @BackReference FOI TRACADA PELA @JsonIgonore QUE FEZ O MESMO EFEITO
	*/
	
	@ManyToMany(mappedBy="categorias")
	private List<Produto> produtos = new ArrayList<>();
	
	public Categoria() {
	}

	public Categoria(Integer id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
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
	
	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}		
	
}
