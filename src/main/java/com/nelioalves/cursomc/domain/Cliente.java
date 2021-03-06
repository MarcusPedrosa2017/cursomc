package com.nelioalves.cursomc.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nelioalves.cursomc.domain.enums.Perfil;
import com.nelioalves.cursomc.domain.enums.TipoCliente;

@Entity
public class Cliente implements Serializable{	
	
	private static final long serialVersionUID = 1L;
	
	@Id		
	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	private Integer id;
	
	@Column(unique=true)
	private String nome;
	
	private String email;
	private String cpfCnpj;
	//foi alterado de TipoCliente para Integer, para armazenar so o codigo, por isso mudamos o get e set deles
	private Integer tipo;
	
	@JsonIgnore//para que não retorne a senha na consulta
	private String senha;
	
	/*
	@JsonManagedReference FOI RETIRADA DEPOIS PELO AUTOR, POIS EM REQUISICOES DO REST HOUVE PROBLEMAS, FOI RETIRADA E NO LADO QUE TEM
	 *A ANOTACAO @BackReference FOI TRACADA PELA @JsonIgonore QUE FEZ O MESMO EFEITO
	
	FOI INCLUIDA O ATRIBUTO cascade PARA QUE QUANDO OCORRER UMA DELECAO DE CLIENTE, O SPRING FACA O DELETE DE TODOS OS ENDERECOS
	ASSOCIACOES AUTOMATICAMENTE 
	*/
	@OneToMany(mappedBy="cliente", cascade=CascadeType.ALL)
	private List<Endereco> endrecos = new ArrayList<>();
	
	@ElementCollection
	@CollectionTable(name="TELEFONE")
	private Set<String> telefone = new HashSet<>();
	
	@ElementCollection(fetch=FetchType.EAGER)//obriga a trazer o perfil automaticamente	
	@CollectionTable(name="PERFIS")
	private Set<Integer> perfis = new HashSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy="cliente")	
	private List<Pedido> pedidos = new ArrayList<>();
	
	public Cliente() {
		this.addPerfil(Perfil.CLIENTE);
	}

	public Cliente(Integer id, String nome, String email, String cpfCnpj, TipoCliente tipo, String senha) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.cpfCnpj = cpfCnpj;
		this.tipo = (tipo == null) ? null : tipo.getCod();
		this.senha = senha;
		this.addPerfil(Perfil.CLIENTE);
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}
	
	//passa o tipo e retorna o enum desse codigo
	public TipoCliente getTipo() {
		return TipoCliente.toEnum(tipo);
	}
	
	//recebe o enum e salva o codigo integer dele
	public void setTipo(TipoCliente tipo) {
		this.tipo = tipo.getCod();
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public Set<Perfil> getPerfis(){
		return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
	}
	
	public void addPerfil(Perfil perfil) {
		perfis.add(perfil.getCod());
	}

	public List<Endereco> getEndrecos() {
		return endrecos;
	}

	public void setEndrecos(List<Endereco> endrecos) {
		this.endrecos = endrecos;
	}

	public Set<String> getTelefones() {
		return telefone;
	}

	public void setTelefones(Set<String> telefones) {
		this.telefone = telefones;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
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
		Cliente other = (Cliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
