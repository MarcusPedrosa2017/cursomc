package com.nelioalves.cursomc.domain;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ItemPedido implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * atributo da ItemPedidoPk que faz a junção de muitos para muitos entre a tabela Pedido e ItemPedido
	 * a anotacao usada @EmbeddedId, pois foi usado um tipo auxiliar(classe) para ser a chave composta desta tabela 
	 * 
	 * a antation @JsonIgonre foi colocada para que essa classe nao seja Serializada na resposta do endpoint REST
	*/
	@JsonIgnore
	@EmbeddedId
	private ItemPedidoPK id = new ItemPedidoPK();
	
	private Double desconto;
	private Integer quantidade;
	private Double preco;
		
	public ItemPedido() {		
	}	
	
	public ItemPedido(Pedido pedido, Produto produto, Double desconto, Integer quantidade, Double preco) {
		super();
		/*
		 * como o id desta classe e um atributo de multiplicidade, modificou-se o contrutor para receber os objetos de Pedido e Produto
		 * para setar no id desta classe corretamente, para que haja relacionamento entre as entidades
		*/
		id.setPedido(pedido);
		id.setProduto(produto);
		this.desconto = desconto;
		this.quantidade = quantidade;
		this.preco = preco;		
	}

	/*
	 * criamos os getters para ter acesso direto sem ter que apontar o id antes
	 * 
	 * temos que colocar a anotacao para ignorar, pois e GET de acesso direto a outra classe, fica ciclico
	*/
	@JsonIgnore
	public Pedido getPedido() {
		return id.getPedido();
	}
		
	public Produto getProduto() {
		return id.getProduto();
	}
	
	public Double getDesconto() {
		return desconto;
	}
	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	public Double getPreco() {
		return preco;
	}
	public void setPreco(Double preco) {
		this.preco = preco;
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
		ItemPedido other = (ItemPedido) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	//COMO TEM O get NA FRENTE DO NOME DO METODO O JSON JA SERIALIZA ELE NA RESPOTA
	public Double getSubTotal() {
		return (preco - desconto) * quantidade;
	}
	
}
