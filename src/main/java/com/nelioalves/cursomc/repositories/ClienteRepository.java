package com.nelioalves.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nelioalves.cursomc.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
	
	/*
	 *o spring já sabe que ao criar um metodo com 'findBy' + o nome do atributo como deve procurar, não é necessario implementar o metodo
	 *ele ja se resolve 
	 *
	 *a anotacao @Transactional com o atributo readOnly informa ao spring que nao e necessario realizar uma transacao, sera apenas leitura
	 *dai nao faz o locking no banco, isso deixa mais performatico
	*/
	@Transactional(readOnly=true)
	Cliente findByEmail(String email);
}
