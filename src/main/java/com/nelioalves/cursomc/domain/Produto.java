package com.nelioalves.cursomc.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Produto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//ANOTAÇÃO QUE INDICA QUE É A PRIMARY KEY DESTA ENTIDADE DE BANCO
	@Id
	//ANOTAÇÃO QUE INDICA QUE A CHAVE PRIMARYA SERÁ GERÊNCIADA PELO SPRING
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String nome;
	private Double preco;	
	
	/*
	 * MAPEAMENTO DE RELACIONAMENTO, MUITOS PARA MUITOS, EXISTIRA UMA TABELA INTERMEDIARIA CAHAMADA PROCUTO_CATEGORIA QUE IRA JUNTAR AS 
	 * DUAS TABELAS	 * 
	 * A ANOTACAO @JsonManagedReference SERVE PARA EVITAR A REFERENCIA CIRCULAR NA HORA DE SERIALIZAR PARA JSON O RETORNO, ELA TRABALHA EM 
	 * CONJUNTO COM A ANOTACAO @JsonBackReference. NO ATRIBUTO DE RELACIONAMENTO DA OUTRA CLASSE  
	 */
	@JsonBackReference
	@ManyToMany
	@JoinTable(name="PRODUTO_CATEGORIA",
	joinColumns = @JoinColumn(name = "produto_id"),
	inverseJoinColumns = @JoinColumn(name = "categoria_id")	
	)	
	private List<Categoria> categorias = new ArrayList<>();
	
	/*
	 * relacionamento feito exatamente como na entidade Pedido 
	 * 
	 * foi colocado para ignorar @JsonIgnore, pois a partir do Produto nao queremos ver os ItemPedido associados, somente o inverso
	*/
	@JsonIgnore
	@OneToMany(mappedBy="id.produto")
	private Set<ItemPedido> itens = new HashSet<>();
	
	public Produto() {		
	}
	
	public Produto(Integer id, String nome, Double preco) {
		super();
		this.id = id;
		this.nome = nome;
		this.preco = preco;
	}

	/*
	 * como tudo que e GET na e serializado por padrao, foi colocado para ignorar, para que nao haja referencia ciclica entre as classes 
	 * e entidades
	*/
	@JsonIgnore
	public List<Pedido> getPedidos(){
		List<Pedido> lista = new ArrayList<>();
		for(ItemPedido x : itens) {
			lista.add(x.getPedido());
		}
		return lista;
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

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public Set<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(Set<ItemPedido> itens) {
		this.itens = itens;
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
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}
