package com.nelioalves.cursomc.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer>{
	
	/*estamos usando o padrao de nome do framework que ja identifica que queremos
	 * fazer uma busca "find" usando um "Cliente", ou seja, vamos buscar um Pedido por um Cliente
	 * 
	 * estamos usando @Transactional(readOnly = true) para evitar locking de banco
	 */
	@Transactional(readOnly = true)
	Page<Pedido> findByCliente(Cliente cliente, Pageable pageRequest);	
	
}
