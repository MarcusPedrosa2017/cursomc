package com.nelioalves.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nelioalves.cursomc.domain.Produto;

//ANOTAÇÃO PARA IDENTIFICAR CLASSES DE ACESSO A DADOS, PARA ISSO EXTEND JpaRepository
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer>{

}
