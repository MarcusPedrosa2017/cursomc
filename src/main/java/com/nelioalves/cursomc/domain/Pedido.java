package com.nelioalves.cursomc.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Pedido implements Serializable{	
	
	private static final long serialVersionUID = 1L;
	
	@Id		
	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	private Integer id;
	
	@JsonFormat(pattern="dd/MM/yyyy HH:mm")
	private Date instante;
	
	//foi usada a anotacao @OneToOne para mostrar a associacao e o cascade pois o JPA precisa, e foi mapeado para o atributo pedido
	//da classe Pagamento, ESTUDAR ISSO DEPOIS
	@JsonManagedReference
	@OneToOne(cascade=CascadeType.ALL, mappedBy="pedido")
	private Pagamento pagamento;
	
	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name="cliente_id")
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(name="endereco_de_entrega_id")
	private Endereco enderecoDeEntrega;
	
	/*
	 * como foi mapeado o relacionamento na tabela ItemPedido, fazemos a associao deste lado com mappedBy passando o atributo que faz o 
	 * relacionamento na outra classe, porem neste caso temos um Id na classe ItemPedido que do tipo ItemPedidoPK, que e a classe que 
	 * faz o relacionamento, ela por sua vez e quem tem o tipo Produto e Pedido, por isso foi mapeado para o atributo Id da ItemPedido
	 * , visando acessar os atributos do tipo Pedido e Produto que ela possui 
	*/
	@OneToMany(mappedBy="id.pedido")
	private Set<ItemPedido> itens = new HashSet<>();
	
	public Pedido() {				
	}

	//foi retirado do construtor o pagamento para não obrigar a passa-lo quando instanciar o Pedido
	public Pedido(Integer id, Date instante, Cliente cliente, Endereco enderecoDeEntrega) {
		super();
		this.id = id;
		this.instante = instante;		
		this.cliente = cliente;
		this.enderecoDeEntrega = enderecoDeEntrega;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getInstante() {
		return instante;
	}

	public void setInstante(Date instante) {
		this.instante = instante;
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Endereco getEnderecoDeEntrega() {
		return enderecoDeEntrega;
	}

	public void setEnderecoDeEntrega(Endereco enderecoDeEntrega) {
		this.enderecoDeEntrega = enderecoDeEntrega;
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
		Pedido other = (Pedido) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
	
}
