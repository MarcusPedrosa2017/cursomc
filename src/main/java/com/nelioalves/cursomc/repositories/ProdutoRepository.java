package com.nelioalves.cursomc.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.domain.Produto;

//ANOTAÇÃO PARA IDENTIFICAR CLASSES DE ACESSO A DADOS, PARA ISSO EXTEND JpaRepository
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer>{
	
	/*
	 * A ANOTACAO @Query VACILITA A IMPLEMENTACAO, POIS NAO E NECESSARIO CRIAR UMA CLASSE QUE IMPLENTE ESTA INTERFACE
	 * PODEMOS COLOCAR A JPQL DENTRO DESTA ANORACAO COMO ARGUMENTO E FRAMEWORK IRA UTILIZALAR ESTA QUERY
	 * 
	 * A ANOTACAO @Param E UTILIZADA PARA DEFINIR OS PARAMETROS DA QUERY*/
	@Transactional(readOnly=true)
	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	Page<Produto> search(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias, Pageable pageRequest);
	
	
	/*ESTA IMPLEMENTACAO USA O PADRAO DE NOMES DO JPA DO SPRING ONDE O FRAMEWORK MONTA A JPQL DE ACORDO COM O PADRAO DE NOMES ADOTADO NA 
	 * ASSINATURA DO METODO, TEM O MESMO EFEITO DO METODO search*/
	@Transactional(readOnly=true)
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
	
}
